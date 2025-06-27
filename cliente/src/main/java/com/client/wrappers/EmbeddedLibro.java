package com.client.wrappers;

import java.util.List;

import com.client.Libro;

public class EmbeddedLibro {
    private List<Libro> libroList;

    public List<Libro> getLibroList() {
        return libroList;
    }

    public void setLibroList(List<Libro> libroList) {
        this.libroList = libroList;
    }
}