package com.client.wrappers;

import java.util.List;

import com.client.Prestamo;

public class EmbeddedPrestamo {
    private List<Prestamo> prestamoList;

    public List<Prestamo> getPrestamoList() {
        return prestamoList;
    }

    public void setPrestamoList(List<Prestamo> prestamoList) {
        this.prestamoList = prestamoList;
    }
}
