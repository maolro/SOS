package com.practica.controladores;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.practica.assembler.EnsambladorLibro;
import com.practica.objetos.Libro;
import com.practica.servicios.*;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

// El controlador gestiona la interfacción con clientes para la API Rest
@RestController
@RequestMapping("/api/v1/libros")
public class ControladorLibro {
    // Se requieren las funciones del Servicio, los repositorios y un ensamblador
    private final ServicioLibro libroService;
    private final PagedResourcesAssembler<Libro> pagedResourcesAssembler;
    private final EnsambladorLibro ensambladorLibro;
    private final ServicioPrestamo servicioPrestamo;

    public ControladorLibro(
        ServicioLibro libroService,
        PagedResourcesAssembler<Libro> pagedResourcesAssembler,
        EnsambladorLibro ensambladorLibro,
        ServicioPrestamo servicioPrestamo) {

        this.libroService = libroService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.ensambladorLibro = ensambladorLibro;
        this.servicioPrestamo = servicioPrestamo;
    }
    // Esta función devolverá todos los libros disponibles como un JSON con paginación y enlaces
    @GetMapping(value = "", produces = { "application/json", "application/xml" })
    public ResponseEntity<PagedModel<Libro>> obtenerLibros(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) Boolean disponible,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "2", required = false) int size) {
        
        // Se buscan todos los libros, los cuales se pueden filtrar según título y disponible
        Page<Libro> libros = libroService.buscarLibros(page, size, titulo, disponible);
        // Se devuelve el objeto paginado con enlaces usando el ensamblador
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(libros, ensambladorLibro));
    }
    // Esta función devolverá el recurso Libro con el id indicado
    @GetMapping(value = "/{id}", produces = { "application/json", "application/xml", "application/hal+json" })
    public ResponseEntity<Libro> obtenerLibroPorId(@PathVariable String id) {
        // Se busca el libro indicado. Si el id proporcionado no existe se devolverá una excepción
        Libro libro = libroService.obtenerLibroPorId(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
            "No se ha encontrado el libro"));
        
        // Se añade un enlace al libro devuelto
        libro.add(linkTo(methodOn(ControladorLibro.class).obtenerLibroPorId(id)).withSelfRel());
        return ResponseEntity.ok(libro);
    }

    @PostMapping
    public ResponseEntity<?> crearLibro(@Valid @RequestBody Libro libro, BindingResult result) {
        // Primero se intenta crear el objeto Libro. Si no cumple uno de los requisitos entonces se devolverá un error
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            // Para mayor claridad se especifican los campos exactos donde hay error
            result.getFieldErrors().forEach(error ->
                errores.put(error.getField(), error.getDefaultMessage())
            );
            // Se devuelve un bad request HTTP
            return ResponseEntity.badRequest().body(errores);
        }
        // Si se ha creado exitosamente entonces el libro se guarda en el repositorio
        try {
            libro = libroService.crearLibro(libro);
            // Se devuelve 201 CREATED y el enlace en la cabecera
            return ResponseEntity.created(linkTo(methodOn(ControladorLibro.class)
                .obtenerLibroPorId(libro.getISBN())).toUri()).build();
        } 
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }
    // Esta función elimina el libro con el ID indicado
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarLibro(@PathVariable String id) {
        try{
            libroService.eliminarLibro(id, servicioPrestamo);
            return ResponseEntity.noContent().build();
        }
        catch(NullPointerException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", e.getMessage()));
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", e.getMessage()));
        }
    }
    // Esta función actualiza los campos del libro con id proporcionado
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarLibro(@PathVariable String id, @RequestBody Libro libro) {
        try {
            Libro actualizado = libroService.actualizarLibro(id, libro);
            return ResponseEntity.ok(actualizado);
        } 
        catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", e.getMessage()));
        } 
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        } 
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error interno del servidor"));
        }
    }
}
