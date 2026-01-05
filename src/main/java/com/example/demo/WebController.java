package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import com.example.demo.dtos.RecetaDTO;
import com.example.demo.modelo.GestorDeCocina;
import com.example.demo.repository.IngredienteRepository;
import org.springframework.ui.Model;


@Controller 
public class WebController {
    @Autowired
    private IngredienteRepository ingredienteRepository; // Para buscar la lista

    @Autowired
    private GestorDeCocina gestorDeCocina; // Tu servicio

    // 1. Mostrar el formulario (GET)
    @GetMapping("/recetas/cargar")
    public String mostrarCargarRecetas(Model model) {
        // Pasamos la lista de ingredientes al HTML para que el usuario elija
        model.addAttribute("ingredientesDisponibles", ingredienteRepository.findAll());
        return "cargar_recetas";
    }

    // 2. Recibir la receta (POST - JSON)
    @PostMapping("/api/recetas/guardar")
    @ResponseBody // Importante: Esto indica que respondemos datos, no una vista HTML
    public String guardarReceta(@RequestBody RecetaDTO recetaDTO) {
        boolean exito = gestorDeCocina.CargarReceta(recetaDTO);
        return exito ? "OK" : "ERROR";
    }

    // Menú Principal
    @GetMapping("/")
    public String home() {
        return "menu"; 
    }

    @GetMapping("/sugerencias")
    public String sugerencias() {
        return "sugerencias"; 
    }

}