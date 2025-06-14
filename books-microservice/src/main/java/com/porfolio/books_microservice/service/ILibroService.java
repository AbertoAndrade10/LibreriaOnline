package com.porfolio.books_microservice.service;

import java.util.List;
import java.util.Optional;



import com.porfolio.books_microservice.dto.LibroDTO;
import com.porfolio.books_microservice.dto.LibroRequestDTO;

public interface ILibroService {
    List<LibroDTO> getAllLibros();

    Optional<LibroDTO> getLibroById(String id);

    LibroDTO createLibro(LibroRequestDTO requestDTO);

    LibroDTO updateLibro(String id, LibroRequestDTO requestDTO);

    List<LibroDTO> getLibrosByTitulo(String titulo);

    List<LibroDTO> getLibroByGenero(String genero);

    List<String> getCategorias();

    public LibroDTO updateImagen(String id, String nuevaImagenUrl);

    void deleteLibro(String id);

    

   

}
