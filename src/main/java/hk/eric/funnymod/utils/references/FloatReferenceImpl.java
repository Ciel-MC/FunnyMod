package hk.eric.funnymod.utils.references;

public class FloatReferenceImpl extends FloatReference {

    public FloatReferenceImpl(float value) {
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
