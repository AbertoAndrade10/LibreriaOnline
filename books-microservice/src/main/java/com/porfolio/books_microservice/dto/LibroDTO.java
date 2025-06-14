package com.porfolio.books_microservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibroDTO {
    private String id;
    private String titulo;
    private String autor;
    private String genero;
    private Double precio;
    private Integer stock;
    private Boolean disponible;
    private String imageUrl;
    private String description;
    
}
