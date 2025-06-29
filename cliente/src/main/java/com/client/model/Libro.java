package com.client.model;

public class Libro {
    
    private Long id;
    private String titulo;
    private String autor;
    private String edicion;
    private String isbn;
    private String editorial;
    private int disponibles;

    public Libro() {}

    public Libro(String titulo, String autor, String edicion, 
    String isbn, String editorial, int disponibles) {
        this.titulo = titulo;
        this.autor = autor;
        this.edicion = edicion;
        this.isbn = isbn;
        this.editorial = editorial;
        this.disponibles = disponibles;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }

    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutor() { return autor; }

    public void setAutor(String autor) { this.autor = autor; }

    public String getEdicion() { return edicion; }

    public void setEdicion(String edicion) { this.edicion = edicion; }

    public String getIsbn() { return isbn; }

    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getEditorial() { return editorial; }

    public void setEditorial(String editorial) { this.editorial = editorial; }

    public int getDisponibles() { return disponibles; }

    public void setDisponibles(int disponibles) { this.disponibles = disponibles; }

    @Override
    public String toString() {
        return "Libro: {" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", edicion='" + edicion + '\'' +
                ", isbn='" + isbn + '\'' +
                ", editorial='" + editorial + '\'' +
                ", disponibles='" + disponibles + '\'' +
                '}';
    }
}
