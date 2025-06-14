package com.porfolio.books_microservice.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.porfolio.books_microservice.dto.LibroDTO;
import com.porfolio.books_microservice.dto.LibroRequestDTO;
import com.porfolio.books_microservice.service.LibroService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

    private final LibroService libroService;
    private final Cloudinary cloudinary;

    public LibroController(LibroService libroService, Cloudinary cloudinary) {
        this.libroService = libroService;
        this.cloudinary = cloudinary;

    }

    @GetMapping
    public List<LibroDTO> getAllLibros() {
        try {
            return libroService.getAllLibros();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibroDTO> getLibroById(@PathVariable String id) {
        return libroService.getLibroById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public LibroDTO createLibro(@Valid @RequestBody LibroRequestDTO requestDTO) {
        return libroService.createLibro(requestDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LibroDTO> updateLibro(@PathVariable String id,
            @Valid @RequestBody LibroRequestDTO requestDTO) {
        return ResponseEntity.ok(libroService.updateLibro(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLibro(@PathVariable String id) {
        libroService.deleteLibro(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/findByName")
    public List<LibroDTO> getLibrosByTitulo(
            @RequestParam String titulo) {
        return libroService.getLibrosByTitulo(titulo);
    }

    @GetMapping("/findByGenero")
    public List<LibroDTO> getLibrosByGenre(@RequestParam String genero) {
        return libroService.getLibroByGenero(genero);
    }

    @GetMapping("/categorias")
    public List<String> getCategorias() {
        return libroService.getCategorias();
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("No se ha seleccionado ningún archivo.");
        }

        try {

            @SuppressWarnings("rawtypes")
            Map result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) result.get("secure_url");

            return ResponseEntity.ok(imageUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al subir la imagen");
        }
    }

    @PatchMapping("/{id}/imagen")
    public ResponseEntity<LibroDTO> updateBookImage(
            @PathVariable String id,
            @RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        try {

            @SuppressWarnings("rawtypes")
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) uploadResult.get("secure_url");

            LibroDTO updated = libroService.updateImagen(id, imageUrl);

            return ResponseEntity.ok(updated);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/image/{publicId}")
    @SuppressWarnings("rawtypes")
    public ResponseEntity<Map> getImageDetails(@PathVariable String publicId) {
        try {
            Map result = cloudinary.api().resource(publicId, ObjectUtils.emptyMap());

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

}