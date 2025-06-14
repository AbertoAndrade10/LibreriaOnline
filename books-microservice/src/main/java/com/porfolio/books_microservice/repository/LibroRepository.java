package com.porfolio.books_microservice.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.porfolio.books_microservice.model.Libro;

public interface LibroRepository extends MongoRepository<Libro, String> {

    @SuppressWarnings("null")
    List<Libro> findAll();

    List<Libro> findByTituloContainingIgnoreCase(String titulo);

    List<Libro> findByGeneroIgnoreCase(String genero);

    @Aggregation(pipeline = {
            "{ $group: { _id: null, generosUnicos: { $addToSet: \"$genero\" } }}",
            "{ $project: { _id: 0, generosUnicos: 1 }}",
            "{ $sort: { generosUnicos: 1 }}"
    })
    List<String> findDistinctGeneros();

    @SuppressWarnings("null")
    Page<Libro> findAll(Pageable pageable);

    Page<Libro> findByGeneroIgnoreCase(String genero, Pageable pageable);
}
