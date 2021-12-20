package hk.eric.funnymod.utils.classes;

public class FinalAtomicFloat extends AtomicFloat {

    private final float value;

    public FinalAtomicFloat(float value) {
        this.value = value;
    }

    @Override
    public float get() {
        return value;
    }

    @Override
    public void set(float value) {
        throw new UnsupportedOperationException("Cannot set value of a Final Atomic class");
    }

    @Override
    public float add(float value) {
        throw new UnsupportedOperationException("Cannot set value of a Final Atomic class");
    }

    @Override
    public float subtract(float value) {
        throw new UnsupportedOperationException("Cannot set value of a Final Atomic class");
    }
}
