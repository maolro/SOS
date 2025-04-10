package com.practica;

import java.util.Date;
import java.util.Calendar;

public class Prestamo {
    private int id;
    private int usuarioId;
    private int libroId;
    private Date fechaPrestamo;
    private Date fechaDevolucionPrevista;
    private Date fechaDevolucionReal;
    private boolean ampliado;
    private String sancion;

    public Prestamo() {}

    public Prestamo(int usuarioId, int libroId, Date fechaPrestamo) {
        this.usuarioId = usuarioId;
        this.libroId = libroId;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucionPrevista = calcularFechaDevolucion(fechaPrestamo);
        this.ampliado = false;
        this.sancion = null;
    }

    private Date calcularFechaDevolucion(Date fechaPrestamo) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaPrestamo);
        calendar.add(Calendar.DAY_OF_YEAR, 14); 
        return calendar.getTime();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getLibroId() {
        return libroId;
    }

    public void setLibroId(int libroId) {
        this.libroId = libroId;
    }

    public Date getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(Date fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
        if (fechaPrestamo != null) {
            this.fechaDevolucionPrevista = calcularFechaDevolucion(fechaPrestamo);
        }
    }

    public Date getFechaDevolucionPrevista() {
        return fechaDevolucionPrevista;
    }

    public Date getFechaDevolucionReal() {
        return fechaDevolucionReal;
    }

    public void setFechaDevolucionReal(Date fechaDevolucionReal) {
        this.fechaDevolucionReal = fechaDevolucionReal;
    }

    public boolean esAmpliado() {
        return ampliado;
    }

    public void setAmpliado(boolean ampliado) {
        this.ampliado = ampliado;
    }

    public String getSancion() {
        return sancion;
    }

    public void setSancion(String sancion) {
        this.sancion = sancion;
    }
}