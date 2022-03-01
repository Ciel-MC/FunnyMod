package hk.eric.funnymod.event.events;

import hk.eric.funnymod.event.EventCancellable;

public class PlayerChatEvent extends EventCancellable {
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
