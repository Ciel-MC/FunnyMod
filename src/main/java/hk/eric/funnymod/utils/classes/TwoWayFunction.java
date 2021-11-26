package hk.eric.funnymod.utils.classes;

public interface TwoWayFunction<A,B> {
    B convert(A a);

    A revert(B b);
}
