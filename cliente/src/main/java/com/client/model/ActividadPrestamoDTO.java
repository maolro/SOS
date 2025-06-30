package com.client.model;

import java.util.Set;

public class ActividadPrestamoDTO {
    private Usuario usuario;
    private Set<Prestamo> prestamosActuales;
    private Set<Prestamo> historicoPrestamos;

    public ActividadPrestamoDTO() {}

    public Set<Prestamo> getPrestamosActuales() {
        return prestamosActuales;
    }

    public void setPrestamosActuales(Set<Prestamo> prestamosActuales) {
        this.prestamosActuales = prestamosActuales;
    }

    public Set<Prestamo> getHistoricoPrestamos() {
        return historicoPrestamos;
    }

    public void setHistoricoPrestamos(Set<Prestamo> historicoPrestamos) {
        this.historicoPrestamos = historicoPrestamos;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void printActividadUsuario() {
        System.out.println("\n=== ACTIVIDAD DEL USUARIO ===");
        System.out.println("Usuario: " + usuario.toString());
        
        System.out.println("\nPRÉSTAMOS ACTUALES:");
        if (prestamosActuales != null && !prestamosActuales.isEmpty()) {
            for (Prestamo prestamo : prestamosActuales) {
                System.out.println(prestamo.toString());
            }
        } else {
            System.out.println("No hay préstamos actuales");
        }
        
        System.out.println("\nHISTÓRICO DE PRÉSTAMOS (últimos 5):");
        if (historicoPrestamos != null && !historicoPrestamos.isEmpty()) {
            for (Prestamo prestamo : historicoPrestamos) {
                System.out.println(prestamo.toString());
            }
        } else {
            System.out.println("No hay préstamos históricos");
        }
        
        System.out.println("=== FIN DE ACTIVIDAD ===");
    }
}

