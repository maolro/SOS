package com.client;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class CrearPrestamoDTO {
    private Long usuario_id;
    private Long libro_id;
    private Date fechaPrestamo;

    public CrearPrestamoDTO(Long usuario_id, Long libro_id, String fechaPrestamo){
        this.usuario_id = usuario_id;
        this.libro_id = libro_id;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(fechaPrestamo, formatter);
        this.fechaPrestamo = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
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

    @Override
    public String toString() {
        return "PrestamoDTO: {" +
                ", usuarioId='" + usuario_id + '\'' +
                ", libroId='" + libro_id + '\'' +
                ", fechaPresamo='" + fechaPrestamo + '\'' +
                '}';
    }
}
