package com.example.hannes.choppic;

public class TriangleChildren {
    private Triangle t1, t2;

    public TriangleChildren(Triangle t1, Triangle t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    public Triangle getT1() {
        return t1;
    }

    public Triangle getT2() {
        return t2;
    }
}
