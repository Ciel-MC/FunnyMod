package hk.eric.funnymod.utils.classes.getters;

import hk.eric.funnymod.utils.classes.caches.CircularQueueCache;

import java.util.function.Function;

public class CachedLambdaGetter<T, R> extends LambdaGetter<T, R> {

    private final CircularQueueCache<T, R> cache;

    public CachedLambdaGetter(Function<T, R> function, Getter<T> getter, int cacheSize) {
        super(function, getter);
        this.cache = new CircularQueueCache<>(function, cacheSize);
    }

    @Override
    public R get() {
        return cache.get(getter.get());
    }

    public Getter<R> toGetter() {
        return CachedLambdaGetter.this;
    }
}
