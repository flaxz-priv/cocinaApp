package com.example.demo.dtos;
import lombok.Data;
@Data
public class IngredienteDTO {
        

    private Long id; // Todo ingrediente necesita un ID único

    private String nombre;
    private String unidad; // Ej: "gr", "kg", "unidad"
}
