package com.authentication.controllers.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authentication.entities.user.AuthenticationDTO;
import com.authentication.entities.user.LoginResponseDTO;
import com.authentication.entities.user.RegisterDTO;
import com.authentication.entities.user.User;
import com.authentication.entities.user.UserRole;
import com.authentication.repositories.UserRepository;
import com.authentication.security.TokenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired // pega o valor do SecurityConfiguration (classe de config do spring security)
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository repository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO data) {
        // usado para encapsular as informações no spring security (deixar de um modo para ser trafegado)
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email().toLowerCase(), data.password());

        // usado para verificar se as informações estão corretas e realizar a autenticação
        var auth = this.authenticationManager.authenticate(usernamePassword);

        // extrai o conteúdo principal do auth, que seria o usuário e faz casting para User para
        // se tornar compatível e válido para gerar o token
        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO data) {
        if (repository.findByEmail(data.email()) != null) return ResponseEntity.badRequest().build();

        String passwordHash = new BCryptPasswordEncoder().encode(data.password());

        User newUser = new User(null, data.email().toLowerCase(), passwordHash, data.fullName(), data.birthDate(), UserRole.USER);
        repository.save(newUser);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
