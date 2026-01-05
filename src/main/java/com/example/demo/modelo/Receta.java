package com.example.demo.modelo;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class Receta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre; 

    @OneToMany(mappedBy = "receta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemReceta> items;

    // Para guardar una lista de textos simples (pasos)
    @ElementCollection
    private List<String> pasos;

    @Enumerated(EnumType.STRING) 
    private Tipo tipoReceta;
}