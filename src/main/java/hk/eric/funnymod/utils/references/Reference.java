package hk.eric.funnymod.utils.references;

public abstract class Reference<T> {

    protected T value;

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }

}
