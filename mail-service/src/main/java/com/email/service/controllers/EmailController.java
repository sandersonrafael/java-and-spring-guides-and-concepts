package com.email.service.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.email.service.dtos.EmailDTO;
import com.email.service.models.EmailModel;
import com.email.service.services.EmailService;

import jakarta.validation.Valid;

@RestController
public class EmailController {

    @Autowired
    private EmailService service;

    @PostMapping("/sending-mail")
    public ResponseEntity<EmailModel> sendingEmail(@RequestBody @Valid EmailDTO dto) {
        EmailModel emailModel = new EmailModel();
        BeanUtils.copyProperties(dto, emailModel); // BeanUtils.copyProperties é similar ao ModelMapper
        service.sendEmail(emailModel);
        return new ResponseEntity<>(emailModel, HttpStatus.CREATED);
    }
}
