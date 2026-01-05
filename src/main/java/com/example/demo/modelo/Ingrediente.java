package com.example.demo.modelo;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data // Genera getters y setters automaticos
public class Ingrediente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Todo ingrediente necesita un ID único

    private String nombre;
    private String unidad; // Ej: "gr", "kg", "unidad"
    private int cantidad;
}