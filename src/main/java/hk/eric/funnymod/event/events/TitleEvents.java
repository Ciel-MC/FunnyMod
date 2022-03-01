package hk.eric.funnymod.event.events;

import hk.eric.funnymod.event.EventCancellable;
import net.minecraft.network.chat.Component;

public class TitleEvents extends EventCancellable {

    private Component component;
    private final TitleType type;

    private TitleEvents(Component component, TitleType type) {
        this.component = component;
        this.type = type;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public TitleType getType() {
        return type;
    }

    public static class TitleEvent extends TitleEvents {
        public TitleEvent(Component component) {
            super(component, TitleType.TITLE);
        }
    }

    public static class SubtitleEvent extends TitleEvents {
        public SubtitleEvent(Component component) {
            super(component, TitleType.SUBTITLE);
        }
    }

    public enum TitleType {
        TITLE,
        SUBTITLE
    }
}
