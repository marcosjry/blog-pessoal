package com.blog.pessoal.acelera.maker.config;

import com.blog.pessoal.acelera.maker.service.TokenService;
import com.blog.pessoal.acelera.maker.service.UsuarioService;
import com.blog.pessoal.acelera.maker.util.EndpointConstants;
import com.blog.pessoal.acelera.maker.util.SecretUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    public SecurityFilter securityFilter(TokenService tokenService, UsuarioService usuarioService) {
        return new SecurityFilter(tokenService, usuarioService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, SecurityFilter securityFilter) throws Exception {
        return httpSecurity
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.sendRedirect("/acesso-negado");
                        }))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/swagger-ui.html").permitAll()
                        .requestMatchers("/swagger-resources/**").permitAll()
                        .requestMatchers("/webjars/**").permitAll()
                        .requestMatchers(HttpMethod.POST, EndpointConstants.USUARIOS).permitAll()
                        .requestMatchers(HttpMethod.GET, EndpointConstants.USUARIOS_LOGIN).permitAll()
                        .requestMatchers(HttpMethod.POST,EndpointConstants.USUARIOS_LOGIN).permitAll()// Permite o acesso ao login
                        .requestMatchers(HttpMethod.POST, EndpointConstants.USUARIOS).hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, EndpointConstants.USUARIOS_ID).hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, EndpointConstants.USUARIOS_ID).hasRole("USER")
                        .requestMatchers(HttpMethod.POST, EndpointConstants.TEMAS).hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, EndpointConstants.TEMAS_ID).hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, EndpointConstants.TEMAS_ID).hasRole("USER")
                        .requestMatchers(HttpMethod.GET, EndpointConstants.TEMAS).hasRole("USER")
                        .requestMatchers(HttpMethod.POST, EndpointConstants.POSTAGENS).hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, EndpointConstants.POSTAGENS_ID).hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, EndpointConstants.POSTAGENS_ID).hasRole("USER")
                        .requestMatchers(HttpMethod.GET, EndpointConstants.POSTAGENS_FILTRO).hasRole("USER")
                        .requestMatchers(HttpMethod.GET, EndpointConstants.POSTAGENS).hasRole("USER")
                        .anyRequest().authenticated() // Exige autenticação para outras rotas
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Lazy
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecretUtil securitySecrets() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Resource resource = new ClassPathResource("secrets.json");
            // Lê o conteúdo do arquivo como InputStream
            return mapper.readValue(resource.getInputStream(), SecretUtil.class);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível carregar a secret", e);
        }
    }
}
