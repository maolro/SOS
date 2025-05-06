package com.client.wrappers;

public class PrestamoPagedResponse {
    private EmbeddedPrestamo _embedded;

    public EmbeddedPrestamo get_embedded() {
        return _embedded;
    }

    public void set_embedded(EmbeddedPrestamo _embedded) {
        this._embedded = _embedded;
    }
}
