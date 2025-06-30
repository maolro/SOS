package com.client.model;

public class ResourceLink {
    private Href self;

    public ResourceLink() {}

    public ResourceLink(Href self) {
        this.self = self;
    }

    public Href getSelf() {
        return self;
    }

    public void setHref(Href self) {
        this.self = self;
    }
}
