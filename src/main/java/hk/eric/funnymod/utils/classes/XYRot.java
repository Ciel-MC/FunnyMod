package hk.eric.funnymod.utils.classes;

import hk.eric.funnymod.utils.classes.pairs.Pair;

public class XYRot extends Pair<Float, Float> {

    public XYRot() {
    }

    public XYRot(float first, float second) {
        super(first, second);
    }

    public void setXRot(float x){
        this.first = x;
    }

    public void setYRot(float y){
        this.second = y;
    }

    public float getXRot(){
        return this.first;
    }

    public float getYRot(){
        return this.second;
    }

    public float getYaw(){
        return this.getYRot();
    }

    public float getPitch(){
        return this.getXRot();
    }

    public void setYaw(float y){
        this.setYRot(y);
    }

    public void setPitch(float x){
        this.setXRot(x);
    }

    @Override
    public String toString() {
        return "XYRot{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}
