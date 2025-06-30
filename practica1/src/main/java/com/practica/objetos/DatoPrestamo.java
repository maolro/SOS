package com.practica.objetos;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class DatoPrestamo {

    private String libro_id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date fechaPrestamo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date fechaDevolucion;

    public DatoPrestamo() {}

    public DatoPrestamo(String libro_id, Date fechaPrestamo) {
        this.libro_id = libro_id;
        this.fechaPrestamo = fechaPrestamo;
    }

    public String getLibro_id() {
        return libro_id;
    }

    public void setLibro_id(String libro_id) {
        this.libro_id = libro_id;
    }

    public Date getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(Date fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public Date getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(Date fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }
}
