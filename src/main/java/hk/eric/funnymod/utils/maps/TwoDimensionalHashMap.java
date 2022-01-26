package hk.eric.funnymod.utils.maps;

import hk.eric.funnymod.utils.classes.TypedPairList;
import hk.eric.funnymod.utils.classes.pairs.Pair;

import java.util.HashMap;

public class TwoDimensionalHashMap<K1, K2, V> implements TwoDimensionalMap<K1, K2, V> {

    private final HashMap<Pair<K1,K2>,V> map = new HashMap<>();

    @Override
    public V put(K1 k1, K2 k2, V v) {
        return map.put(new Pair<>(k1,k2),v);
    }

    @Override
    public void put(TypedPairList<K1, K2, V> content) {
        content.forEach(pair -> put(content.getType(), pair.getFirst(), pair.getSecond()));
    }

    @Override
    public void put(TypedPairList<K2, K1, V> content, boolean vertical) {
        content.forEach(pair -> put(pair.getFirst(), content.getType(), pair.getSecond()));
    }

    @Override
    public V get(K1 k1, K2 k2) {
        return map.get(new Pair<>(k1,k2));
    }

    @Override
    public V remove(K1 k1, K2 k2) {
        return map.remove(new Pair<>(k1,k2));
    }

    @Override
    public boolean containsKey(K1 k1, K2 k2) {
        return map.containsKey(new Pair<>(k1,k2));
    }

    @Override
    public boolean containsValue(V v) {
        return map.containsValue(v);
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public void clear() {
        map.clear();
    }

    @SafeVarargs
    public static <V1, V2, K> TwoDimensionalHashMap<V1,V2,K> of(TypedPairList<V1,V2,K>... keys) {
        TwoDimensionalHashMap<V1,V2,K> map = new TwoDimensionalHashMap<>();
        for (TypedPairList<V1, V2, K> key : keys) {
            key.forEach(pair -> map.put(key.getType(), pair.getFirst(), pair.getSecond()));
        }
        return map;
    }
}
