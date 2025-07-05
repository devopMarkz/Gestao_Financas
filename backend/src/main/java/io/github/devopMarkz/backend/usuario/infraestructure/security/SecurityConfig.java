package io.github.devopMarkz.backend.usuario.infraestructure.security;

import io.github.devopMarkz.backend.usuario.infraestructure.security.handlers.CustomAccessDeniedHandler;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Classe responsável por configurar a segurança da aplicação utilizando Spring Security.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    /**
     * Configura a cadeia de filtros de segurança, definindo políticas de autenticação, permissões e CORS.
     *
     * @param http                       configuração de segurança HTTP.
     * @param authenticationFilterService filtro personalizado de autenticação baseado em JWT.
     * @param authenticationEntryPoint    manipulador de erro para autenticação inválida (401).
     * @param accessDeniedHandler         manipulador de erro para acesso negado (403).
     * @return o filtro de segurança configurado.
     * @throws Exception em caso de erro de configuração.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthenticationFilterService authenticationFilterService,
                                                   CustomAuthenticationEntryPoint authenticationEntryPoint,
                                                   CustomAccessDeniedHandler accessDeniedHandler) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests -> {
                    // Endpoints públicos
                    authorizeRequests.requestMatchers(
                            "/v2/api-docs/**",
                            "/v3/api-docs/**",
                            "/swagger-resources/**",
                            "/swagger-ui.html",
                            "/swagger-ui/**",
                            "/webjars/**",
                            "/actuator/**"
                    ).permitAll();

                    // Endpoints permitidos sem autenticação
                    authorizeRequests.requestMatchers(HttpMethod.POST, "/auth/login").permitAll();
                    authorizeRequests.requestMatchers(HttpMethod.POST, "/usuarios").permitAll();

                    // Demais endpoints requerem autenticação
                    authorizeRequests.anyRequest().authenticated();
                })
                .exceptionHandling(exceptions -> {
                    exceptions.authenticationEntryPoint(authenticationEntryPoint);
                    exceptions.accessDeniedHandler(accessDeniedHandler);
                })
                .addFilterBefore(authenticationFilterService, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Bean responsável por fornecer o encoder de senhas utilizado na aplicação.
     *
     * @return instância de {@link BCryptPasswordEncoder}.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean que expõe o {@link AuthenticationManager}, necessário para o fluxo de autenticação.
     *
     * @param authConfig configuração de autenticação padrão do Spring.
     * @return o {@link AuthenticationManager} configurado.
     * @throws Exception em caso de falha na configuração.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Configuração de CORS da aplicação. Define as origens, métodos e cabeçalhos permitidos.
     *
     * @return configuração de origem para requisições CORS.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:8080", "http://192.168.100.52:8080"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
