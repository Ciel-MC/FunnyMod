package hk.eric.funnymod.event.events;

import hk.eric.funnymod.event.CancellableEvent;

public class PlayerChatEvent extends CancellableEvent {
    private String message;

    public PlayerChatEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
