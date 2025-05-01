package com.practica.controladores;

import org.springframework.web.bind.annotation.*;

import com.practica.assembler.EnsambladorLibro;
import com.practica.objetos.Libro;
import com.practica.servicios.ServicioLibro;
import com.practica.repositorios.RepositorioLibro;

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

@RestController
@RequestMapping("/api/v1/libros")
public class ControladorLibro {

    private final ServicioLibro libroService;
    private final PagedResourcesAssembler<Libro> pagedResourcesAssembler;
    private final EnsambladorLibro ensambladorLibro;
    private final RepositorioLibro repositorioLibro;

    public ControladorLibro(
        ServicioLibro libroService,
        RepositorioLibro repositorioLibro,
        PagedResourcesAssembler<Libro> pagedResourcesAssembler,
        EnsambladorLibro ensambladorLibro) {

        this.libroService = libroService;
        this.repositorioLibro = repositorioLibro;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.ensambladorLibro = ensambladorLibro;
    }

    @GetMapping(value = "", produces = { "application/json", "application/xml" })
    public ResponseEntity<PagedModel<Libro>> obtenerLibros(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) Boolean disponible,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size) {

        Page<Libro> libros = libroService.buscarLibros(page, size, titulo, disponible);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(libros, ensambladorLibro));
    }

    @GetMapping(value = "/{id}", produces = { "application/json", "application/xml", "application/hal+json" })
    public ResponseEntity<Libro> obtenerLibroPorId(@PathVariable Long id) {
        Libro libro = libroService.obtenerLibroPorId(id)
            .orElseThrow(() -> new RuntimeException("No se ha encontrado el libro"));
        libro.add(linkTo(methodOn(ControladorLibro.class).obtenerLibroPorId(id)).withSelfRel());
        return ResponseEntity.ok(libro);
    }

    @PostMapping
    public ResponseEntity<?> crearLibro(@Valid @RequestBody Libro libro, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                errores.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errores);
        }
        libro = repositorioLibro.save(libro);
        return ResponseEntity.status(HttpStatus.CREATED).body(libro);
    }

    @DeleteMapping("/{id}")
    public void eliminarLibro(@PathVariable Long id) {
        libroService.eliminarLibro(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarLibro(@PathVariable Long id, @RequestBody Libro libro) {
        try {
            Libro actualizado = libroService.actualizarLibro(id, libro);
            return ResponseEntity.ok(actualizado);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }
}
