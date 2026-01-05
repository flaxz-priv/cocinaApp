package com.example.demo.repository;

import com.example.demo.modelo.Heladera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeladeraRepository extends JpaRepository<Heladera, Long> {
}