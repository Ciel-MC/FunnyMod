package hk.eric.funnymod.utils.classes.caches;

import java.util.HashMap;
import java.util.function.Function;

public class HashMapCache< K , V > implements Cache<K, V> {

    private final Function<K, V> function;
    private final HashMap<K, V> map = new HashMap<>();

    public HashMapCache(Function<K, V> function) {
        this.function = function;
    }

    @Override
    public V get(K k) {
        if (map.containsKey(k)) {
            return map.get(k);
        }else {
            V v = function.apply(k);
            map.put(k, v);
            return v;
        }
    }
}
