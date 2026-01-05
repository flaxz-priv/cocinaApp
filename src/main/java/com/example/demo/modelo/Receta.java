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

    // Una receta tiene MUCHOS ingredientes
    @OneToMany(cascade = CascadeType.ALL) 
    private List<Ingrediente> ingredientes; 

    // Para guardar una lista de textos simples (pasos)
    @ElementCollection
    private List<String> pasos;

    @Enumerated(EnumType.STRING) 
    private Tipo tipoReceta;
}