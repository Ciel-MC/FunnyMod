package hk.eric.funnymod.utils;

public interface HasFlag {

    boolean getFlag(Flags flag) ;

    void setFlag(Flags flag, Object value);

    enum Flags {
        SENT_BY_FUNNY_MOD
    }
}
