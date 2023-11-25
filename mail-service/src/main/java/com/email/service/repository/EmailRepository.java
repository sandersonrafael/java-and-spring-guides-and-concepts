package com.email.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.email.service.models.EmailModel;

public interface EmailRepository extends JpaRepository<EmailModel, Long> {

}
