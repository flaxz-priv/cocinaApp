package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dtos.IngredienteDTO;
import com.example.demo.dtos.ItemHeladeraDTO;
import com.example.demo.dtos.RecetaDTO;
import com.example.demo.dtos.SugerenciaDTO;
import com.example.demo.modelo.GestorDeCocina;
import com.example.demo.modelo.Heladera;
import com.example.demo.modelo.Ingrediente;
import com.example.demo.repository.HeladeraRepository;
import com.example.demo.repository.IngredienteRepository;
import org.springframework.ui.Model;
import java.util.List;


@Controller 
public class WebController {
    @Autowired
    private IngredienteRepository ingredienteRepository; // Para buscar la lista

    @Autowired
    private GestorDeCocina gestorDeCocina; // Tu servicio
    
    @Autowired
    private HeladeraRepository heladeraRepository; // Asegúrate de inyectar esto
    // --- VISTA HTML ---
    @GetMapping("/sugerencias")
    public String vistaSugerencias() {
        return "sugerencias"; // Retorna sugerencias.html
    }

    // --- API: OBTENER LISTA FILTRADA ---
    @GetMapping("/api/recetas/sugerencias")
    @ResponseBody
    public List<SugerenciaDTO> obtenerSugerencias() {
        return gestorDeCocina.sugerirRecetas();
    }

    // --- API: COCINAR ---
    @PostMapping("/api/recetas/cocinar/{id}")
    @ResponseBody
    public String cocinar(@PathVariable Long id) {
        gestorDeCocina.cocinarReceta(id);
        return "OK";
    }
    // --- VISTA: CARGAR HELADERA ---
    @GetMapping("/heladera/cargar")
    public String cargarHeladera(Model model) {
        model.addAttribute("ingredientesDisponibles", ingredienteRepository.findAll());
        return "cargar_heladera";
    }

    // --- API: GUARDAR COMPRA ---
    @PostMapping("/api/heladera/agregar")
    @ResponseBody
    public String agregarCompra(@RequestBody List<ItemHeladeraDTO> items) {
        gestorDeCocina.cargarCompra(items);
        return "OK";
    }

    // --- API: CREAR NUEVO INGREDIENTE ---
    @PostMapping("/api/ingredientes/nuevo")
    @ResponseBody
    public Ingrediente crearIngrediente(@RequestBody IngredienteDTO nuevoIngrediente) {
        return gestorDeCocina.crearIngrediente(nuevoIngrediente);
    }
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


    // NUEVO: Ver qué hay en la heladera
    @GetMapping("/heladera/ver")
    public String verHeladera(Model model) {
        // Buscamos la heladera (ID 1)
        Heladera heladera = heladeraRepository.findById(1L).orElse(new Heladera());
        model.addAttribute("items", heladera.getItems());
        return "ver_heladera";
    }

    // --- API: CONSUMIR UN ITEM MANUALMENTE ---
    @PostMapping("/api/heladera/consumir/{id}")
    @ResponseBody
    public String consumirManual(@PathVariable Long id, @RequestParam double cantidad) {
        gestorDeCocina.consumirStock(id, cantidad);
        return "OK";
    }
}