package com.practica.controladores;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.practica.assembler.EnsambladorLibro;
import com.practica.assembler.EnsambladorPrestamo;
import com.practica.assembler.EnsambladorUsuario;
import com.practica.objetos.*;
import com.practica.servicios.*;

import jakarta.validation.Valid;

import com.practica.repositorios.*;

//Paginación
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
//Hateoas
import org.springframework.hateoas.PagedModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;



@RestController
@RequestMapping("/api/v1/usuarios")
public class ControladorUsuario {
    private ServicioUsuario userService;
    private PagedResourcesAssembler<Usuario> pagedResourcesAssembler;
    private PagedResourcesAssembler<Libro> pagedResourcesAssemblerLibro;
    private PagedResourcesAssembler<Prestamo> pagedResourcesAssemblerPrestamo;
    private EnsambladorUsuario ensambladorUsuario;
    private EnsambladorLibro ensambladorLibro;
    private EnsambladorPrestamo ensambladorPrestamo;
    private RepositorioUsuario repositorioUsuario;
    private RepositorioLibro repositorioLibro;
    private RepositorioPrestamo repositorioPrestamo;

    public ControladorUsuario(ServicioUsuario userService, 
    RepositorioUsuario repositorioUsuario,
    PagedResourcesAssembler<Usuario> pagedResourcesAssembler,
    PagedResourcesAssembler<Libro> pagedResourcesAssemblerLibro,
    PagedResourcesAssembler<Prestamo> pagedResourcesAssemblerPrestamo,
    EnsambladorUsuario ensambladorUsuario,
    EnsambladorLibro ensambladorLibro,
    EnsambladorPrestamo ensambladorPrestamo,
    RepositorioPrestamo repositorioPrestamo,
    RepositorioLibro repositorioLibro) {

        this.userService = userService;
        this.repositorioUsuario = repositorioUsuario;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.pagedResourcesAssemblerLibro = pagedResourcesAssemblerLibro;
        this.pagedResourcesAssemblerPrestamo = pagedResourcesAssemblerPrestamo;
        this.ensambladorUsuario = ensambladorUsuario;
        this.ensambladorLibro = ensambladorLibro;
        this.ensambladorPrestamo = ensambladorPrestamo;
        this.repositorioPrestamo = repositorioPrestamo;
        this.repositorioLibro = repositorioLibro;
    }

    @GetMapping(value = "", produces = { "application/json", "application/xml" })
    public ResponseEntity<PagedModel<Usuario>> getUsuarios(
            @RequestParam(defaultValue = "", required = false) String starts_with,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size) {

        Page<Usuario> empleados = userService.buscarUsuarios(starts_with, page, size);

        return ResponseEntity.ok(pagedResourcesAssembler.toModel(empleados, ensambladorUsuario));
    }

    @GetMapping(value = "/{id}", produces = { "application/json", "application/xml", 
    "application/hal+json" })
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable Long id) {
        Usuario usuario = userService.obtenerUsuarioPorId(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el usuario"));
        usuario.add(linkTo(methodOn(ControladorUsuario.class).obtenerUsuarioPorId(id)).withSelfRel());
        return ResponseEntity.ok(usuario);
    }

    @PostMapping
    public ResponseEntity<?> crearUsuario(@Valid @RequestBody Usuario user, BindingResult result) {
         if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                errores.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errores);
        }
        user = repositorioUsuario.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        userService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario user) {
        try {
            Usuario actualizado = userService.actualizarUsuario(id, user);
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

    @GetMapping("/{id}/libros")
    public ResponseEntity<?> historicoLibrosUsuario(@PathVariable Long id,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size){
        
        Usuario usuario = userService.obtenerUsuarioPorId(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el usuario"));

        List<Prestamo> prestamos = repositorioPrestamo.findByUsuario(usuario);

        if (prestamos.isEmpty()) {
            return ResponseEntity.ok()
                .body(Map.of("mensaje", "No se encontraron préstamos para el usuario con ID " + id));
        }
        List<Libro> libros = prestamos.stream().map(Prestamo::getLibro)
        .distinct().toList();

        return ResponseEntity.ok().body(libros);
    }
    @GetMapping("/{id}/actividad")
    public ResponseEntity<?> actividadUsuario(@PathVariable Long id,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size){
        
        Usuario usuario = userService.obtenerUsuarioPorId(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el usuario"));

        List<Prestamo> prestamos = repositorioPrestamo.findTop5ByUsuarioOrderByFechaPrestamoDesc(usuario);

        if (prestamos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "No se encontraron préstamos para el usuario con ID " + id));
        }
        
        return ResponseEntity.ok().body(prestamos);
    }
}
