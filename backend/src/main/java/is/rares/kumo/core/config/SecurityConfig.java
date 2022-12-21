package is.rares.kumo.core.config;

import is.rares.kumo.security.AuthorizationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final AuthorizationInterceptor authorizationInterceptor;

    public SecurityConfig(AuthorizationInterceptor authorizationInterceptor) {
        this.authorizationInterceptor = authorizationInterceptor;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorize -> authorize
//                        .antMatchers("/api/authenticate/**").permitAll()
                        .antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
//                        .antMatchers("/webjars/**").permitAll()
//                        .antMatchers("/swagger-resources/**").permitAll()
//                        .antMatchers("/swagger-resources").permitAll()
                        .anyRequest().authenticated()
                );
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

//        http.addFilterBefore(authorizationInterceptor, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
