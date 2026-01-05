package com.example.demo.modelo;
import com.example.demo.dtos.*;
import com.example.demo.repository.IngredienteRepository;
import com.example.demo.repository.RecetaRepository;
import com.example.demo.modelo.Ingrediente;
import java.util.stream.Collectors;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GestorDeCocina {
    @Autowired
    private RecetaRepository recetaRepository; // 2. Inyectamos la instancia (no estático)

    @Autowired
    private IngredienteRepository ingredienteRepository; // 3. Necesario para vincular el ingrediente real

    public boolean CargarReceta(RecetaDTO receta) {
        Receta nuevaReceta = new Receta();
        nuevaReceta.setNombre(receta.getNombre());
        nuevaReceta.setTipoReceta(receta.getTipoReceta());
        nuevaReceta.setPasos(receta.getPasos());
        if (receta.getItems() != null) {
            List<ItemReceta> listaItems = receta.getItems().stream().map(itemDTO -> {
                ItemReceta item = new ItemReceta();
                
                // A. Seteamos la cantidad
                item.setCantidad(itemDTO.getCantidad());

                // B. Buscamos el ingrediente real en la DB (usando el ID o Nombre que venga en el DTO)
                // Asumo que tu ItemRecetaDTO trae el ID del ingrediente.
                Ingrediente ingredienteReal = ingredienteRepository.findById(itemDTO.getIngrediente().getId())
                        .orElseThrow(() -> new RuntimeException("Ingrediente no encontrado con ID: " + itemDTO.getIngrediente().getId()));
                
                item.setIngrediente(ingredienteReal);

                // C. Guardo el vínculo con la nueva receta
                item.setReceta(nuevaReceta);
                
                return item;
            }).collect(Collectors.toList());

            nuevaReceta.setItems(listaItems);
        }
        recetaRepository.save(nuevaReceta);
        return true; // Retorna true si la carga fue exitosa
    }
}
