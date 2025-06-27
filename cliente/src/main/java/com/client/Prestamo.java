package com.client;

import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Prestamo {

    private Long id;
    private Usuario usuario;
    private Libro libro;
    private Date fechaPrestamo;
    private Date fechaDevolucionPrevista;
    private Date fechaDevolucionReal;
    private boolean ampliado;
    private String sancion;

    public Prestamo() {}

    public Prestamo(Usuario usuario, Libro libro, Date fechaPrestamo) {
        this.usuario = usuario;
        this.libro = libro;
        this.fechaPrestamo = fechaPrestamo;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }

    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Libro getLibro() { return libro; }

    public void setLibro(Libro libro) { this.libro = libro; }

    public Date getFechaPrestamo() { return fechaPrestamo; }

    public void setFechaPrestamo(Date fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public Date getFechaDevolucionPrevista() { return fechaDevolucionPrevista; }

    public void setFechaDevolucionPrevista(Date fechaDevolucionPrevista) {
        this.fechaDevolucionPrevista = fechaDevolucionPrevista;
    }

    public Date getFechaDevolucionReal() { return fechaDevolucionReal; }

    public void setFechaDevolucionReal(Date fechaDevolucionReal) {
        this.fechaDevolucionReal = fechaDevolucionReal;
    }

    public boolean isAmpliado() { return ampliado; }

    public void setAmpliado(boolean ampliado) { this.ampliado = ampliado; }

    public String getSancion() { return sancion; }

    public void setSancion(String sancion) { this.sancion = sancion; }

    @Override
    public String toString() {
        return "Prestamo: {" +
                "id=" + id +
                ", usuarioId='" + usuario.getId() + '\'' +
                ", libroId='" + libro.getId() + '\'' +
                ", fechaPrestamo='" + fechaPrestamo + '\'' +
                ", fechaDevolucionPrevista='" + fechaDevolucionPrevista + '\'' +
                ", fechaDevolucionReal='" + fechaDevolucionReal + '\'' +
                ", ampliado='" + ampliado + '\'' +
                ", sancion='" + sancion + '\'' +
                '}';
    }
}
