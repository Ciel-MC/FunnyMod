package hk.eric.funnymod.utils.classes;

public class Flag {

    private boolean set = false;

    public void set() {
        set = true;
    }

    public boolean consume() {
        if (set) {
            set = false;
            return true;
        }else {
            return false;
        }
    }

    public boolean isSet() {
        return set;
    }
}
