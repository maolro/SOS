package com.client.model;

public class PageLinks {
    private Href first;
    private Href self;
    private Href next;
    private Href last;

    public PageLinks() {}

    public PageLinks(Href first, Href self, Href next, Href last) {
        this.first = first;
        this.self = self;
        this.next = next;
        this.last = last;
    }

    public Href getFirst() {
        return first;
    }

    public void setFirst(Href first) {
        this.first = first;
    }

    public Href getSelf() {
        return self;
    }

    public void setSelf(Href self) {
        this.self = self;
    }

    public Href getNext() {
        return next;
    }

    public void setNext(Href next) {
        this.next = next;
    }

    public Href getLast() {
        return last;
    }

    public void setLast(Href last) {
        this.last = last;
    }
}

