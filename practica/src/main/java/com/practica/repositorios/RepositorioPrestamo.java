package com.practica.repositorios;

import java.time.LocalDate;
import java.util.List;

import com.practica.objetos.Prestamo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RepositorioPrestamo extends JpaRepository<Prestamo, Long> {

    List<Prestamo> findByUsuarioIdAndFechaDevolucionRealIsNull(Long usuarioId);

    List<Prestamo> findByUsuarioId(Long usuarioId);

    @Query("SELECT p FROM Prestamo p " +
           "WHERE (:usuarioId IS NULL OR p.usuarioId = :usuarioId) " +
           "AND (:fechaInicio IS NULL OR p.fechaPrestamo >= :fechaInicio) " +
           "AND (:fechaFin IS NULL OR p.fechaPrestamo <= :fechaFin)")
    Page<Prestamo> findByUsuarioIdAndFechaRange(
            @Param("usuarioId") Long usuarioId,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin,
            Pageable pageable
    );
}
