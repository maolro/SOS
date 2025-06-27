package com.client.wrappers;

public class UsuarioPagedResponse {
    private EmbeddedUsuarios _embedded;

    public EmbeddedUsuarios get_embedded() {
        return _embedded;
    }

    public void set_embedded(EmbeddedUsuarios _embedded) {
        this._embedded = _embedded;
    }
}
