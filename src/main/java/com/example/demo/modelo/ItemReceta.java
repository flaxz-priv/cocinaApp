package com.example.demo.modelo;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ItemReceta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Ingrediente ingrediente; // De aquí sacamos nombre y unidad

    @ManyToOne
    @JoinColumn(name = "receta_id")
    private Receta receta;

    private double cantidad; // Ej: 0.5 (Significa 0.5 Litros de Leche)
}