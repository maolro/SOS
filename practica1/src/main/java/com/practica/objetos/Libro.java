package com.practica.objetos;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "libros")
public class Libro extends RepresentationModel<Libro> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El título es obligatorio.")
    private String titulo;

    @NotNull(message = "El autor es obligatorio.")
    private String autor;

    private String edicion;

    @NotNull(message = "El ISBN es obligatorio.")
    private String isbn;

    private String editorial;

    private boolean disponible = true;

    public Libro() {}

    public Libro(String titulo, String autor, String edicion, 
    String isbn, String editorial, boolean disponible) {
        this.titulo = titulo;
        this.autor = autor;
        this.edicion = edicion;
        this.isbn = isbn;
        this.editorial = editorial;
        this.disponible = disponible;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }

    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutor() { return autor; }

    public void setAutor(String autor) { this.autor = autor; }

    public String getEdicion() { return edicion; }

    public void setEdicion(String edicion) { this.edicion = edicion; }

    public String getISBN() { return isbn; }

    public void setISBN(String isbn) { this.isbn = isbn; }

    public String getEditorial() { return editorial; }

    public void setEditorial(String editorial) { this.editorial = editorial; }

    public boolean isDisponible() { return disponible; }

    public void setDisponible(boolean disponible) { this.disponible = disponible; }
}
