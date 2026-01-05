package com.example.demo.modelo;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.util.ArrayList;

@Entity
@Data
public class Heladera {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "heladera", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemHeladera> items = new ArrayList<>();
}