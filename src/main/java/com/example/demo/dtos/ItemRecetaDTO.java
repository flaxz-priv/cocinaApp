package com.example.demo.dtos;

import com.example.demo.modelo.Ingrediente;
import com.example.demo.modelo.Receta;
import lombok.Data;

@Data
public class ItemRecetaDTO {
    private Long id;


    private Ingrediente ingrediente; // De aquí sacamos nombre y unidad

    private Receta receta;

    private double cantidad; // Ej: 0.5 (Significa 0.5 Litros de Leche)
}
