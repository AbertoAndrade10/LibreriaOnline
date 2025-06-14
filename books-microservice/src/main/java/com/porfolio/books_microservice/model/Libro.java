package com.porfolio.books_microservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "libros")
public class Libro {

    @Id
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
