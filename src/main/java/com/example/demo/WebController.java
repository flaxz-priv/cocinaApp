package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller 
public class WebController {

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