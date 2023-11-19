package com.authentication.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.authentication.entities.user.User;

@Service
public class TokenService {

    // Pega o valor do application.properties ou application.yml
    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${api.security.token.expiresAt}")
    private Long expiresAt;

    public String generateToken(User user) {
        try { // determina quem será o algorítimo do jwt
            Algorithm algorithm = Algorithm.HMAC256(secret);

            String token = JWT.create()
                .withIssuer("api-jwt") // representa quem criou - usar nome da aplicação
                .withSubject(user.getEmail()) // representação / login do usuário
                .withExpiresAt(getExpireTime(expiresAt)) // tempo até expirar em milissegundos
                .sign(algorithm); // tipo de algoritmo de criptografia

            return token;

        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao tentar criar o token");
        }
    }

    public String validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        try {
            return JWT.require(algorithm) // valida o token e extrai o subject (email nesse caso)
                .withIssuer("api-jwt")
                .build()
                .verify(token)
                .getSubject();
        } catch (JWTVerificationException exception) {
            return "";
        }
    }

    private Date getExpireTime(Long milliseconds) {
        return new Date(new Date().getTime() + milliseconds);
    }
}
