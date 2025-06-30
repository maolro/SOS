package com.client.model;

import org.springframework.hateoas.PagedModel.PageMetadata;

public class PageUsuario {
    private UsuarioList _embedded;
    private PageLinks _links;
    private PageMetadata page;

    public PageUsuario() {}

    public PageUsuario(UsuarioList _embedded, PageLinks _links, 
    PageMetadata page) {
        this._embedded = _embedded;
        this._links = _links;
        this.page = page;
    }

    public UsuarioList get_embedded() {
        return _embedded;
    }

    public void set_embedded(UsuarioList _embedded) {
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

    public void imprimirPaginaUsuarios(){
        System.out.println("Total de usuarios: " + page.getTotalElements());
        System.out.println("Página actual: " + page.getNumber());
        System.out.println("Tamaño página: " + page.getSize());
        System.out.println("Número de páginas: " + page.getTotalPages());
        System.out.println("**********************");
        System.out.println("Links");
        if(_links.getFirst() != null)
            System.out.println("First: " + _links.getFirst().getHref());
        System.out.println("Self: " + _links.getSelf().getHref());
        if(_links.getNext() != null)
            System.out.println("Next: " + _links.getNext().getHref());
        if(_links.getLast() != null)
            System.out.println("Last: " + _links.getLast().getHref());
        System.out.println("**********************");
        System.out.println("Usuarios");
        if(_embedded != null){
            for (Usuario usuario : _embedded.getUsuarioList()) {
                System.out.println(usuario.toString());
            }
        }

    }
}
