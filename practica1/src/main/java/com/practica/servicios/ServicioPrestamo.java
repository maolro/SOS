package com.practica.servicios;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.practica.objetos.*;
import com.practica.repositorios.*;

@Service
public class ServicioPrestamo {

    private final RepositorioPrestamo repositorio;
    private final ServicioUsuario servicioUsuario;
    private final ServicioLibro servicioLibro;

    @Autowired
    public ServicioPrestamo(RepositorioPrestamo repositorio,
            ServicioUsuario servicioUsuario, ServicioLibro servicioLibro) {
        this.repositorio = repositorio;
        this.servicioUsuario = servicioUsuario;
        this.servicioLibro = servicioLibro;
    }

    public List<Prestamo> obtenerTodos() {
        return repositorio.findAll();
    }

    public Optional<Prestamo> obtenerPrestamoPorId(Long prestamoId) {
        return repositorio.findById(prestamoId);
    }

    public Prestamo crearPrestamo(DatoPrestamo upId, String usuarioId) {
        // Obtiene el usuario
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(usuarioId)
                .orElseThrow(() -> new NoSuchElementException("El usuario no existe"));
        // Obtiene el libro
        Libro libro = servicioLibro.obtenerLibroPorId(upId.getLibro_id())
                .orElseThrow(() -> new NoSuchElementException("El libro no existe"));
        // Comprueba si el usuario esta sancionado
        if (usuario.getSancionado()) {
            throw new IllegalCallerException("El usuario está sancionado");
        }
        // Comprueba si hay copias disponibles del libro
        if (libro.getDisponibles() <= 0) {
            throw new NullPointerException("No quedan ejemplares disponibles");
        }
        // Comprueba si el usuario tiene ya prestado el libro
        if (buscarPrestamosPorLibro(libro, true).size() > 0) {
            throw new NullPointerException("El usuario ya tiene prestado el libro");
        }

        // Se asignan las claves y valores del préstamo
        Prestamo prestamo = new Prestamo();
        prestamo.setUsuario(usuario);
        prestamo.setLibro(libro);

        // Fija la fecha del prestamo (hoy en caso que falte)
        if (upId.getFechaPrestamo() == null) {
            prestamo.setFechaPrestamo(new Date());
        } else {
            prestamo.setFechaPrestamo(upId.getFechaPrestamo());
        }

        // Calcula la fecha de devolución prevista
        Calendar cal = Calendar.getInstance();
        cal.setTime(prestamo.getFechaPrestamo());
        cal.add(Calendar.DAY_OF_MONTH, 14);
        prestamo.setFechaDevolucionPrevista(cal.getTime());

        // Comprueba si las fechas son validas
        if (upId.getFechaDevolucion() != null
                && (upId.getFechaDevolucion().before(prestamo.getFechaPrestamo())
                        || (upId.getFechaPrestamo() == null))) {
            throw new IllegalArgumentException(
                    "La fecha de devolución real no puede ser anterior a la fecha del préstamo o en el futuro");
        }

        prestamo.setFechaDevolucionReal(upId.getFechaDevolucion());
        // Se actualizan los prestamos si no es historico
        if (upId.getFechaDevolucion() == null) {
            // Actualiza la cantidad de libros disponibles
            libro.setDisponibles(libro.getDisponibles() - 1);
        }
        // Se guarda el préstamo en el repositorio
        return repositorio.save(prestamo);
    }

    public Prestamo actualizarPrestamo(Long id, Prestamo actualizado) {
        Prestamo base = repositorio.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Préstamo no encontrado"));

        base.setFechaPrestamo(actualizado.getFechaPrestamo());
        base.setFechaDevolucionPrevista(actualizado.getFechaDevolucionPrevista());
        base.setFechaDevolucionReal(actualizado.getFechaDevolucionReal());
        base.setAmpliado(actualizado.getAmpliado());
        base.setLibro(actualizado.getLibro());
        base.setUsuario(actualizado.getUsuario());

        return repositorio.save(base);
    }

    public void eliminarPrestamo(Long id) {
        repositorio.deleteById(id);
    }

    public Page<Prestamo> buscarPrestamos(int page, int size, String usuarioId,
            String fechaInicioStr, String fechaFinStr, Boolean actual) {

        Pageable pageable = PageRequest.of(page, size);

        java.sql.Date fechaInicio;
        java.sql.Date fechaFin;

        try {
            LocalDate parsedInicio = (fechaInicioStr != null) ? LocalDate.parse(fechaInicioStr)
                    : LocalDate.of(1900, 1, 1);
            LocalDate parsedFin = (fechaFinStr != null) ? LocalDate.parse(fechaFinStr) : LocalDate.of(2100, 1, 1);

            fechaInicio = java.sql.Date.valueOf(parsedInicio);
            fechaFin = java.sql.Date.valueOf(parsedFin);
        } catch (Exception e) {
            throw new IllegalArgumentException("Formato de fecha inválido. Usa yyyy-MM-dd.");
        }

        if (actual == null) {
            actual = false;
        }

        return repositorio.listaPrestamos(usuarioId, fechaInicio, fechaFin, actual, pageable);
    }

    public List<Prestamo> buscarPrestamosPorLibro(Libro libro, boolean actual) {
        return repositorio.findByLibro(libro, actual);
    }

    public List<Prestamo> buscarPrestamosPorUsuario(Usuario usuario, boolean actual) {
        return repositorio.findByUsuario(usuario, actual);
    }

    public Page<Libro> listaLibrosPrestadosUsuario(int page,
            int size, Usuario usuario) {
        Pageable pageable = PageRequest.of(page, size);
        return repositorio.buscarLibrosPrestados(usuario, pageable);
    }

    public Prestamo ampliarPrestamo(Long id, String usuarioId) {
        Prestamo prestamo = obtenerPrestamoPorId(id)
                .orElseThrow(() -> new NoSuchElementException("Préstamo no encontrado"));
        // Comprueba si el path es corrrecto
        if (!prestamo.getUsuario().getMatricula().equals(usuarioId)) {
            throw new NoSuchElementException("El usuario indicado no tiene el prestamo");
        }
        // Comprueba si se ha devuelto
        if (prestamo.getFechaDevolucionReal() != null) {
            throw new IllegalArgumentException("No se puede ampliar un prestamo devuelto");
        }
        // Comprueba si el prestamo esta vencido
        if (prestamo.getFechaDevolucionPrevista().before(new Date())) {
            throw new IllegalArgumentException("No se puede ampliar un prestamo vencido");
        }
        // Comprueba si se ha ampliado ya
        if (prestamo.getAmpliado()) {
            throw new IllegalArgumentException("No se puede ampliar un prestamo ya ampliado");
        }
        prestamo.setAmpliado(true);
        // Ampliacion de la fecha devolucion prevista
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(prestamo.getFechaDevolucionPrevista());
        calendar.add(Calendar.WEEK_OF_YEAR, 2);
        prestamo.setFechaDevolucionPrevista(calendar.getTime());
        // Guarda el nuevo prestamo
        repositorio.save(prestamo);
        return prestamo;
    }

    public String devolverPrestamo(Long id, Date fechaDevolucion) {
        Prestamo prestamo = obtenerPrestamoPorId(id)
                .orElseThrow(() -> new NoSuchElementException("Préstamo no encontrado"));
        // Comprueba si se ha devuelto
        if (prestamo.getFechaDevolucionReal() != null) {
            throw new IllegalArgumentException("No se devolver un prestamo devuelto");
        }
        // Guarda el resultado actualizado
        prestamo.setFechaDevolucionReal(fechaDevolucion);
        repositorio.save(prestamo);
        // Aumenta la cantidad de libros disponibles y actualiza el libro
        Libro libro = prestamo.getLibro();
        libro.setDisponibles(libro.getDisponibles() + 1);
        servicioLibro.actualizarLibro(libro.getISBN(), libro);
        // Comprueba si el prestamo esta vencido y establece sancion
        if (prestamo.getFechaDevolucionReal().after(new Date())) {
            Usuario usuario = prestamo.getUsuario();
            usuario.setSancionado(true);
            servicioUsuario.actualizarUsuario(usuario.getMatricula(),
                    usuario);
            return new String("La devolución ha superado el plazo. Se aplicará sanción");
        }
        return new String("La devolución ha sido exitosa");
    }
}
