package hk.eric.funnymod.event.events;

import hk.eric.funnymod.event.EventCancellable;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;

import java.util.UUID;

public class ChatReceivedEvent extends EventCancellable {

    private Component message;
    private ChatType type;
    private UUID sender;

    public ChatReceivedEvent(Component message, ChatType type, UUID sender) {
        this.message = message;
        this.type = type;
        this.sender = sender;
    }

    public Component getMessage() {
        return message;
    }

    public void setMessage(Component message) {
        this.message = message;
    }

    public ChatType getType() {
        return type;
    }

    public void setType(ChatType type) {
        this.type = type;
    }

    public UUID getSender() {
        return sender;
    }

    public void setSender(UUID sender) {
        this.sender = sender;
    }
}
