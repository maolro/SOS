package com.client.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Prestamo {

    private Long id;
    private Usuario usuario;
    private Libro libro;
    private Date fechaPrestamo;
    private Date fechaDevolucionPrevista;
    private Date fechaDevolucionReal;
    private boolean ampliado;
    private String sancion;
    @JsonProperty("_links")
    private ResourceLink _links;

    public Prestamo() {
    }

    public Prestamo(Usuario usuario, Libro libro, Date fechaPrestamo) {
        this.usuario = usuario;
        this.libro = libro;
        this.fechaPrestamo = fechaPrestamo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public Date getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(Date fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public Date getFechaDevolucionPrevista() {
        return fechaDevolucionPrevista;
    }

    public void setFechaDevolucionPrevista(Date fechaDevolucionPrevista) {
        this.fechaDevolucionPrevista = fechaDevolucionPrevista;
    }

    public Date getFechaDevolucionReal() {
        return fechaDevolucionReal;
    }

    public void setFechaDevolucionReal(Date fechaDevolucionReal) {
        this.fechaDevolucionReal = fechaDevolucionReal;
    }

    public boolean isAmpliado() {
        return ampliado;
    }

    public void setAmpliado(boolean ampliado) {
        this.ampliado = ampliado;
    }

    public String getSancion() {
        return sancion;
    }

    public void setSancion(String sancion) {
        this.sancion = sancion;
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
        sb.append("Prestamo: {")
                .append("id=").append(id)
                .append(", usuarioId='").append(usuario != null ? usuario.getMatricula() : "null").append('\'')
                .append(", libroId='").append(libro != null ? libro.getIsbn() : "null").append('\'')
                .append(", fechaPrestamo='").append(fechaPrestamo).append('\'')
                .append(", fechaDevolucionPrevista='").append(fechaDevolucionPrevista).append('\'')
                .append(", fechaDevolucionReal='").append(fechaDevolucionReal).append('\'')
                .append(", ampliado='").append(ampliado).append('\'')
                .append(", sancion='").append(sancion).append('\'');

        if (_links != null && _links.getSelf() != null && _links.getSelf().getHref() != null) {
            sb.append(", enlace='").append(_links.getSelf().getHref()).append('\'');
        }

        sb.append('}');
        return sb.toString();
    }
}
