package com.authentication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.authentication.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
