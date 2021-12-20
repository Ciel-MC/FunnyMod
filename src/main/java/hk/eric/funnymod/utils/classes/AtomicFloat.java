package hk.eric.funnymod.utils.classes;

public abstract class AtomicFloat {
    private float value;

    public abstract float get();

    public abstract void set(final float value);

    public abstract float add(final float value);

    public abstract float subtract(final float value);

}
