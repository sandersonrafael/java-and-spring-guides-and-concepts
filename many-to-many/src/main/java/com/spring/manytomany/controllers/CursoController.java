package com.spring.manytomany.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.manytomany.entidades.Curso;
import com.spring.manytomany.repositories.CursoRepository;

@RestController
@RequestMapping(value = "/api/cursos")
public class CursoController {

    @Autowired
    private CursoRepository repository;

    @GetMapping
    public List<Curso> findAll() {
        return repository.findAll();
    }

    @GetMapping(value = "/{id}")
    public Curso findById(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @PostMapping
    public Curso create(@RequestBody Curso curso) {
        return repository.save(curso);
    }

    public Curso findByNomeCurso(String nome) {
        return repository.findByNomeDoCurso(nome);
    }
}
