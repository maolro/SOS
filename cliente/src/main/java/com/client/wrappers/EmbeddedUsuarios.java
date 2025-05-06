package com.client.wrappers;

import java.util.List;

import com.client.Usuario;

public class EmbeddedUsuarios {
    private List<Usuario> usuarioList;

    public List<Usuario> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
    }
}