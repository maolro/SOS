package com.practica.objetos;

import org.springframework.hateoas.RepresentationModel;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "libros")
public class Libro extends RepresentationModel<Libro> {

    @Id
    @NotBlank(message = "El ISBN es obligatorio.")
    private String isbn;

    @NotBlank(message = "El título es obligatorio.")
    private String titulo;

    private String autor;

    private String edicion;

    private String editorial;

    @NotNull(message = "La cantidad de ejemplares disponibles es obligatoria.")
    private Integer disponibles;

    public Libro() {}

    public Libro(String titulo, String autor, String edicion, 
    String isbn, String editorial, Integer disponibles) {
        this.titulo = titulo;
        this.autor = autor;
        this.edicion = edicion;
        this.isbn = isbn;
        this.editorial = editorial;
        this.disponibles = disponibles;
    }

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

    public Integer getDisponibles() { return disponibles; }

    public void setDisponibles(Integer disponibles) { this.disponibles = disponibles; }
}
