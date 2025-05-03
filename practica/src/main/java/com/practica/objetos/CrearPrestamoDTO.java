package com.practica.objetos;

import java.time.LocalDate;
import java.util.Date;

import jakarta.validation.constraints.NotNull;

public class CrearPrestamoDTO {
    @NotNull
    private Long usuario_id;

    @NotNull
    private Long libro_id;

    @NotNull
    private Date fechaPrestamo;

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
