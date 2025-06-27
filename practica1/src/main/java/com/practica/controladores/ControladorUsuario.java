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
    private PagedResourcesAssembler<Usuario> pagedResourcesAssembler;
    private PagedResourcesAssembler<Libro> pagedResourcesAssemblerLibro;
    private PagedResourcesAssembler<Prestamo> pagedResourcesAssemblerPrestamo;
    private EnsambladorUsuario ensambladorUsuario;
    private EnsambladorLibro ensambladorLibro;
    private EnsambladorPrestamo ensambladorPrestamo;
    private ServicioUsuario servicioUsuario;
    private ServicioLibro servicioLibro;
    private ServicioPrestamo servicioPrestamo;

    public ControladorUsuario(PagedResourcesAssembler<Usuario> pagedResourcesAssembler,
    PagedResourcesAssembler<Libro> pagedResourcesAssemblerLibro,
    PagedResourcesAssembler<Prestamo> pagedResourcesAssemblerPrestamo,
    EnsambladorUsuario ensambladorUsuario,
    EnsambladorLibro ensambladorLibro,
    EnsambladorPrestamo ensambladorPrestamo,
    ServicioUsuario servicioUsuario, 
    ServicioLibro servicioLibro,
    ServicioPrestamo servicioPrestamo) {
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.pagedResourcesAssemblerLibro = pagedResourcesAssemblerLibro;
        this.pagedResourcesAssemblerPrestamo = pagedResourcesAssemblerPrestamo;
        this.ensambladorUsuario = ensambladorUsuario;
        this.ensambladorLibro = ensambladorLibro;
        this.ensambladorPrestamo = ensambladorPrestamo;
        this.servicioUsuario = servicioUsuario;
        this.servicioLibro = servicioLibro;
        this.servicioPrestamo = servicioPrestamo;
    }

    @GetMapping(value = "", produces = { "application/json", "application/xml" })
    public ResponseEntity<PagedModel<Usuario>> getUsuarios(
            @RequestParam(defaultValue = "", required = false) String starts_with,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size) {

        Page<Usuario> empleados = servicioUsuario.buscarUsuarios(starts_with, page, size);

        return ResponseEntity.ok(pagedResourcesAssembler.toModel(empleados, ensambladorUsuario));
    }

    @GetMapping(value = "/{id}", produces = { "application/json", "application/xml", 
    "application/hal+json" })
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable Long id) {
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el usuario"));
        usuario.add(linkTo(methodOn(ControladorUsuario.class).obtenerUsuarioPorId(id)).withSelfRel());
        return ResponseEntity.ok(usuario);
    }

    @PostMapping
    public ResponseEntity<?> crearUsuario(@Valid @RequestBody Usuario user, 
    BindingResult result) {
        // Comprueba si hay errores
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                errores.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errores);
        }
        user = servicioUsuario.crearUsuario(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        servicioUsuario.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long id, 
    @Valid @RequestBody Usuario user) {
        try {
            Usuario actualizado = servicioUsuario.actualizarUsuario(id, user);
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
    /*
    @GetMapping("/{id}/libros")
    public ResponseEntity<?> historicoLibrosUsuario(@PathVariable Long id,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size){
        
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(id)
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
    */

    /*
    @GetMapping("/{id}/actividad")
    public ResponseEntity<?> actividadUsuario(@PathVariable Long id,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size){
        
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el usuario"));

        List<Prestamo> prestamos = repositorioPrestamo.findTop5ByUsuarioOrderByFechaPrestamoDesc(usuario);

        if (prestamos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "No se encontraron préstamos para el usuario con ID " + id));
        }
        
        return ResponseEntity.ok().body(prestamos);
    }
    */

    // GESTIÓN DE PRÉSTAMOS DEL USUARIO

    // CREACIÓN DE PRÉSTAMOS (POST)
    @PostMapping("/{id}/prestamos")
    public ResponseEntity<?> crearPrestamo(@PathVariable Integer id,
        @Valid @RequestBody UsuarioPrestamo usuarioPrestamo, BindingResult result) {
        // Comprueba si el objeto usuarioPrestamo es válido
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                errores.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errores);
        }
        // Comprueba si las claves foráneas existen
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
            "No se ha encontrado el usuario"));
        Libro libro = servicioLibro.obtenerLibroPorId(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
            "El libro indicado no existe"));
        
        // Se crea y se guarda un objeto préstmao
        Prestamo prestamo = servicioPrestamo.crearPrestamo(usuarioPrestamo, usuario, libro);
        
        // Envío de resultado exitoso
        return ResponseEntity.status(HttpStatus.CREATED).body(prestamo);
    }

    // OBTENCIÓN DE PRÉSTAMOS ACTUALES CON FILTRO DE FECHAS
    @GetMapping(value = "/{id}/prestamos", produces = { "application/json", "application/xml" })
    public ResponseEntity<PagedModel<Prestamo>> obtenerPrestamos(
            @RequestParam(required = false) String fechaInicio,
            @RequestParam(required = false) String fechaFin,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Prestamo> prestamos = prestamoService.buscarPrestamos(page, size, id, fechaInicio, fechaFin);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(prestamos, ensambladorPrestamo));
    }

    // OBTENCIÓN DE UN PRÉSTAMO ESPECÍFICO
    @GetMapping(value = "/{usuarioId}/prestamos/{prestamoId}", 
    produces = { "application/json", "application/xml", "application/hal+json" })
    public ResponseEntity<Prestamo> obtenerPrestamoPorId(@PathVariable Long usuarioId,
    @PathVariable Long prestamoId) {
        Prestamo prestamo = prestamoService.obtenerPrestamoPorId(prestamo_id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
            "No se ha encontrado el préstamo"));
        
        // Comprueba si el usuario es válido
        if (!prestamo.getUsuario().getId().equals(usuarioId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("El préstamo no pertenece al usuario.");
        }
        
        // Devuelve el resultado correcto
        prestamo.add(linkTo(methodOn(ControladorPrestamo.class)
                .obtenerPrestamoPorId(usuario_id, prestamo_id)).withSelfRel());
        return ResponseEntity.ok(prestamo);
    }

    // ELIMINACIÓN DE UN PRÉSTAMO
    @DeleteMapping("/{usuarioId}/prestamos/{prestamoId}")
    public ResponseEntity<?> eliminarPrestamo(@PathVariable Long usuarioId,
    @PathVariable Long prestamoId) {
        Prestamo prestamo = prestamoService.obtenerPrestamoPorId(prestamo_id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
            "No se ha encontrado el préstamo"));

        // Comprueba si el usuario es válido
        if (!prestamo.getUsuario().getId().equals(usuarioId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("El préstamo no pertenece al usuario.");
        }

        prestamoService.eliminarPrestamo(prestamo);
        return ResponseEntity.noContent().build();
    }

    // AMPLIACIÓN O DEVOLUCIÓN DE UN PRÉSTAMO
    @PutMapping("/{usuarioId}/prestamos/{prestamoId}")
    public ResponseEntity<?> actualizarPrestamo(@PathVariable Long usuarioId,
    @PathVariable Long prestamoId, @Valid @RequestBody Prestamo prestamo) {
        Prestamo prestamo = prestamoService.obtenerPrestamoPorId(prestamo_id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
            "No se ha encontrado el préstamo"));

        // Comprueba si el usuario es válido
        if (!prestamo.getUsuario().getId().equals(usuarioId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("El préstamo no pertenece al usuario.");
        }

        prestamoService.eliminarPrestamo(prestamo);
        return ResponseEntity.noContent().build();
    }
    /*
    @PostMapping("/{id}/devolucion")
    public ResponseEntity<?> devolverPrestamo(@PathVariable Long id){
        Date fechaActual = new Date();
        Prestamo prestamo = prestamoService.obtenerPrestamoPorId(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el préstamo"));
        prestamo.setFechaDevolucionReal(fechaActual);
        if(fechaActual.after(prestamo.getFechaDevolucionPrevista())){
            prestamo.setSancion("El usuario está sancionado");
        }
        prestamoService.actualizarPrestamo(id, prestamo);
        return ResponseEntity.ok().body(prestamo);
    }
    // Función que gestiona la ampliación del préstamo
    @PutMapping("/{id}/ampliacion")
    public ResponseEntity<?> ampliarPrestamo(@PathVariable Long id, @RequestParam(required = true) Integer dias){
        // Se obtiene el préstamo con el id indicado
        Prestamo prestamo = prestamoService.obtenerPrestamoPorId(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el préstamo"));
        // Si se amplía después de la fecha de devolución entonces no se aplicará y devuelve Bad Request
        Date fechaActual = new Date();
        if(fechaActual.after(prestamo.getFechaDevolucionPrevista())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("No es posible ampliar un préstamo después de la fecha de devolución.");
        }
        // Se suma el parámetro días a la fecha de devolución prevista
        Calendar cal = Calendar.getInstance();
        cal.setTime(prestamo.getFechaDevolucionPrevista());
        cal.add(Calendar.DAY_OF_MONTH, dias);
        // Se actualiza el préstamo a la nueva fecha ampliada y se indica que se ha ampliado
        prestamo.setFechaDevolucionPrevista(cal.getTime());
        prestamo.setAmpliado(true);
        prestamoService.actualizarPrestamo(id, prestamo);
        return ResponseEntity.ok().body(prestamo);
    }
    */
}
