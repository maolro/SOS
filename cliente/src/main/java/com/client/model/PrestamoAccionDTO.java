package com.client.model;

public class PrestamoAccionDTO {
    private String operacion; 
    private String fechaDevolucion;

    public PrestamoAccionDTO() {}

    public PrestamoAccionDTO(String operacion, String fechaDevolucion) {
        this.operacion = operacion;
        this.fechaDevolucion = fechaDevolucion;
    }

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public String getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(String fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }
}
