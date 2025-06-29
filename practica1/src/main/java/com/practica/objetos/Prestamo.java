package com.practica.objetos;

import java.util.Date;

import org.springframework.hateoas.RepresentationModel;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "prestamos")
public class Prestamo extends RepresentationModel<Prestamo>{

    
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

    public boolean getAmpliado() { return ampliado; }

    public void setAmpliado(boolean ampliado) { this.ampliado = ampliado; }

    @Override
    public String toString() {
        return "Prestamo: {" +
                "id=" + id +
                ", usuarioId='" + usuario.getMatricula() + '\'' +
                ", libroId='" + libro.getISBN() + '\'' +
                ", fechaPrestamo='" + fechaPrestamo + '\'' +
                ", fechaDevolucionPrevista='" + fechaDevolucionPrevista + '\'' +
                ", fechaDevolucionReal='" + fechaDevolucionReal + '\'' +
                ", ampliado='" + ampliado + '\'' +
                ", sancion='" + sancion + '\'' +
                '}';
    }
}