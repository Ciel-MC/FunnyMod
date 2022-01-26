package hk.eric.funnymod.utils.references;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class LinkedFloatReferenceImpl extends FloatReference {

    private final Consumer<Float> setter;
    private final Supplier<Float> getter;

    public LinkedFloatReferenceImpl(Consumer<Float> setter, Supplier<Float> getter) {
        this.setter = setter;
        this.getter = getter;
    }

    @Override
    public Float get() {
        return getter.get();
    }

    @Override
    public void set(Float value) {
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
