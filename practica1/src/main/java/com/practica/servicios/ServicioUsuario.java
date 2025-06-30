package com.practica.servicios;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.practica.objetos.*;
import com.practica.repositorios.RepositorioUsuario;

@Service
public class ServicioUsuario {
    private final RepositorioUsuario repositorio;

    public ServicioUsuario(RepositorioUsuario repositorio) {
        this.repositorio = repositorio;
    }

    public List<Usuario> obtenerTodos() {
        return repositorio.findAll();
    }

    public Optional<Usuario> obtenerUsuarioPorId(String id) {
        return repositorio.findById(id);
    }

    public Usuario crearUsuario(Usuario usuario) {
        return repositorio.save(usuario);
    }

    public Usuario actualizarUsuario(String id, Usuario usuarioActualizado) {
        // Busca el usuario en el repositorio
        Usuario usuarioBase = repositorio.findById(id)
        .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));
        // Comprueba si se ha intentado cambiar la matrícula
        if (usuarioActualizado.getMatricula() != null &&
            !usuarioActualizado.getMatricula().equals(id)) {
            throw new IllegalArgumentException("La matrícula no se puede modificar.");
        }
        // Actualiza los campos
        usuarioBase.setNombreUsuario(usuarioActualizado.getNombreUsuario());
        usuarioBase.setCorreoElectronico(usuarioActualizado.getCorreoElectronico());
        usuarioBase.setFechaNacimiento(usuarioActualizado.getFechaNacimiento());
        // Guarda en el repositorio y devuelve el usuario modificado
        return repositorio.save(usuarioBase);
    }

    public void eliminarUsuario(String id, ServicioPrestamo servicioPrestamo) {
        // Comprueba si el usuario existe
        Usuario usuario = obtenerUsuarioPorId(id)
            .orElseThrow(() -> new NullPointerException(
                "No se ha encontrado el usuario"));
        // Comprueba si el usuario ha hecho algun prestamo
        if(servicioPrestamo.buscarPrestamosPorUsuario(usuario, true).size() > 0){
            throw new IllegalArgumentException(
                "No se puede borrar un usuario con prestamos activos");
        }
        // Elimina el usuario
        repositorio.deleteById(id);
    }

     public Page<Usuario> buscarUsuarios(String starts_with, int page, int size) {
        // Crear el objeto Pageable usando el número de página, el tamaño y el campo por
        // el que se ordena (name,desc)
        Pageable paginable = PageRequest.of(page, size);
        if (starts_with != null) {
            return repositorio.findByNombreUsuarioStartsWith(starts_with, paginable);
        } 
        else {
            return repositorio.findAll(paginable);
        }
    }
}