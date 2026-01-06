package com.example.demo.modelo;
import com.example.demo.dtos.*;
import com.example.demo.repository.HeladeraRepository;
import com.example.demo.repository.IngredienteRepository;
import com.example.demo.repository.RecetaRepository;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.Optional;

@Service
public class GestorDeCocina {
    @Autowired
    private RecetaRepository recetaRepository; // 2. Inyectamos la instancia (no estático)

    @Autowired
    private IngredienteRepository ingredienteRepository; // 3. Necesario para vincular el ingrediente real

    @Autowired
    private HeladeraRepository heladeraRepository;

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
                Ingrediente ingredienteReal = ingredienteRepository.findById(itemDTO.getIdIngrediente())
                        .orElseThrow(() -> new RuntimeException("Ingrediente no encontrado con ID: " + itemDTO.getIdIngrediente()));
                
                item.setIngrediente(ingredienteReal);

                // C. Guardo el vínculo con la nueva receta
                item.setReceta(nuevaReceta);
                
                return item;
            }).collect(Collectors.toList());

            nuevaReceta.setItems(listaItems);
        }
        System.out.println("Guardando receta: " + nuevaReceta.getNombre());
        recetaRepository.save(nuevaReceta);
        return true; // Retorna true si la carga fue exitosa
    }

    @Transactional
    public void cargarCompra(List<ItemHeladeraDTO> itemsCompra) {
        // 1. Obtenemos la heladera (Asumimos ID 1 siempre)
        Heladera heladera = heladeraRepository.findById(1L).orElse(null);
        if (heladera == null) {
            heladera = new Heladera(); // Si no existe, la creamos
            heladera = heladeraRepository.save(heladera);
        }

        for (ItemHeladeraDTO itemDTO : itemsCompra) {
            // Buscamos si ya existe ese ingrediente en la heladera
            Optional<ItemHeladera> itemExistente = heladera.getItems().stream()
                .filter(i -> i.getIngrediente().getId().equals(itemDTO.getIdIngrediente()))
                .findFirst();

            if (itemExistente.isPresent()) {
                // SI YA ESTÁ: Sumamos la cantidad nueva a la vieja
                ItemHeladera item = itemExistente.get();
                item.setCantidad(item.getCantidad() + itemDTO.getCantidad());
            } else {
                // SI NO ESTÁ: Creamos uno nuevo
                Ingrediente ingredienteReal = ingredienteRepository.findById(itemDTO.getIdIngrediente())
                        .orElseThrow(() -> new RuntimeException("Ingrediente no encontrado"));

                ItemHeladera nuevoItem = new ItemHeladera();
                nuevoItem.setIngrediente(ingredienteReal);
                nuevoItem.setCantidad(itemDTO.getCantidad());
                nuevoItem.setHeladera(heladera); // Vinculamos padre
                
                heladera.getItems().add(nuevoItem);
            }
        }
        // Al guardar la heladera, se guardan/actualizan todos sus items por Cascade
        heladeraRepository.save(heladera);
    }
    public Ingrediente crearIngrediente(IngredienteDTO dto) {
        Ingrediente nuevo = new Ingrediente();
        nuevo.setNombre(dto.getNombre());
        nuevo.setUnidad(dto.getUnidad());
        return ingredienteRepository.save(nuevo);
    }

    public List<SugerenciaDTO> sugerirRecetas() {
        // 1. Traemos todo
        List<Receta> recetas = recetaRepository.findAll();
        Heladera heladera = heladeraRepository.findById(1L).orElse(new Heladera()); // Asumimos heladera 1
        List<ItemHeladera> stock = heladera.getItems();

        List<SugerenciaDTO> sugerencias = new ArrayList<>();

        for (Receta receta : recetas) {
            List<String> faltantes = new ArrayList<>();
            List<String> todosIngredientes = new ArrayList<>();

            // Analizamos cada ingrediente de la receta
            for (ItemReceta itemReceta : receta.getItems()) {
                String nombreIng = itemReceta.getIngrediente().getNombre();
                String unidad = itemReceta.getIngrediente().getUnidad();
                double cantidadNecesaria = itemReceta.getCantidad();

                // Formateamos para mostrar en el detalle
                todosIngredientes.add(nombreIng + ": " + cantidadNecesaria + " " + unidad);

                // Buscamos en la heladera
                Optional<ItemHeladera> itemEnHeladera = stock.stream()
                        .filter(i -> i.getIngrediente().getId().equals(itemReceta.getIngrediente().getId()))
                        .findFirst();

                if (itemEnHeladera.isPresent()) {
                    double cantidadTengo = itemEnHeladera.get().getCantidad();
                    if (cantidadTengo < cantidadNecesaria) {
                        double falta = cantidadNecesaria - cantidadTengo;
                        faltantes.add(nombreIng + " (Falta: " + falta + " " + unidad + ")");
                    }
                } else {
                    // No tengo nada de este ingrediente
                    faltantes.add(nombreIng + " (Falta: " + cantidadNecesaria + " " + unidad + ")");
                }
            }

            // REGLA DE NEGOCIO: Mostrar si faltan 3 o menos ingredientes
            if (faltantes.size() <= 3) {
                SugerenciaDTO dto = new SugerenciaDTO();
                dto.setIdReceta(receta.getId());
                dto.setNombre(receta.getNombre());
                dto.setTipo(receta.getTipoReceta());
                dto.setIngredientesFaltantes(faltantes);
                dto.setEsPosible(faltantes.isEmpty());
                dto.setPasos(receta.getPasos());
                dto.setTodosLosIngredientes(todosIngredientes);
                
                sugerencias.add(dto);
            }
        }
        return sugerencias;
    }

    // --- LÓGICA 2: COCINAR (RESTAR STOCK) ---
    @Transactional
    public void cocinarReceta(Long idReceta) {
        Receta receta = recetaRepository.findById(idReceta).orElseThrow();
        Heladera heladera = heladeraRepository.findById(1L).orElseThrow();

        for (ItemReceta requerido : receta.getItems()) {
            // Buscamos el item en la heladera
            ItemHeladera itemStock = heladera.getItems().stream()
                    .filter(i -> i.getIngrediente().getId().equals(requerido.getIngrediente().getId()))
                    .findFirst()
                    .orElse(null);

            if (itemStock != null) {
                double nuevaCantidad = itemStock.getCantidad() - requerido.getCantidad();
                if (nuevaCantidad <= 0.001) {
                    heladera.getItems().remove(itemStock);
                } else {
                    // Si sobra, solo actualizamos la cantidad
                    itemStock.setCantidad(nuevaCantidad);
                }
            }
        }
        heladeraRepository.save(heladera);
    }
    // --- LÓGICA 3: CONSUMO MANUAL (Consumo rápido) ---
    @Transactional
    public void consumirStock(Long idItemHeladera, double cantidad) {
        Heladera heladera = heladeraRepository.findById(1L).orElseThrow();
        
        // Buscamos el item específico por su ID único
        ItemHeladera itemTarget = heladera.getItems().stream()
                .filter(i -> i.getId().equals(idItemHeladera))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item no encontrado en heladera"));
                
        double nuevaCantidad = itemTarget.getCantidad() - cantidad;
        
        // Si se acaba (o queda negativo por error), lo borramos
        if (nuevaCantidad <= 0.001) {
             heladera.getItems().remove(itemTarget);
        } else {
             itemTarget.setCantidad(nuevaCantidad);
        }
        
        heladeraRepository.save(heladera);
    }
}
