package com.example.demo.modelo;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
public class ItemHeladera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Ingrediente ingrediente; 

    @ManyToOne
    @JoinColumn(name = "heladera_id")
    @ToString.Exclude          
    @EqualsAndHashCode.Exclude 
    private Heladera heladera;

    private double cantidad; // Ej: 1.0 (Tienes 1 Litro entero)
}