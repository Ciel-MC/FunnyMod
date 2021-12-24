package hk.eric.funnymod.modules;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.funnymod.event.EventHandler;
import hk.eric.funnymod.event.EventManager;
import hk.eric.funnymod.exceptions.ConfigLoadingFailedException;

import java.util.ArrayList;
import java.util.List;

public abstract class ToggleableModule extends Module implements Toggleable {

    private final List<EventHandler<?>> onOffHandlers = new ArrayList<>();
    private boolean enabled;

    public ToggleableModule(String displayName, String description) {
        this(displayName, description, false);
    }

    public ToggleableModule(String displayName, String description, boolean enabled) {
        super(displayName, description);
        this.enabled = enabled;
        if (enabled) onEnable();
    }

    public ToggleableModule(String displayName, String description, IBoolean visible) {
        this(displayName, description, visible, false);
    }

    public ToggleableModule(String displayName, String description, IBoolean visible, boolean enabled) {
        super(displayName, description, visible);
        this.enabled = enabled;
        if (enabled) onEnable();
    }

    @Override
    public IToggleable getToggleable() {
        return new IToggleable() {
            @Override
            public void toggle() {
                ToggleableModule.this.toggle();
            }

            @Override
            public boolean isOn() {
                return ToggleableModule.this.isOn();
            }
        };
    }

    @Override
    public void toggle() {
        setEnabled(!getEnabled());
    }

    @Override
    public boolean isOn() {
        return getEnabled();
    }

    protected void registerOnOffHandler(EventHandler<?> eventHandler) {
        onOffHandlers.add(eventHandler);
    }

    private boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        if (enabled != this.enabled) {
            this.enabled = enabled;
            if (enabled) {
                onEnable();
            } else {
                onDisable();
            }
        }
    }

    public void onEnable() {
        onOffHandlers.forEach(EventManager.getInstance()::register);
    }

    public void onDisable() {
        onOffHandlers.forEach(EventManager.getInstance()::unregister);
    }

    @Override
    public ObjectNode save() {
        return super.save().put("enabled", getEnabled());
    }

    @Override
    public void load(ObjectNode node) throws ConfigLoadingFailedException {
        super.load(node);
        setEnabled(node.get("enabled").asBoolean());
    }
}
