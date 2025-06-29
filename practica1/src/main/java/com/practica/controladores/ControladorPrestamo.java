package com.practica.controladores;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.practica.assembler.EnsambladorPrestamo;
import com.practica.objetos.*;
import com.practica.servicios.ServicioPrestamo;
import com.practica.repositorios.RepositorioPrestamo;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

@RestController
@RequestMapping("/api/v1/usuarios/{usuario_id}/prestamos")
public class ControladorPrestamo {

    private final ServicioPrestamo prestamoService;
    private final RepositorioPrestamo repositorioPrestamo;
    private final PagedResourcesAssembler<Prestamo> pagedResourcesAssembler;
    private final EnsambladorPrestamo ensambladorPrestamo;

    public ControladorPrestamo(
        ServicioPrestamo prestamoService,
        RepositorioPrestamo repositorioPrestamo,
        PagedResourcesAssembler<Prestamo> pagedResourcesAssembler,
        EnsambladorPrestamo ensambladorPrestamo) {

        this.prestamoService = prestamoService;
        this.repositorioPrestamo = repositorioPrestamo;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.ensambladorPrestamo = ensambladorPrestamo;
    }

    @GetMapping(value = "", produces = { "application/json", "application/xml" })
    public ResponseEntity<PagedModel<Prestamo>> obtenerPrestamos(
            @PathVariable Long usuario_id,
            @RequestParam(required = false) String fechaInicio,
            @RequestParam(required = false) String fechaFin,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size,
            @RequestParam(required = false) boolean actual) {

        Page<Prestamo> prestamos = prestamoService
            .buscarPrestamos(page, size, usuario_id, 
            fechaInicio, fechaFin, actual);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(prestamos, ensambladorPrestamo));
    }

    @GetMapping(value = "/{id}", produces = { "application/json", "application/xml", "application/hal+json" })
    public ResponseEntity<Prestamo> obtenerPrestamoPorId(@PathVariable Long id) {
        Prestamo prestamo = prestamoService.obtenerPrestamoPorId(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el préstamo"));
        prestamo.add(linkTo(methodOn(ControladorPrestamo.class).obtenerPrestamoPorId(id)).withSelfRel());
        return ResponseEntity.ok(prestamo);
    }

    @PostMapping
    public ResponseEntity<?> crearPrestamo(@Valid @RequestBody DatoPrestamo prestamoDTO, 
    BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                errores.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errores);
        }
        Prestamo prestamo = repositorioPrestamo
            .save(prestamoService.crearPrestamo(prestamoDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(prestamo);
    }
    // Esta función elimina el préstamo con el ID indicado
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPrestamo(@PathVariable Long id) {
        prestamoService.eliminarPrestamo(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
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
    @PutMapping("/{id}")
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
}
