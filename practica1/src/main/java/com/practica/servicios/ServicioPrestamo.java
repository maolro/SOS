package com.practica.servicios;

import java.time.LocalDate;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.practica.objetos.*;
import com.practica.repositorios.*;


@Service
public class ServicioPrestamo {

    private final RepositorioPrestamo repositorio;
    private final RepositorioUsuario repositorioUsuario;
    private final RepositorioLibro repositorioLibro;

    @Autowired
    public ServicioPrestamo(RepositorioPrestamo repositorio, RepositorioUsuario repositorioUsuario,
    RepositorioLibro repositorioLibro) {
        this.repositorio = repositorio;
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioLibro = repositorioLibro;
    }

    public List<Prestamo> obtenerTodos() {
        return repositorio.findAll();
    }

    public Optional<Prestamo> obtenerPrestamoPorId(Long prestamoId) {
        return repositorio.findById(prestamoId);
    }

    public Prestamo crearPrestamo(UsuarioPrestamo upId, Usuario usuario, Libro libro) {
        Prestamo prestamo = new Prestamo();

        // Se asignan las claves y valores del préstamo
        prestamo.setUsuario(usuario);
        prestamo.setLibro(libro);
        prestamo.setFechaPrestamo(upId.getFechaPrestamo());
        
        // Calcula la fecha de devolución prevista
        Calendar cal = Calendar.getInstance();
        cal.setTime(prestamo.getFechaPrestamo());
        cal.add(Calendar.DAY_OF_MONTH, 14);
        prestamo.setFechaDevolucionPrevista(cal.getTime());

        // Se guarda el préstamo en el repositorio
        return repositorio.save(prestamo);
    }

    public Prestamo actualizarPrestamo(Long id, Prestamo actualizado) {
        Prestamo base = repositorio.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Préstamo no encontrado"));

        base.setFechaPrestamo(actualizado.getFechaPrestamo());
        base.setFechaDevolucionPrevista(actualizado.getFechaDevolucionPrevista());
        base.setFechaDevolucionReal(actualizado.getFechaDevolucionReal());
        base.setAmpliado(actualizado.isAmpliado());
        base.setSancion(actualizado.getSancion());
        base.setLibro(actualizado.getLibro());
        base.setUsuario(actualizado.getUsuario());

        return repositorio.save(base);
    }

    public void eliminarPrestamo(Prestamo prestamo) {
        repositorio.delete(prestamo);
    }

    public Page<Prestamo> buscarPrestamos(int page, int size, Long usuarioId, String fechaInicioStr, String fechaFinStr) {
        Pageable pageable = PageRequest.of(page, size);

        LocalDate fechaInicio = null;
        LocalDate fechaFin = null;

        try {
            if (fechaInicioStr != null) {
                fechaInicio = LocalDate.parse(fechaInicioStr);
            }
            if (fechaFinStr != null) {
                fechaFin = LocalDate.parse(fechaFinStr);
            }
        } 
        catch (Exception e) {
            throw new IllegalArgumentException("Formato de fecha inválido. Usa yyyy-MM-dd.");
        }

        return repositorio.prestamosActualesPorFecha(usuarioId, fechaInicio, fechaFin, pageable);
    }

    public List<Prestamo> buscarPrestamosActivosPorUsuario(Long usuarioId) {
        return repositorio.findByUsuarioIdAndFechaDevolucionRealIsNull(usuarioId);
    }
}
