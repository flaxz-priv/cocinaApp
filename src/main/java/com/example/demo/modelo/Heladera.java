package com.example.demo.modelo;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.util.ArrayList;

@Entity
@Data
public class Heladera {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // La heladera tiene UNA LISTA de ingredientes guardados
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ingrediente> ingredientes = new ArrayList<>();
}