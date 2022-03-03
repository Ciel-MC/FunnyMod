package hk.eric.funnymod.utils.classes.caches;

import java.util.function.Function;

public record DebugCache<T, R>(Function<T, R> getter) implements Cache<T, R> {

    @Override
    public R get(T t) {
        return getter.apply(t);
    }
}
