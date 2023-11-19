package com.spring.manytomany.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.manytomany.entidades.Aluno;
import com.spring.manytomany.entidades.Curso;
import com.spring.manytomany.repositories.AlunoRepository;
import com.spring.manytomany.repositories.CursoRepository;

@RestController
@RequestMapping(value = "/api/alunos")
public class AlunoController {

    @Autowired
    private AlunoRepository repository;

    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping
    public List<Aluno> findAll() {
        return repository.findAll();
    }

    @GetMapping(value = "/{id}")
    public Aluno findById(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @PostMapping
    public Aluno create(@RequestBody Aluno aluno) {
        List<Curso> cursos = new ArrayList<>();
        aluno.getCursos().forEach(c -> {
            Curso curso = cursoRepository.findByNomeDoCurso(c.getNome());
            cursos.add(curso != null ? curso : c);
        });

        aluno.getCursos().removeIf(x -> x != null);
        Aluno novoAluno = repository.save(aluno);

        cursos.forEach(curso -> novoAluno.getCursos().add(curso));
        return repository.save(novoAluno);
    }

    @PatchMapping("/{id}")
    public Aluno removeJavaScript(@PathVariable Long id) {
        Aluno aluno = repository.findById(id).orElse(null);
        aluno.getCursos().removeIf(x -> x.getNome() == "HTML");
        return repository.save(aluno);
    }
}
