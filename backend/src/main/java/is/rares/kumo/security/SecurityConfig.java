package is.rares.kumo.security;

import is.rares.kumo.controller.AuthenticationController;
import is.rares.kumo.security.annotation.NotAuthenticated;
import is.rares.kumo.utils.DefaultUtils;
import is.rares.kumo.utils.ReflectionUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@Slf4j
public class SecurityConfig {
    private final AuthorizationInterceptor authorizationInterceptor;
    private final GenericApplicationContext applicationContext;
    private final ApplicationArguments applicationArguments;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests(authorize -> {
                    applyAnnotationDeclaredAuthentication(authorize);

                    authorize
                            .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                            .requestMatchers("/swagger-resources/**").permitAll()

                            .requestMatchers("/api/websocket").permitAll()

                            .anyRequest().permitAll();
                })
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(authorizationInterceptor, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    public void applyAnnotationDeclaredAuthentication(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorizationConfig) {
        var classes = ReflectionUtils.findClassesAnnotatedWith(AuthenticationController.class.getPackageName(), RestController.class);

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
            String requestPath = DefaultUtils.combineNullableURLs(basePath, DefaultUtils.firstOrNull(requestMappingAnnotation.path()));
            if (requestMethod != null && requestPath != null) {
                log.info("Added permitAll for {} {}", requestMethod.asHttpMethod(), requestPath);
                authorizationConfig.requestMatchers(requestMethod.asHttpMethod(), requestPath).permitAll();
            }
        }
    }

    private boolean applyingAuthenticationForHttpMethodIsPossible(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorizationConfig, String basePath, String annotationRequestPath, HttpMethod httpMethod) {
        String requestPath = DefaultUtils.combineNullableURLs(basePath, annotationRequestPath);
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
