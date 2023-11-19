package com.spring.manytomany.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.manytomany.entidades.Aluno;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

}
