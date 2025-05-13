package com.practica.objetos;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "prestamos")
public class Prestamo extends RepresentationModel<Prestamo> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "libro_id", referencedColumnName = "id")
    private Libro libro;

    @NotNull(message = "La fecha de préstamo es obligatoria.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date fechaPrestamo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date fechaDevolucionPrevista;

    @Temporal(TemporalType.DATE)
    private Date fechaDevolucionReal = null;

    private boolean ampliado = false;

    private String sancion;

    public Prestamo() {}

    public Prestamo(Usuario usuario, Libro libro, Date fechaPrestamo) {
        this.usuario = usuario;
        this.libro = libro;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucionPrevista = calcularFechaDevolucion(fechaPrestamo);
        this.fechaDevolucionReal = null;
        this.ampliado = false;
    }

    private Date calcularFechaDevolucion(Date fechaPrestamo) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaPrestamo);
        cal.add(Calendar.DAY_OF_MONTH, 14);
        return cal.getTime();
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
        if (this.fechaDevolucionPrevista == null) {
            this.fechaDevolucionPrevista = calcularFechaDevolucion(fechaPrestamo);
        }
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
}
