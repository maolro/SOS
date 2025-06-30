package com.practica.servicios;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.practica.objetos.Libro;
import com.practica.repositorios.RepositorioLibro;

@Service
public class ServicioLibro {

    private final RepositorioLibro repositorio;

    public ServicioLibro(RepositorioLibro repositorio) {
        this.repositorio = repositorio;
    }

    public List<Libro> obtenerTodos() {
        return repositorio.findAll();
    }

    public Optional<Libro> obtenerLibroPorId(String id) {
        return repositorio.findById(id);
    }

    public Libro crearLibro(Libro libro) {
        // Comprueba is no hay ISBN repetido
        if(obtenerLibroPorId(libro.getISBN()).isPresent())
            throw new IllegalArgumentException("No pueden existir dos libros con ISBN repetido");
        // Comprueba si se añade la correcta cantidad de libros
        if(libro.getDisponibles() <= 0)
            throw new IllegalArgumentException("Debe añadirse por lo menos un ejemplar");
        // Guarda el libro
        return repositorio.save(libro);
    }

    public Libro actualizarLibro(String id, Libro libroActualizado) {
        Libro libroBase = repositorio.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Libro no encontrado"));

        if (!libroActualizado.getISBN().equals(id)) {
            throw new IllegalArgumentException("El ISBN no se puede modificar.");
        }

        libroBase.setTitulo(libroActualizado.getTitulo());
        libroBase.setAutor(libroActualizado.getAutor());
        libroBase.setEdicion(libroActualizado.getEdicion());
        libroBase.setEditorial(libroActualizado.getEditorial());
        libroBase.setDisponibles(libroActualizado.getDisponibles());

        return repositorio.save(libroBase);
    }

    public void eliminarLibro(String id, ServicioPrestamo servicioPrestamo) {
         // Comprueba si el libro existe
        Libro libro = obtenerLibroPorId(id)
            .orElseThrow(() -> new NullPointerException(
                "No se ha encontrado el libro"));
        // Comprueba si el libro esta prestado
        if(servicioPrestamo.buscarPrestamosPorLibro(libro, false).size() > 0){
            throw new IllegalArgumentException(
                "No se puede borrar un libro prestado");
        }
        // Elimina el libro
        repositorio.deleteById(id);
    }

    public Page<Libro> buscarLibros(int page, int size, String titulo, Boolean disponible) {
        Pageable paginable = PageRequest.of(page, size);
        // Comprueba si existe el parametro de busqueda
        if(disponible == null)
            return repositorio.buscarLibros(titulo, false, paginable);
        // Obtiene los elementos del repositorio
        return repositorio.buscarLibros(titulo, disponible, paginable);
    }


}
