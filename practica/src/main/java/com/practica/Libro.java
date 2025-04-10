package com.practica;

public class Libro {
    private int id;
    private String titulo;
    private String autor;
    private String edicion;
    private String ISBN;
    private String editorial;
    private boolean disponible;

    public Libro() { }

    public Libro(String titulo, String autor, String edicion, 
                String ISBN, String editorial, boolean disponible) {
        this.titulo = titulo;
        this.autor = autor;
        this.edicion = edicion;
        this.ISBN = ISBN;
        this.editorial = editorial;
        this.disponible = disponible;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEdicion() {
        return edicion;
    }

    public void setEdicion(String edicion) {
        this.edicion = edicion;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public boolean estaDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
}