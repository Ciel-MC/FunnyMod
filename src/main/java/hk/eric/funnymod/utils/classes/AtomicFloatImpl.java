package hk.eric.funnymod.utils.classes;

public class AtomicFloatImpl extends AtomicFloat {

    private float value;

    public AtomicFloatImpl(float value) {
        this.value = value;
    }

    @Override
    public float get() {
        return value;
    }

    @Override
    public void set(float value) {
        this.value = value;
    }

    @Override
    public float add(float value) {
        return this.value += value;
    }

    @Override
    public float subtract(float value) {
        return this.value -= value;
    }


}
