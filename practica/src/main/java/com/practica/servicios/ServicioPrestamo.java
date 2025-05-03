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

    public Optional<Prestamo> obtenerPrestamoPorId(Long id) {
        return repositorio.findById(id);
    }

    public Prestamo crearPrestamo(CrearPrestamoDTO prestamoDTO) {
        Prestamo prestamo = new Prestamo();
        //Actualizar los campos del préstamo a los ids proporcionados
        Usuario usuario = repositorioUsuario.findById(prestamoDTO.getUsuario_id())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        Libro libro = repositorioLibro.findById(prestamoDTO.getLibro_id())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro no encontrado"));
        // Si ambas claves foráneas existen entonces se asignan
        prestamo.setUsuario(usuario);
        prestamo.setLibro(libro);
        prestamo.setFechaPrestamo(prestamoDTO.getFechaPrestamo());
        // Calcula la fecha de devolución prevista si no se ha establecido
        if (prestamo.getFechaDevolucionPrevista() == null && prestamo.getFechaPrestamo() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(prestamo.getFechaPrestamo());
            cal.add(Calendar.DAY_OF_MONTH, 14);
            prestamo.setFechaDevolucionPrevista(cal.getTime());
        }
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

    public void eliminarPrestamo(Long id) {
        repositorio.deleteById(id);
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
        } catch (Exception e) {
            throw new IllegalArgumentException("Formato de fecha inválido. Usa yyyy-MM-dd.");
        }

        return repositorio.findByUsuarioIdAndFechaRange(usuarioId, fechaInicio, fechaFin, pageable);
    }

    public List<Prestamo> buscarPrestamosActivosPorUsuario(Long usuarioId) {
        return repositorio.findByUsuarioIdAndFechaDevolucionRealIsNull(usuarioId);
    }
}
