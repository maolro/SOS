package com.practica.repositorios;

import com.practica.objetos.Libro;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioLibro extends JpaRepository<Libro, Long> {
    
    boolean existsByIsbn(String isbn);

    Page<Libro> findByTituloContainingIgnoreCase(String titulo, Pageable pageable);

    Page<Libro> findByDisponible(Boolean disponible, Pageable pageable);

    Page<Libro> findByTituloContainingIgnoreCaseAndDisponible(String titulo, Boolean disponible, Pageable pageable);
}
