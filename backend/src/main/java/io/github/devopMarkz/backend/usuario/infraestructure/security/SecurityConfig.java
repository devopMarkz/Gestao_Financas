package io.github.devopMarkz.backend.usuario.infraestructure.security;

import io.github.devopMarkz.backend.usuario.infraestructure.security.handlers.CustomAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthenticationFilterService authenticationFilterService,
                                                   CustomAuthenticationEntryPoint authenticationEntryPoint) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests -> {
                    authorizeRequests.requestMatchers(
                            "/v2/api-docs/**",
                            "/v3/api-docs/**",
                            "/swagger-resources/**",
                            "/swagger-ui.html",
                            "/swagger-ui/**",
                            "/webjars/**",
                            "/actuator/**"
                    ).permitAll();
                    authorizeRequests.requestMatchers(HttpMethod.POST, "/auth/login").permitAll();
                    authorizeRequests.requestMatchers(HttpMethod.POST, "/usuarios").permitAll();
                    authorizeRequests.anyRequest().authenticated();
                })
                .exceptionHandling(exceptions -> {
                    exceptions.authenticationEntryPoint(authenticationEntryPoint);
                })
                .addFilterBefore(authenticationFilterService, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

}
