package hk.eric.funnymod.modules;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.funnymod.exceptions.ConfigLoadingFailedException;

public abstract class ToggleableModule extends Module implements CanToggle {

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
    public IToggleable isEnabled() {
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

    private boolean getEnabled() {
        return enabled;
    }

    public void onEnable(){}

    public void onDisable(){}

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
