package com.example.demo.repository;
import com.example.demo.modelo.ItemReceta;
import org.springframework.data.jpa.repository.JpaRepository;       
import org.springframework.stereotype.Repository;
@Repository
public interface ItemRecetaRepository extends JpaRepository<ItemReceta, Long> {
}