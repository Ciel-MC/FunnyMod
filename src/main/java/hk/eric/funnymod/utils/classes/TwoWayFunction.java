package hk.eric.funnymod.utils.classes;

public interface TwoWayFunction<A,B> {
    B convert(A a);

    A revert(B b);

    default TwoWayFunction<B,A> reverse() {
        return new TwoWayFunction<B, A>() {
            @Override
            public A convert(B b) {
                return TwoWayFunction.this.revert(b);
            }

            @Override
            public B revert(A a) {
                return TwoWayFunction.this.convert(a);
            }
        };
    }
}
