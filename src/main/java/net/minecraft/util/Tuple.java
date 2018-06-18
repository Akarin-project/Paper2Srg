package net.minecraft.util;

public class Tuple<A, B> {

    private A a;
    private B b;

    public Tuple(A a0, B b0) {
        this.a = a0;
        this.b = b0;
    }

    public A getFirst() {
        return this.a;
    }

    public B getSecond() {
        return this.b;
    }
}
