package com.example.demo.dtos;

import java.util.List;
import com.example.demo.dtos.ItemRecetaDTO;
import com.example.demo.modelo.Tipo;
import lombok.Data;

@Data
public class RecetaDTO {

    private Long id;

    private String nombre; 

    private List<ItemRecetaDTO> items;

    private List<String> pasos;

    private Tipo tipoReceta;
}
