package hk.eric.funnymod.utils.classes;

public class LiteralTwoWayFunction<A> implements TwoWayFunction<A, A> {
    @Override
    public A convert(A a) {
        return a;
    }

    @Override
    public A revert(A a) {
        return a;
    }
}
