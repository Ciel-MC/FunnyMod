package hk.eric.funnymod.gui.setting;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.setting.Savable;
import hk.eric.funnymod.utils.ObjectUtil;

import java.util.function.Consumer;

public abstract class SavableSetting<T> extends Setting<T> implements Savable<T> {

    private final String configName;

    public SavableSetting(String displayName, String configName, String description, T value) {
        super(displayName, description, value);
        this.configName = configName;
    }

    public SavableSetting(String displayName, String configName, String description, IBoolean visible, T value) {
        super(displayName, description, visible, value);
        this.configName = configName;
    }

    public SavableSetting(String displayName, String configName, String description, T value, Consumer<T> onChange) {
        super(displayName, description, value, onChange);
        this.configName = configName;
    }

    public SavableSetting(String displayName, String configName, String description, IBoolean visible, T value, Consumer<T> onChange) {
        super(displayName, description, visible, value, onChange);
        this.configName = configName;
    }

    @Override
    public ObjectNode save() {
        return saveThis();
    }

    @Override
    public void load(ObjectNode node) {
        loadThis(node);
    }

    @Override
    public String getValueName() {
        return getConverter().convert(getValue());
    }

    @Override
    public ObjectNode saveThis() {
        return ObjectUtil.getObjectNode().put("value", getConverter().convert(getValue()));
    }

    @Override
    public void loadThis(ObjectNode node) {
        setValue(getConverter().revert(node.get("value").asText()));
    }

    @Override
    public String getConfigName() {
        return configName;
    }
}
