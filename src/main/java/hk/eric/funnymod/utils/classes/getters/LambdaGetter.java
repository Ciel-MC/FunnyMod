package hk.eric.funnymod.utils.classes.getters;

import java.util.function.Function;

public abstract class LambdaGetter<T, R> implements Getter<R> {


    protected final Function<T, R> function;
    protected final Getter<T> getter;

    public LambdaGetter(Function<T, R> function, Getter<T> getter) {
        this.function = function;
        this.getter = getter;
    }

    @Override
    public R get() {
        return function.apply(getter.get());
    }
}
