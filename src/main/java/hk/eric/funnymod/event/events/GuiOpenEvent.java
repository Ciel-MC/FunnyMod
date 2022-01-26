package hk.eric.funnymod.event.events;

import hk.eric.funnymod.event.EventCancellable;
import net.minecraft.network.chat.Component;

public class GuiOpenEvent extends EventCancellable {
    private final int containerId;
    private final int type;
    private final Component title;

    public GuiOpenEvent(int containerId, int type, Component title) {
        this.containerId = containerId;
        this.type = type;
        this.title = title;
    }

    public int getContainerId() {
        return containerId;
    }

    public int getType() {
        return type;
    }

    public Component getTitle() {
        return title;
    }
}
