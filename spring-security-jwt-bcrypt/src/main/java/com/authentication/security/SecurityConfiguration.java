package com.authentication.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    /*
        O filter é como se fosse um  middleware. Recebe como parâmetros:
        HttpServletRequest request -> similar ao simples req do Express JS
        HttpServletResponse response -> similar ao res do Express JS
        FilterChain filterChain -> similar ao next() do Express. Repassa req e res, mas é necessário
        chamar: filterChain.doFilter(request, response)
    */

    @Autowired
    private SecurityFilter securityFilter;

    @Bean // É quem define as rotas e suas autorizações / restrições
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // desabilitar csrf, pois aplicação é stateless (não armazena estados e sim recebe tokens)
            .sessionManagement( // declara que é uma sessão STATELESS (servidor não armazena sessões ativas)
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            ).authorizeHttpRequests( // define os tipos de requisições, urls das requisições e suas permissões
                auth -> auth
                    .requestMatchers(HttpMethod.POST, "/api/product").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/register").permitAll()
                    .anyRequest().authenticated()
            ) // adiciona um filtro para verificar o token e extrair as informações do user
              // antes do filtro UsernamePasswordAuthenticationFilter.class que é um filtro pertencente ao próprio contexto do spring security
            .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean // bean que é usada na classe AuthenticationController para autenticar os dados fornecidos
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean // para utilização do BCrypt
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
