package hk.eric.funnymod.utils.classes;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class TwoDimensionalEnumMap<K1 extends Enum<K1>, K2 extends Enum<K2>, V> implements TwoDimensionalMap<K1,K2,V>{

    private final EnumMap<K1, EnumMap<K2,V>> map;
    private final Class<K1> k1Class;
    private final Class<K2> k2Class;

    public TwoDimensionalEnumMap(Class<K1> k1, Class<K2> k2) {
        k1Class = k1;
        k2Class = k2;
        map = new EnumMap<>(k1);
    }

    @Override
    public V put(K1 k1, K2 k2, V v) {
        return map.computeIfAbsent(k1, k -> new EnumMap<>(k2Class)).put(k2, v);
    }

    @Override
    public V get(K1 k1, K2 k2) {
        return map.get(k1).get(k2);
    }

    @Override
    public V remove(K1 k1, K2 k2) {
        return map.get(k1).remove(k2);
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
    public boolean containsKey(K1 k1, K2 k2) {
        return map.containsKey(k1) && map.get(k1).containsKey(k2);
    }

    @Override
    public boolean containsValue(V v) {
        boolean returnValue = false;
        for (Map.Entry<K1, EnumMap<K2, V>> entry : map.entrySet()) {
            if (entry.getValue().containsValue(v)) {
                returnValue = true;
                break;
            }
        }
        return returnValue;
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public int size() {
        AtomicInteger size = new AtomicInteger();
        map.values().forEach(enumMap -> size.getAndAdd(enumMap.size()));
        return size.get();
    }

    @Override
    public void clear() {
        map.clear();
    }

    @SafeVarargs
    public static <K1 extends Enum<K1>, K2 extends Enum<K2>, V> TwoDimensionalEnumMap<K1,K2,V> of(Class<K1> k1Class, Class<K2> k2Class, TypedPairList<K1,K2,V>... keys) {
        TwoDimensionalEnumMap<K1,K2,V> map = new TwoDimensionalEnumMap<>(k1Class, k2Class);
        for (TypedPairList<K1, K2, V> key : keys) {
            key.forEach(pair -> map.put(key.getType(), pair.getFirst(), pair.getSecond()));
        }
        return map;
    }
}
