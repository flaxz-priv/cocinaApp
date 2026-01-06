package com.example.demo.dtos;

import com.example.demo.modelo.Tipo;
import lombok.Data;
import java.util.List;

@Data
public class SugerenciaDTO {
    private Long idReceta;
    private String nombre;
    private Tipo tipo;          // VEGANO, CARNIVORO...
    private List<String> ingredientesFaltantes; // Ej: ["200 gr de Queso", "1 u de Cebolla"]
    private boolean esPosible;  // true si no falta nada
    
    // Para el detalle (Modal)
    private List<String> pasos;
    private List<String> todosLosIngredientes; // Lista formateada para leer fácil
}