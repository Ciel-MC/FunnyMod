package hk.eric.funnymod.utils.classes;

public interface TwoDimensionalMap<K1, K2, V> {
    V put(K1 k1, K2 k2, V v);

    V get(K1 k1, K2 k2);

    V remove(K1 k1, K2 k2);

    void put(TypedPairList<K1, K2, V> content);

    void put(TypedPairList<K2, K1, V> content, boolean vertical);


    boolean containsKey(K1 k1, K2 k2);

    boolean containsValue(V v);

    boolean isEmpty();

    int size();

    void clear();
}
