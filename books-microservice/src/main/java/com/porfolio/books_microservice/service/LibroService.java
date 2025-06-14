package com.porfolio.books_microservice.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.porfolio.books_microservice.dto.LibroDTO;
import com.porfolio.books_microservice.dto.LibroRequestDTO;
import com.porfolio.books_microservice.model.Libro;
import com.porfolio.books_microservice.repository.LibroRepository;

@Service
public class LibroService implements ILibroService {

    private static final Logger logger = LoggerFactory.getLogger(LibroService.class);

    @Autowired
    private final LibroRepository libroRepository;

    public LibroService(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    @Override
    public List<LibroDTO> getAllLibros() {
        try {
            logger.info("Obteniendo todos los libros");
            List<Libro> libros = libroRepository.findAll();
            logger.info("Libros encontrados: {}", libros.size());

            return libros.stream()
                    .map(this::convertToDTO)
                    .toList();
        } catch (Exception e) {
            logger.error("Error al obtener libros", e);
            throw e;
        }
    }
    /*
     * public List<LibroDTO> getAllLibros() {
     * return libroRepository.findAll().stream()
     * .map(this::convertToDTO)
     * .toList();
     * }
     */

    @Override
    public Optional<LibroDTO> getLibroById(String id) {
        return libroRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    public LibroDTO createLibro(LibroRequestDTO requestDTO) {
        Libro libro = new Libro();
        libro.setTitulo(requestDTO.getTitulo());
        libro.setAutor(requestDTO.getAutor());
        libro.setPrecio(requestDTO.getPrecio());
        libro.setStock(requestDTO.getStock());
        libro.setDisponible(requestDTO.getStock() > 0);
        libro.setGenero(requestDTO.getGenero());
        libro.setImageUrl(requestDTO.getImageUrl());
        libro.setDescription(requestDTO.getDescription());

        Libro saved = libroRepository.save(libro);
        return convertToDTO(saved);
    }

    @Override
    public LibroDTO updateLibro(String id, LibroRequestDTO requestDTO) {
        Libro libro = libroRepository.findById(id).orElseThrow();

        Optional.ofNullable(requestDTO.getTitulo()).ifPresent(libro::setTitulo);
        Optional.ofNullable(requestDTO.getAutor()).ifPresent(libro::setAutor);
        Optional.ofNullable(requestDTO.getGenero()).ifPresent(libro::setGenero);
        Optional.ofNullable(requestDTO.getDescription()).ifPresent(libro::setDescription);

        if (requestDTO.getPrecio() != null) {
            libro.setPrecio(requestDTO.getPrecio());
        }

        if (requestDTO.getStock() != null) {
            libro.setStock(requestDTO.getStock());
            libro.setDisponible((requestDTO.getStock() > 0));
        }

        if (requestDTO.getImageUrl() != null) {
            libro.setImageUrl(requestDTO.getImageUrl());
        }

        return convertToDTO(libroRepository.save(libro));

    }

    @Override
    public List<LibroDTO> getLibrosByTitulo(String titulo) {
        return libroRepository.findByTituloContainingIgnoreCase(titulo).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<LibroDTO> getLibroByGenero(String genero) {
        return libroRepository.findByGeneroIgnoreCase(genero).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    @Cacheable("categorias")
    public List<String> getCategorias() {
        return libroRepository.findAll().stream()
                .map(Libro::getGenero)
                .filter(genero -> genero != null && !genero.isEmpty())
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public void deleteLibro(String id) {
        libroRepository.deleteById(id);
    }

    private LibroDTO convertToDTO(Libro libro) {

        if (libro == null) {
            return null;

        }

        LibroDTO dto = new LibroDTO();

        dto.setId(libro.getId());
        dto.setTitulo(libro.getTitulo());
        dto.setAutor(libro.getAutor());
        dto.setGenero(libro.getGenero());
        dto.setPrecio(libro.getPrecio());
        dto.setStock(libro.getStock());
        dto.setDisponible(libro.getDisponible());
        dto.setImageUrl(libro.getImageUrl());
        dto.setDescription(libro.getDescription());

        return dto;
    }

    @Override
    public LibroDTO updateImagen(String id, String nuevaImagenUrl) {
        Libro libro = libroRepository.findById(id).orElseThrow();
        libro.setImageUrl(nuevaImagenUrl);
        return convertToDTO(libroRepository.save(libro));
    }

}