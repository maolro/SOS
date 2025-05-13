package com.practica.assembler;

import com.practica.controladores.ControladorUsuario;
import com.practica.objetos.Usuario;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

// Esta clase es un ensamblador que se usa para crear objetos paginados con enlaces para HATEOAS
// Hace falta crear un ensamblador para cada clase de recurso que se gestionará. Este es para el objeto Usuario
@Component
public class EnsambladorUsuario extends RepresentationModelAssemblerSupport<Usuario, Usuario> {
    public EnsambladorUsuario() {
        super(ControladorUsuario.class, Usuario.class);
    }

    @Override
    public Usuario toModel(Usuario entity) {

        entity.add(linkTo(methodOn(ControladorUsuario.class)
        .obtenerUsuarioPorId(entity.getId())).withSelfRel());
        
        return entity;
    }
}
