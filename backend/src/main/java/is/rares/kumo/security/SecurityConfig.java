package is.rares.kumo.security;

import is.rares.kumo.controller.AuthenticationController;
import is.rares.kumo.security.annotation.NotAuthenticated;
import is.rares.kumo.utils.DefaultUtils;
import is.rares.kumo.utils.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {
    private final AuthorizationInterceptor authorizationInterceptor;

    public SecurityConfig(AuthorizationInterceptor authorizationInterceptor) {
        this.authorizationInterceptor = authorizationInterceptor;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests(authorize -> {
                    applyAnnotationDeclaredAuthentication(authorize);

                    authorize
                            .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                            .requestMatchers("/swagger-resources/**").permitAll()
                            .anyRequest().authenticated();
                })
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(authorizationInterceptor, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    public void applyAnnotationDeclaredAuthentication(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorizationConfig) {
        var classes = ReflectionUtils.getAllClassesFromPackageRecursive(AuthenticationController.class.getPackageName());

        for (Class<?> clazz : classes) {
            if (!clazz.getName().endsWith("Controller"))
                continue;

            String basePath = findRequestMappingBasePath(clazz);

            for (Method method : clazz.getMethods()) {
                if (!method.isAnnotationPresent(NotAuthenticated.class))
                    continue;

                processAuthenticationForMethod(basePath, method, authorizationConfig);
            }
        }
    }

    private void processAuthenticationForMethod(String basePath, Method method, AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorizationConfig) {
        var getMappingAnnotation = AnnotationUtils.findAnnotation(method, GetMapping.class);
        if (getMappingAnnotation != null && applyingAuthenticationForHttpMethodIsPossible(authorizationConfig, basePath, DefaultUtils.firstOrNull(getMappingAnnotation.path()), HttpMethod.GET)) {
            return;
        }

        var postMappingAnnotation = AnnotationUtils.findAnnotation(method, PostMapping.class);
        if (postMappingAnnotation != null && applyingAuthenticationForHttpMethodIsPossible(authorizationConfig, basePath, DefaultUtils.firstOrNull(postMappingAnnotation.path()), HttpMethod.POST)) {
            return;
        }

        var putMappingAnnotation = AnnotationUtils.findAnnotation(method, PutMapping.class);
        if (putMappingAnnotation != null && applyingAuthenticationForHttpMethodIsPossible(authorizationConfig, basePath, DefaultUtils.firstOrNull(putMappingAnnotation.path()), HttpMethod.PUT)) {
            return;
        }

        var deleteMappingAnnotation = AnnotationUtils.findAnnotation(method, DeleteMapping.class);
        if (deleteMappingAnnotation != null && applyingAuthenticationForHttpMethodIsPossible(authorizationConfig, basePath, DefaultUtils.firstOrNull(deleteMappingAnnotation.path()), HttpMethod.DELETE)) {
            return;
        }

        var patchMappingAnnotation = AnnotationUtils.findAnnotation(method, PatchMapping.class);
        if (patchMappingAnnotation != null && applyingAuthenticationForHttpMethodIsPossible(authorizationConfig, basePath, DefaultUtils.firstOrNull(patchMappingAnnotation.path()), HttpMethod.PATCH)) {
            return;
        }

        var requestMappingAnnotation = AnnotationUtils.findAnnotation(method, RequestMapping.class);
        if (requestMappingAnnotation != null) {
            RequestMethod requestMethod = DefaultUtils.firstOrNull(requestMappingAnnotation.method());
            String requestPath = DefaultUtils.combineNullablePaths(basePath, DefaultUtils.firstOrNull(requestMappingAnnotation.path()));
            if (requestMethod != null && requestPath != null) {
                log.info("Added permitAll for {} {}", requestMethod.asHttpMethod(), requestPath);
                authorizationConfig.requestMatchers(requestMethod.asHttpMethod(), requestPath).permitAll();
            }
        }
    }

    private boolean applyingAuthenticationForHttpMethodIsPossible(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorizationConfig, String basePath, String annotationRequestPath, HttpMethod httpMethod) {
        String requestPath = DefaultUtils.combineNullablePaths(basePath, annotationRequestPath);
        if (requestPath != null) {
            log.info("Added permitAll for {} {}", httpMethod, requestPath);
            authorizationConfig.requestMatchers(httpMethod, requestPath).permitAll();
            return true;
        }
        return false;
    }

    private String findRequestMappingBasePath(Class<?> clazz) {
        var annotation = AnnotationUtils.findAnnotation(clazz, RequestMapping.class);
        if (annotation != null)
            return DefaultUtils.firstOrNull(annotation.path());
        return null;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
