package com.practica.controladores;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.practica.assembler.*;
import com.practica.objetos.*;
import com.practica.servicios.*;

import jakarta.validation.Valid;

//Paginación
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
//Hateoas
import org.springframework.hateoas.PagedModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;



@RestController
@RequestMapping("/api/v1/usuarios")
public class ControladorUsuario {
    private PagedResourcesAssembler<Usuario> pagedResourcesAssembler;
    private PagedResourcesAssembler<Libro> pagedResourcesAssemblerLibro;
    private PagedResourcesAssembler<Prestamo> pagedResourcesAssemblerPrestamo;
    private EnsambladorUsuario ensambladorUsuario;
    private EnsambladorLibro ensambladorLibro;
    private EnsambladorPrestamo ensambladorPrestamo;
    private ServicioUsuario servicioUsuario;
    private ServicioPrestamo servicioPrestamo;

    public ControladorUsuario(PagedResourcesAssembler<Usuario> pagedResourcesAssembler,
    PagedResourcesAssembler<Libro> pagedResourcesAssemblerLibro,
    PagedResourcesAssembler<Prestamo> pagedResourcesAssemblerPrestamo,
    EnsambladorUsuario ensambladorUsuario,
    EnsambladorLibro ensambladorLibro,
    EnsambladorPrestamo ensambladorPrestamo,
    ServicioUsuario servicioUsuario, 
    ServicioPrestamo servicioPrestamo) {
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.pagedResourcesAssemblerLibro = pagedResourcesAssemblerLibro;
        this.pagedResourcesAssemblerPrestamo = pagedResourcesAssemblerPrestamo;
        this.ensambladorUsuario = ensambladorUsuario;
        this.ensambladorLibro = ensambladorLibro;
        this.ensambladorPrestamo = ensambladorPrestamo;
        this.servicioUsuario = servicioUsuario;
        this.servicioPrestamo = servicioPrestamo;
    }

    @GetMapping(value = "", produces = { "application/json", "application/xml" })
    public ResponseEntity<PagedModel<Usuario>> getUsuarios(
            @RequestParam(defaultValue = "", required = false) String starts_with,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "2", required = false) int size) {

        Page<Usuario> empleados = servicioUsuario.buscarUsuarios(starts_with, page, size);

        return ResponseEntity.ok(pagedResourcesAssembler.toModel(empleados, ensambladorUsuario));
    }

    @GetMapping(value = "/{id}", produces = { "application/json", "application/xml", 
    "application/hal+json" })
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable String id) {
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el usuario"));
        usuario.add(linkTo(methodOn(ControladorUsuario.class).obtenerUsuarioPorId(id)).withSelfRel());
        return ResponseEntity.ok(usuario);
    }

    @PostMapping
    public ResponseEntity<?> crearUsuario(@Valid @RequestBody Usuario user, 
    BindingResult result) {
        // Comprueba si hay errores en el JSON
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            errores.put("error", "formato invalido del JSON");
            result.getFieldErrors().forEach(error ->
                errores.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errores);
        }
        // Comprueba si hay valores repetidos
        if(servicioUsuario.obtenerUsuarioPorId(user.getMatricula()).isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", "Un usuario con esa matrícula ya existe"));
        }
        // Crea el usuario
        user = servicioUsuario.crearUsuario(user);
        return ResponseEntity.created(linkTo(methodOn(ControladorUsuario.class).obtenerUsuarioPorId(user.getMatricula())).toUri()).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable String id) {
        try{
            servicioUsuario.eliminarUsuario(id, servicioPrestamo);
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

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable String id, 
    @Valid @RequestBody Usuario user) {
        try {
            Usuario actualizado = servicioUsuario.actualizarUsuario(id, user);
            actualizado.add(linkTo(methodOn(ControladorUsuario.class)
                .obtenerUsuarioPorId(id)).withSelfRel());
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
    public ResponseEntity<?> historicoLibrosUsuario(
        @PathVariable String id,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "2") int size){
        
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el usuario"));

        Page<Libro> libros = servicioPrestamo.listaLibrosPrestadosUsuario(page, size, usuario);

        return ResponseEntity.ok(pagedResourcesAssemblerLibro.toModel(libros, ensambladorLibro));
    }

    @GetMapping("/{id}/actividad")
    public ResponseEntity<?> actividadUsuario(@PathVariable String id){
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el usuario"));
        
        Set<EntityModel<Prestamo>> prestamosActuales = new HashSet<>();
        Set<EntityModel<Prestamo>> historicoPrestamos = new HashSet<>();
        // Obtiene préstamos actuales
        for (Prestamo prestamo : servicioPrestamo.buscarPrestamosPorUsuario(usuario, true)) {
            prestamosActuales.add(EntityModel.of(
                prestamo,      
                linkTo(methodOn(ControladorUsuario.class).obtenerPrestamoPorId(id, prestamo.getId()))
 .              withSelfRel())
            );
        }
        // Obtiene histórico de 5 préstamos
        List<Prestamo> listaPrestamosHistoricos = servicioPrestamo
        .buscarPrestamosPorUsuario(usuario, false);
        for (int i=0; 
        i < Math.min(listaPrestamosHistoricos.size(), 5); i++) {
            // Obtiene y añade el préstamo actual si es historico
            Prestamo prestamo = listaPrestamosHistoricos.get(i);
            if(prestamo.getFechaDevolucionReal() != null){
                historicoPrestamos.add(EntityModel.of(
                prestamo,linkTo(methodOn(ControladorUsuario.class)
                    .obtenerPrestamoPorId(id, prestamo.getId())).withSelfRel()));
            }
        }
        // Crea un DTO de actividad
        ActividadPrestamoDTO dto = new ActividadPrestamoDTO();
        dto.setUsuario(EntityModel.of(
                usuario,      
                linkTo(methodOn(ControladorUsuario.class).obtenerUsuarioPorId(id))
 .              withSelfRel()));
        dto.setPrestamosActuales(prestamosActuales);
        dto.setHistoricoPrestamos(historicoPrestamos);
        // Envia el usuario
        return ResponseEntity.ok().body(dto);
    }
    // GESTIÓN DE PRÉSTAMOS DEL USUARIO

    // CREACIÓN DE PRÉSTAMOS (POST)
    @PostMapping("/{id}/prestamos")
    public ResponseEntity<?> crearPrestamo(@PathVariable String id,
        @Valid @RequestBody DatoPrestamo usuarioPrestamo, BindingResult result) {
        // Comprueba si el objeto usuarioPrestamo es válido
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                errores.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errores);
        }
        // Se crea y se guarda un objeto préstmao
        try{
            Prestamo prestamo = servicioPrestamo.crearPrestamo(usuarioPrestamo, id);
            // Envío de resultado exitoso
            return ResponseEntity.created(linkTo(methodOn(ControladorUsuario.class)
            .obtenerPrestamoPorId(id, prestamo.getId())).toUri())
            .build();
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } 
        catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));
        } 
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } 
        catch (IllegalCallerException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        } 
       
    }

    // OBTENCIÓN DE PRÉSTAMOS ACTUALES CON FILTRO DE FECHAS
    @GetMapping(value = "/{id}/prestamos", produces = { "application/json", "application/xml" })
    public ResponseEntity<?> obtenerPrestamos(
            @PathVariable String id,
            @RequestParam(required = false) String fechaInicio,
            @RequestParam(required = false) String fechaFin,
            @RequestParam(defaultValue = "true") Boolean actual,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        servicioUsuario.obtenerUsuarioPorId(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el usuario"));
        try{
            Page<Prestamo> prestamos = servicioPrestamo.buscarPrestamos(page, size, id, fechaInicio, fechaFin, actual);
            return ResponseEntity.ok(pagedResourcesAssemblerPrestamo.toModel(prestamos, ensambladorPrestamo));
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
        
    }

    // OBTENCIÓN DE UN PRÉSTAMO ESPECÍFICO
    @GetMapping(value = "/{usuarioId}/prestamos/{prestamoId}", 
    produces = { "application/json", "application/xml", "application/hal+json" })
    public ResponseEntity<?> obtenerPrestamoPorId(@PathVariable String usuarioId,
    @PathVariable Long prestamoId) {
        Prestamo prestamo = servicioPrestamo.obtenerPrestamoPorId(prestamoId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
            "No se ha encontrado el préstamo"));
        
        // Comprueba si el usuario es válido
        if (!prestamo.getUsuario().getMatricula().equals(usuarioId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("El préstamo no pertenece al usuario.");
        }
        
        // Devuelve el resultado correcto
        prestamo.add(linkTo(methodOn(ControladorUsuario.class)
                .obtenerPrestamoPorId(usuarioId, prestamoId)).withSelfRel());
        return ResponseEntity.ok(prestamo);
    }

    // ELIMINACIÓN DE UN PRÉSTAMO
    @DeleteMapping("/{usuarioId}/prestamos/{prestamoId}")
    public ResponseEntity<?> eliminarPrestamo(@PathVariable String usuarioId,
    @PathVariable Long prestamoId) {
        Prestamo prestamo = servicioPrestamo.obtenerPrestamoPorId(prestamoId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
            "No se ha encontrado el préstamo"));

        // Comprueba si el usuario es válido
        if (!prestamo.getUsuario().getMatricula().equals(usuarioId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("El préstamo no pertenece al usuario.");
        }
        try{
            servicioPrestamo.eliminarPrestamo(prestamoId);
            return ResponseEntity.noContent().build();
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(Map.of("error", e.getMessage()));
        }
        
    }

    // AMPLIACIÓN DE UN PRÉSTAMO
    @PutMapping("/{usuarioId}/prestamos/{prestamoId}")
    public ResponseEntity<?> manejarAccionPrestamo(
            @PathVariable String usuarioId,
            @PathVariable Long prestamoId,
            @RequestBody PrestamoAccionDTO accion) {

        try {
            if(accion.getOperacion().equals("ampliar")) {
                Prestamo prestamo = servicioPrestamo.ampliarPrestamo(prestamoId, usuarioId);
                prestamo.add(linkTo(methodOn(ControladorUsuario.class)
                .obtenerPrestamoPorId(usuarioId, prestamoId)).withSelfRel());
                return ResponseEntity.ok(prestamo);
            }
            else if(accion.getOperacion().equals("devolver")){
                if (accion.getFechaDevolucion() == null) {
                    return ResponseEntity.badRequest().body(Map.of("error", "Datos de devolución faltantes"));
                }
                String mensaje = servicioPrestamo.devolverPrestamo(prestamoId, accion.getFechaDevolucion());
                return ResponseEntity.ok(Map.of("resultado", mensaje));
            }
            else{
                return ResponseEntity.badRequest().body(Map.of("error", "Tipo de acción desconocido"));
            }             
        } 
        catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } 
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        }
    }
}
