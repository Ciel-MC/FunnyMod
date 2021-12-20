package hk.eric.funnymod.event.events;

import hk.eric.funnymod.event.EventCancellable;

public class KeyEvent extends EventCancellable {

    private final int key, action;

    public KeyEvent(int key, int action) {
        this.key = key;
        this.action = action;
    }

    public int getKey() {
        return key;
    }

    public int getAction() {
        return action;
    }
}
