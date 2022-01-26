package hk.eric.funnymod.utils.references;

public class FinalFloatReference extends FloatReference {

    private final float aFloat;

    public FinalFloatReference(float value) {
        this.aFloat = value;
    }

    @Override
    public Float get() {
        return aFloat;
    }

    @Override
    public void set(Float value) {
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
