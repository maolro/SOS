package com.practica.objetos;

import java.util.Set;

import org.springframework.hateoas.EntityModel;

public class ActividadPrestamoDTO {
    private EntityModel<Usuario> usuario;
    private Set<EntityModel<Prestamo>> prestamosActuales;
    private Set<EntityModel<Prestamo>> historicoPrestamos;

    public ActividadPrestamoDTO() {}

    public Set<EntityModel<Prestamo>> getPrestamosActuales() {
        return prestamosActuales;
    }

    public void setPrestamosActuales(Set<EntityModel<Prestamo>> prestamosActuales) {
        this.prestamosActuales = prestamosActuales;
    }

    public Set<EntityModel<Prestamo>> getHistoricoPrestamos() {
        return historicoPrestamos;
    }

    public void setHistoricoPrestamos(Set<EntityModel<Prestamo>> historicoPrestamos) {
        this.historicoPrestamos = historicoPrestamos;
    }

    public EntityModel<Usuario> getUsuario() {
        return usuario;
    }

    public void setUsuario(EntityModel<Usuario> usuario) {
        this.usuario = usuario;
    }
}
