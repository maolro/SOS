package com.practica.repositorios;

import com.practica.objetos.Usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface RepositorioUsuario extends JpaRepository<Usuario, Long> {
    boolean existsByNombreUsuario(String nombre);

    Page<Usuario> findByNombreUsuarioStartsWith(@Param("starts_with") String startsWith, Pageable pageable);

}