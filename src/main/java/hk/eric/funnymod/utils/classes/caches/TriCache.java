package hk.eric.funnymod.utils.classes.caches;

import hk.eric.funnymod.utils.classes.circularQueue.CircularQueue;
import hk.eric.funnymod.utils.classes.lamdba.TriFunction;
import hk.eric.funnymod.utils.classes.pairs.QuadPair;

public class TriCache<A, B, C, R> {

    private final CircularQueue<QuadPair<A, B, C, R>> cache;
    private final TriFunction<A, B, C, R> getter;

    public TriCache(TriFunction<A, B, C, R> getter, int size) {
        this.getter = getter;
        cache = new CircularQueue<>(size);
    }

    public R get(A a, B b, C c) {
        for (QuadPair<A, B, C, R> pair : cache.asList()) {
            if (pair.getA().equals(a) && pair.getB().equals(b) && pair.getC().equals(c)) {
                return pair.getD();
            }
        }
        R r = getter.apply(a, b, c);
        cache.push(new QuadPair<>(a, b, c, r));
        return r;
    }
}
