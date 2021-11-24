package hk.eric.funnymod.modules;

import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.base.IToggleable;

public abstract class ToggleableModule extends Module implements CanToggle {

    private boolean enabled;

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
        if(enabled != this.enabled) {
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
}
