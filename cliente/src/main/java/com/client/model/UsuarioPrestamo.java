package com.client.model;

import java.util.Date;

public class UsuarioPrestamo {

    private Long usuario_id;

    private Long libro_id;

    private String fechaPrestamo;

    private String fechaDevolucionReal;

    private boolean ampliado;

    public UsuarioPrestamo() {}

    public UsuarioPrestamo(Long usuario_id, Long libro_id, String fechaPrestamo, 
    String fechaDevolucionReal, boolean ampliado) {
        this.usuario_id = usuario_id;
        this.libro_id = libro_id;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucionReal = fechaDevolucionReal;
        this.ampliado = ampliado;
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

    public String getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(String fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public String getFechaDevolucionReal() {
        return fechaPrestamo;
    }

    public void setFechaDevolucionReal(String fechaDevolucionReal) {
        this.fechaDevolucionReal = fechaDevolucionReal;
    }

    public boolean getAmpliado() {
        return ampliado;
    }

    public void setAmpliado(boolean ampliado) {
        this.ampliado = ampliado;
    }
}
