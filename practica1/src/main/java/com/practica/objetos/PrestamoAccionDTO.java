package com.practica.objetos;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;

public class PrestamoAccionDTO {

    @NotBlank(message = "El tipo de operacion es obligatorio.")
    private String operacion; // "ampliar" or "devolver"
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date fechaDevolucion;

    public PrestamoAccionDTO() {}

    public PrestamoAccionDTO(String operacion, Date fechaDevolucion) {
        this.operacion = operacion;
        this.fechaDevolucion = fechaDevolucion;
    }

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public Date getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(Date fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }
}
