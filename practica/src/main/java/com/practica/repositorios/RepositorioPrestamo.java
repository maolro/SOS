package com.practica.repositorios;

import java.util.List;

import com.practica.objetos.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioPrestamo extends JpaRepository<Prestamo, Long> {

    List<Prestamo> findByUsuarioIdAndFechaDevolucionRealIsNull(Long usuarioId);
}
