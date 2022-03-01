package hk.eric.funnymod.utils.classes.pairs;

public class TriPair<A, B, C> {
    protected A a;
    protected B b;
    protected C c;

    public TriPair(A a, B b, C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public TriPair() {}

    public A getA() {
        return a;
    }

    public void setA(A a) {
        this.a = a;
    }

    public B getB() {
        return b;
    }

    public void setB(B b) {
        this.b = b;
    }

    public C getC() {
        return c;
    }

    public void setC(C c) {
        this.c = c;
    }
}
