package com.client.wrappers;

import java.util.List;

import com.client.model.UsuarioResp;

public class EmbeddedUsuarios {
    private List<UsuarioResp> usuarioList;

    public List<UsuarioResp> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<UsuarioResp> usuarioList) {
        this.usuarioList = usuarioList;
    }
}