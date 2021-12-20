package hk.eric.funnymod.utils.classes;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class LinkedAtomicFloatImpl extends AtomicFloat {

    private final Consumer<Float> setter;
    private final Supplier<Float> getter;

    public LinkedAtomicFloatImpl(Consumer<Float> setter, Supplier<Float> getter) {
        this.setter = setter;
        this.getter = getter;
    }

    @Override
    public float get() {
        return getter.get();
    }

    @Override
    public void set(float value) {
        setter.accept(value);
    }

    @Override
    public float add(float value) {
        setter.accept(getter.get() + value);
        return getter.get();
    }

    @Override
    public float subtract(float value) {
        setter.accept(getter.get() - value);
        return getter.get();
    }
}
