package com.authentication.entities.user;

import java.time.LocalDate;

public record RegisterDTO(
    String email,
    String password,
    String fullName,
    LocalDate birthDate,
    UserRole role
) {}
