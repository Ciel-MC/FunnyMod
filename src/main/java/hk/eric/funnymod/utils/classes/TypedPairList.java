package hk.eric.funnymod.utils.classes;

import java.util.ArrayList;
import java.util.Collection;

public class TypedPairList<K1, K2, V > extends ArrayList<Pair<K2,V>> {
    private final K1 type;

    public TypedPairList(K1 type) {
        this.type = type;
    }

    public TypedPairList(K1 type, Collection<? extends Pair<K2,V>> c) {
        super(c);
        this.type = type;
    }

    public K1 getType() {
        return type;
    }

    public TypedPairList(K1 type, K2 k1, V v1) {
        this.type = type;
        add(new Pair<>(k1, v1));
    }

    public TypedPairList(K1 type, K2 k1, V v1, K2 k2, V v2) {
        this.type = type;
        add(new Pair<>(k1, v1));
        add(new Pair<>(k2, v2));
    }

    public TypedPairList(K1 type, K2 k1, V v1, K2 k2, V v2, K2 k3, V v3) {
        this.type = type;
        add(new Pair<>(k1, v1));
        add(new Pair<>(k2, v2));
        add(new Pair<>(k3, v3));
    }

    public TypedPairList(K1 type, K2 k1, V v1, K2 k2, V v2, K2 k3, V v3, K2 k4, V v4) {
        this.type = type;
        add(new Pair<>(k1, v1));
        add(new Pair<>(k2, v2));
        add(new Pair<>(k3, v3));
        add(new Pair<>(k4, v4));
    }

    public TypedPairList(K1 type, K2 k1, V v1, K2 k2, V v2, K2 k3, V v3, K2 k4, V v4, K2 k5, V v5) {
        this.type = type;
        add(new Pair<>(k1, v1));
        add(new Pair<>(k2, v2));
        add(new Pair<>(k3, v3));
        add(new Pair<>(k4, v4));
        add(new Pair<>(k5, v5));
    }

    public TypedPairList(K1 type, Object... objects) {
        this.type = type;
        for (int i = 0; i < objects.length; i += 2) {
            //noinspection unchecked
            add(new Pair<>((K2)objects[i], (V)objects[i+1]));
        }
    }
}
