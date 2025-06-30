package com.practica.repositorios;

import java.time.LocalDate;
import java.util.List;

import com.practica.objetos.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RepositorioPrestamo extends JpaRepository<Prestamo, Long> {

    @Query("SELECT p FROM Prestamo p " +
       "WHERE p.usuario = :usuario " +
       "AND (:actual = false OR p.fechaDevolucionReal IS NULL)")
    List<Prestamo> findByUsuario(
            @Param("usuario") Usuario usuario,
            @Param("actual") boolean actual
    );

    @Query("SELECT p FROM Prestamo p " +
        "WHERE p.libro = :libro " +
        "AND (:actual = false OR p.fechaDevolucionReal IS NULL)")
    List<Prestamo> findByLibro(
            @Param("libro") Libro libro,
            @Param("actual") boolean actual
    );

    @Query("SELECT p FROM Prestamo p " +
           "WHERE (p.usuario.matricula = :usuarioId) " +
           "AND (:fechaInicio IS NULL OR p.fechaPrestamo >= :fechaInicio) " +
           "AND (:fechaFin IS NULL OR p.fechaPrestamo <= :fechaFin)" +
           "AND (:actual = false OR p.fechaDevolucionReal IS NULL)" +
           "ORDER BY p.fechaPrestamo DESC")
    Page<Prestamo> listaPrestamos(
            @Param("usuarioId") String usuarioId,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin,
            @Param("actual") Boolean actual,
            Pageable pageable
    );

    @Query("SELECT DISTINCT p.libro FROM Prestamo p "+
    "WHERE (p.usuario = :usuario)")
    Page<Libro> buscarLibrosPrestados(@Param("usuario") Usuario usuario,
    Pageable pageable);
}
