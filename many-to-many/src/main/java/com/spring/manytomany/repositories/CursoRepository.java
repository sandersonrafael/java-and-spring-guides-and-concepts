package com.spring.manytomany.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spring.manytomany.entidades.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long> {

    @Query("SELECT c FROM Curso c WHERE c.nome = :nome")
    public Curso findByNomeDoCurso(String nome);
}
