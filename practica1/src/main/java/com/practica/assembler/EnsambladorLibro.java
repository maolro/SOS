package com.practica.assembler;

import com.practica.controladores.ControladorLibro;
import com.practica.objetos.Libro;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

// Esta clase es un ensamblador que se usa para crear objetos paginados con enlaces para HATEOAS
// Hace falta crear un ensamblador para cada clase de recurso que se gestionará. Este es para el objeto Libro
@Component
public class EnsambladorLibro extends RepresentationModelAssemblerSupport<Libro, Libro> {
    
    public EnsambladorLibro() {
        super(ControladorLibro.class, Libro.class);
    }

    @Override
    public Libro toModel(Libro entity) {
        entity.add(linkTo(methodOn(ControladorLibro.class)
            .obtenerLibroPorId(entity.getId())).withSelfRel());
        return entity;
    }
}
