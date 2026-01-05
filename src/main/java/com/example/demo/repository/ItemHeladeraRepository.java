package com.example.demo.repository;
import com.example.demo.modelo.ItemHeladera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ItemHeladeraRepository extends JpaRepository<ItemHeladera, Long> {
}
