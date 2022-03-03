package hk.eric.funnymod.utils.classes.caches;

import hk.eric.funnymod.utils.classes.circularQueue.CircularQueue;
import hk.eric.funnymod.utils.classes.pairs.Pair;

import java.util.function.Function;

public class Cache<T, R> {

    private final CircularQueue<Pair<T,R>> cache;
    private final Function<T, R> getter;

    public Cache(Function<T, R> getter, int size) {
        cache = new CircularQueue<>(size);
        this.getter = getter;
    }

    public R get(T t) {
        for (Pair<T, R> pair : cache.asList()) {
            if (pair.getFirst().equals(t)) {
                return pair.getSecond();
            }
        }
        R r = getter.apply(t);
        cache.push(new Pair<>(t, r));
        return r;
    }
}
