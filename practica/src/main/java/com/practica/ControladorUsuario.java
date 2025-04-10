package com.practica;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
public class ControladorUsuario {
    private final ServicioUsuario userService;

    public ControladorUsuario(ServicioUsuario userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<Usuario> obtenerTodosUsuarios() {
        return userService.obtenerTodosUsuarios();
    }

    @GetMapping("/{id}")
    public Usuario obtenerUsuarioPorId(@PathVariable Integer id) {
        return userService.obtenerUsuarioPorId(id);
    }

    @PostMapping
    public Usuario crearUsuario(@RequestBody Usuario user) {
        return userService.crearUsuario(user);
    }

    @DeleteMapping("/{id}")
    public void eliminarUsuario(@PathVariable Integer id) {
        userService.eliminarUsuario(id);
    }

    @PutMapping("/{id}")
    public Usuario actualizarUsuario(@PathVariable Integer id, @RequestBody Usuario user) {
        return userService.actualizarUsuario(id, user);
    }
}
