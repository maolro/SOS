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

    @NotNull(message = "El id del usuario es obligatorio.")
    private Long usuarioId;

    @NotNull(message = "El id del usuario es obligatorio.")
    private Long libroId;

    @NotNull(message = "La fecha de préstamo es obligatoria.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date fechaPrestamo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date fechaDevolucionPrevista;

    @Temporal(TemporalType.DATE)
    private Date fechaDevolucionReal = null;

    private boolean ampliado = false;

    private String sancion;

    public Prestamo() {}

    public Prestamo(Long usuario_id, Long libro_id, Date fechaPrestamo) {
        this.usuarioId = usuario_id;
        this.libroId = libro_id;
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

    public Long getUsuarioId() { return usuarioId; }

    public void setUsuarioId(Long usuario_id) { this.usuarioId = usuario_id; }

    public Long getLibroId() { return libroId; }

    public void setLibro(Long libro_id) { this.libroId = libro_id; }

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
