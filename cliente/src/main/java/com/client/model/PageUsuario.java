package com.client.model;

import org.springframework.hateoas.PagedModel.PageMetadata;

public class PageUsuario {
    private Usuario _embedded;
    private PageLinks _links;
    private PageMetadata page;

    public PageUsuario() {}

    public PageUsuario(Usuario _embedded, PageLinks _links, PageMetadata page) {
        this._embedded = _embedded;
        this._links = _links;
        this.page = page;
    }

    public Usuario get_embedded() {
        return _embedded;
    }

    public void set_embedded(Usuario _embedded) {
        this._embedded = _embedded;
    }

    public PageLinks get_links() {
        return _links;
    }

    public void set_links(PageLinks _links) {
        this._links = _links;
    }

    public PageMetadata getPage() {
        return page;
    }

    public void setPage(PageMetadata page) {
        this.page = page;
    }
}
