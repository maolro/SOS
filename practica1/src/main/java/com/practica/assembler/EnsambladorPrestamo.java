package com.practica.assembler;

import com.practica.controladores.ControladorUsuario;
import com.practica.objetos.Prestamo;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

// Esta clase es un ensamblador que se usa para crear objetos paginados con enlaces para HATEOAS
// Hace falta crear un ensamblador para cada clase de recurso que se gestionará. Este es para el objeto Préstamo
@Component
public class EnsambladorPrestamo extends RepresentationModelAssemblerSupport<Prestamo, Prestamo> {

    public EnsambladorPrestamo() {
        super(ControladorUsuario.class, Prestamo.class);
    }

    @Override
    public Prestamo toModel(Prestamo entity) {
        entity.add(linkTo(methodOn(ControladorUsuario.class)
            .obtenerPrestamoPorId(entity.getUsuario().getMatricula(), 
            entity.getId())).withSelfRel());
        
        return entity;
    }
}
