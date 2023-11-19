package com.authentication.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.authentication.repositories.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    // trata-se de um middleware

    /*
        O doFilterInternal é um  middleware. Recebe como parâmetros:
        HttpServletRequest request -> similar ao simples req do Express JS
        HttpServletResponse response -> similar ao res do Express JS
        FilterChain filterChain -> similar ao next() do Express, mas repassa req e res, como:
            filterChain.doFilter(request, response)
    */

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = recoverToken(request); // extrai o token
        if (token != null) {
            var subject = tokenService.validateToken(token); // pega esse token, valida e extrai email

            // localiza o usuário na base de dados
            UserDetails user = userRepository.findByEmail(subject);

            // salva esse usuário autenticado no contexto da requisição para posterior verificação
            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);

        // o método vai tentar buscar as informações e adicionar no contexto da requisição
        // se não validar ou estiver incorreto, ele passa para o próximo filtro (middleware)
        // e como não vai ter autenticado, resultará em erro 403 (unalthorized)
    }

    // extrai o token do Header da requisição
    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;

        return authHeader.split(" ")[1]; // separa em ["Bearer", "token_jwt_informado"] e pega o índice 1
    }
}
