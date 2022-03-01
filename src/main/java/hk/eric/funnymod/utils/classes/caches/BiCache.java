package hk.eric.funnymod.utils.classes.caches;

import hk.eric.funnymod.utils.classes.circularQueue.CircularQueue;
import hk.eric.funnymod.utils.classes.pairs.TriPair;

import java.util.function.BiFunction;

public class BiCache<T, U, R> {

    private final CircularQueue<TriPair<T,U,R>> cache;
    private final BiFunction<T, U, R> getter;

    public BiCache(BiFunction<T, U, R> getter, int size) {
        cache = new CircularQueue<>(size);
        this.getter = getter;
    }

    public R get(T t, U u) {
        for (TriPair<T, U, R> pair : cache.asList()) {
            if (t.equals(pair.getA()) && u.equals(pair.getB())) {
                return pair.getC();
            }
        }
        R r = getter.apply(t,u);
        cache.push(new TriPair<>(t, u, r));
        return r;
    }
}
