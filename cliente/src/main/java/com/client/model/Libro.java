package com.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Libro {

    private Long id;
    private String titulo;
    private String autor;
    private String edicion;
    private String isbn;
    private String editorial;
    private int disponibles;
    @JsonProperty("_links")
    private ResourceLink _links;

    public Libro() {
    }

    public Libro(String titulo, String autor, String edicion,
            String isbn, String editorial, int disponibles) {
        this.titulo = titulo;
        this.autor = autor;
        this.edicion = edicion;
        this.isbn = isbn;
        this.editorial = editorial;
        this.disponibles = disponibles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public int getDisponibles() {
        return disponibles;
    }

    public void setDisponibles(int disponibles) {
        this.disponibles = disponibles;
    }

    public ResourceLink get_links() {
        return _links;
    }

    public void set_links(ResourceLink links) {
        this._links = links;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Libro: {")
                .append("id=").append(id)
                .append(", titulo='").append(titulo).append('\'')
                .append(", autor='").append(autor).append('\'')
                .append(", edicion='").append(edicion).append('\'')
                .append(", isbn='").append(isbn).append('\'')
                .append(", editorial='").append(editorial).append('\'')
                .append(", disponibles='").append(disponibles).append('\'');

        if (_links != null && _links.getSelf() != null && _links.getSelf().getHref() != null) {
            sb.append(", enlace='").append(_links.getSelf().getHref()).append('\'');
        }

        sb.append('}');
        return sb.toString();
    }
}
