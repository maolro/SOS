package com.practica.objetos;

import java.util.Date;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

public class UsuarioPrestamo {

    @NotNull(message = "El id del usuario es obligatorio y no puede ser null")
    private Long usuario_id;

    @NotNull(message = "El id del libro es obligatorio y no puede ser null")
    private Long libro_id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date fechaPrestamo;

    public UsuarioPrestamo() {}

    public UsuarioPrestamo(Long usuario_id, Long libro_id, Date fechaPrestamo) {
        this.usuario_id = usuario_id;
        this.libro_id = libro_id;
        this.fechaPrestamo = fechaPrestamo;
    }

    public Long getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(Long usuario_id) {
        this.usuario_id = usuario_id;
    }

    public Long getLibro_id() {
        return libro_id;
    }

    public void setLibro_id(Long libro_id) {
        this.libro_id = libro_id;
    }

    public Date getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(Date fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }
}
