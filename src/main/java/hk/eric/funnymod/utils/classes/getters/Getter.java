package hk.eric.funnymod.utils.classes.getters;

public interface Getter<T> {

    static <T> Getter<T> fixed(T value) {
        return new Getter<>() {
            @Override
            public T get() {
                return value;
            }

            @Override
            public boolean isFixed() {
                return true;
            }
        };
    }

    default boolean isFixed() {
        return false;
    }

    T get();

}
