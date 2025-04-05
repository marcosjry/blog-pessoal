package com.blog.pessoal.acelera.maker.config;

import com.blog.pessoal.acelera.maker.service.TokenService;
import com.blog.pessoal.acelera.maker.service.UsuarioService;
import com.blog.pessoal.acelera.maker.util.SecretUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
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
                        .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/usuarios/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/usuarios/login").permitAll()// Permite o acesso ao login
                        .requestMatchers(HttpMethod.POST, "/api/usuarios").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/api/usuarios/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/usuarios/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/temas").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/api/temas/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/temas/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/temas").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/postagens").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/api/postagens").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/postagens/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/postagens/filtro?").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/postagens").hasRole("USER")
                        .anyRequest().authenticated() // Exige autenticação para outras rotas
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

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
