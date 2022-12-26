package is.rares.kumo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import is.rares.kumo.core.exceptions.ErrorResponse;
import is.rares.kumo.core.exceptions.KumoException;
import is.rares.kumo.core.exceptions.codes.AccountCodeErrorCodes;
import is.rares.kumo.security.entity.CurrentUser;
import is.rares.kumo.security.token.AsyncTokenStore;
import is.rares.kumo.security.token.TokenStore;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order
public class AuthorizationInterceptor extends OncePerRequestFilter {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String REFRESH_HEADER = "Refresh-Token";
    private static final String BEARER_ATTRIBUTE = "Bearer "; // space is mandatory
    private static final String VALIDATE_2FA_ENDPOINT = "/validate2FA";

    private final JwtUserService userService;
    private final ObjectMapper objectMapper;
    private final AsyncTokenStore asyncTokenStore;
    private final TokenStore tokenStore;

    public AuthorizationInterceptor(JwtUserService userService,
                                    ObjectMapper objectMapper,
                                    AsyncTokenStore asyncTokenStore,
                                    TokenStore tokenStore) {
        this.userService = userService;
        this.objectMapper = objectMapper;
        this.asyncTokenStore = asyncTokenStore;
        this.tokenStore = tokenStore;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        try {
            this.authUser(request);
            this.refreshToken(request);

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            final ErrorResponse error = new ErrorResponse(AccountCodeErrorCodes.UNEXPECTED_ERROR, "Invalid authentication");

            response.setStatus(error.getHttpStatus().value());
            response.getWriter().write(objectMapper.writeValueAsString(error));
            response.setContentType("application/json");
        }
    }

    private void authUser(HttpServletRequest request) {
        final String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (authHeader != null && authHeader.startsWith(BEARER_ATTRIBUTE))
            this.setPrincipal(authHeader.substring(BEARER_ATTRIBUTE.length()), request.getRequestURL().toString(), false);
    }

    private void setPrincipal(String jwt, String requestUrl, boolean isRefreshFlowActive) {
        if (jwt == null)
            return;

        tokenStore.checkTokenValidity(jwt);

        final CurrentUser currentUser = userService.loadUserByUsername(jwt);
        if (!isRefreshFlowActive) {
            this.validate2FA(currentUser, requestUrl);
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(currentUser, null, currentUser.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        this.asyncTokenStore.updateTokenUsage(jwt);
    }

    private void validate2FA(CurrentUser currentUser, String requestUrl) {
        if (currentUser.isUsing2FA() && currentUser.isTwoFANeeded() && !requestUrl.contains(VALIDATE_2FA_ENDPOINT))
            throw new KumoException(AccountCodeErrorCodes.TWO_FACTOR_MISSING, "Missing 2fa authentication");
    }


    private void refreshToken(HttpServletRequest request) {
        final String authHeader = request.getHeader(REFRESH_HEADER);
        if (authHeader != null && authHeader.startsWith(BEARER_ATTRIBUTE))
            this.setPrincipal(authHeader.substring(BEARER_ATTRIBUTE.length()), request.getRequestURL().toString(), true);
    }
}
