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

    public Optional<Libro> obtenerLibroPorId(Long id) {
        return repositorio.findById(id);
    }

    public Libro crearLibro(Libro libro) {
        return repositorio.save(libro);
    }

    public Libro actualizarLibro(Long id, Libro libroActualizado) {
        Libro libroBase = repositorio.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Libro no encontrado"));

        if (!libroActualizado.getISBN().equals(libroActualizado.getISBN())) {
            throw new IllegalArgumentException("El ISBN no se puede modificar.");
        }

        libroBase.setTitulo(libroActualizado.getTitulo());
        libroBase.setAutor(libroActualizado.getAutor());
        libroBase.setEdicion(libroActualizado.getEdicion());
        libroBase.setEditorial(libroActualizado.getEditorial());
        libroBase.setDisponible(libroActualizado.isDisponible());

        return repositorio.save(libroBase);
    }

    public void eliminarLibro(Long id) {
        repositorio.deleteById(id);
    }

    public Page<Libro> buscarLibros(int page, int size, String titulo, Boolean disponible) {
        Pageable paginable = PageRequest.of(page, size);

        if (titulo != null && disponible != null) {
            return repositorio.findByTituloContainingIgnoreCaseAndDisponible(titulo, disponible, paginable);
        } else if (titulo != null) {
            return repositorio.findByTituloContainingIgnoreCase(titulo, paginable);
        } else if (disponible != null) {
            return repositorio.findByDisponible(disponible, paginable);
        } else {
            return repositorio.findAll(paginable);
        }
    }
}
