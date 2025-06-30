package com.client.model;

public class DatoPrestamo {

    private String libro_id;

    private String fechaPrestamo;

    private String fechaDevolucion;

    public DatoPrestamo() {}

    public DatoPrestamo(String libro_id, String fechaPrestamo) {
        this.libro_id = libro_id;
        this.fechaPrestamo = fechaPrestamo;
    }

    public String getLibro_id() {
        return libro_id;
    }

    public void setLibro_id(String libro_id) {
        this.libro_id = libro_id;
    }

    public String getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(String fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public String getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(String fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }
}
