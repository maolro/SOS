package com.practica.controladores;

import org.springframework.web.bind.annotation.*;

import com.practica.assembler.EnsambladorUsuario;
import com.practica.objetos.*;
import com.practica.servicios.*;

import jakarta.validation.Valid;

import com.practica.repositorios.*;

//Paginación
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
//Hateoas
import org.springframework.hateoas.PagedModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.HashMap;
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
    private EnsambladorUsuario ensambladorUsuario;
    private RepositorioUsuario repositorioUsuario;

    public ControladorUsuario(ServicioUsuario userService, 
    RepositorioUsuario repositorioUsuario,
    PagedResourcesAssembler<Usuario> pagedResourcesAssembler,
    EnsambladorUsuario ensambladorUsuario) {
        this.userService = userService;
        this.repositorioUsuario = repositorioUsuario;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.ensambladorUsuario = ensambladorUsuario;
    }

    @GetMapping(value = "", produces = { "application/json", "application/xml" })
    public ResponseEntity<PagedModel<Usuario>> getEmpleados(
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
            .orElseThrow(() -> new RuntimeException("No se ha encontrado el usuario"));
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
    public void eliminarUsuario(@PathVariable Long id) {
        userService.eliminarUsuario(id);
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
}
