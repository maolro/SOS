package com.practica;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Service;

@Service
public class ServicioUsuario {
    private final Map<Integer, Usuario> usuarios = new HashMap<>();
    
    private final AtomicInteger idCounter = new AtomicInteger(1);
    
    public Usuario crearUsuario(Usuario usuario) {
        int id = idCounter.getAndIncrement();
        usuario.setId(id);
        usuarios.put(id, usuario);
        return usuario;
    }
    
    public List<Usuario> obtenerTodosUsuarios() {
        return new ArrayList<>(usuarios.values());
    }
    
    public Usuario obtenerUsuarioPorId(Integer id) {
        return usuarios.get(id);
    }

    // Método para PUT
    public Usuario actualizarUsuario(Integer id, Usuario usuarioActualizado) {
        if (usuarios.containsKey(id)) {
            usuarioActualizado.setId(id); 
            usuarios.put(id, usuarioActualizado);
            return usuarioActualizado;
        }
        return null;
    }
    // Método para DELETE
    public boolean eliminarUsuario(Integer id) {
        return usuarios.remove(id) != null;
    }
}