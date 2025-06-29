package com.practica.objetos;

import java.util.Date;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

public class DatoPrestamo {

    private String libro_id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date fechaPrestamo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date fechaDevolucion;

    private Boolean ampliado = false;

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

    public void getFechaDevolucion(Date fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }   

    public Boolean getAmpliado(){
        return ampliado;
    }

    public void setAmpliado(Boolean ampliado){
        this.ampliado = ampliado;
    }
}
