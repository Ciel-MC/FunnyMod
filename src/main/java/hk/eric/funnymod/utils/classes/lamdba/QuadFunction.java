package hk.eric.funnymod.utils.classes.lamdba;

public interface QuadFunction<A,B,C,D,R> {
    R apply(A a, B b, C c, D d);
}
