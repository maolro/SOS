package com.practica.repositorios;

import com.practica.objetos.Libro;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RepositorioLibro extends JpaRepository<Libro, String> {
    
    boolean existsByIsbn(String isbn);

    @Query("SELECT l FROM Libro l " +
       "WHERE (:titulo IS NULL OR LOWER(l.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))) " +
       "AND (:disponible = false OR l.disponibles > 0)")
    Page<Libro> buscarLibros(
            @Param("titulo") String titulo,
            @Param("disponible") boolean disponible,
            Pageable pageable
    );
}
