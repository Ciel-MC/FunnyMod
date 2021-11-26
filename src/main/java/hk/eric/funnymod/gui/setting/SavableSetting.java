package hk.eric.funnymod.gui.setting;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.setting.Savable;
import hk.eric.funnymod.exceptions.ConfigLoadingFailedException;
import hk.eric.funnymod.utils.ObjectUtil;
import hk.eric.funnymod.utils.classes.TwoWayFunction;

import java.util.function.Consumer;

public abstract class SavableSetting<T> extends Setting<T> implements Savable<T> {
    public SavableSetting(String displayName, String configName, String description, T value) {
        super(displayName, configName, description, value);
    }

    public SavableSetting(String displayName, String configName, String description, IBoolean visible, T value) {
        super(displayName, configName, description, visible, value);
    }

    public SavableSetting(String displayName, String configName, String description, T value, Consumer<T> onChange) {
        super(displayName, configName, description, value, onChange);
    }

    public SavableSetting(String displayName, String configName, String description, IBoolean visible, T value, Consumer<T> onChange) {
        super(displayName, configName, description, visible, value, onChange);
    }

    @Override
    public TwoWayFunction<T, String> getConverter() {
        return null;
    }

    @Override
    public ObjectNode save() {
        return ObjectUtil.getObjectNode().put("value", getConverter().convert(getValue()));
    }

    @Override
    public void load(ObjectNode node) throws ConfigLoadingFailedException {
        setValue(getConverter().revert(node.get("value").asText()));
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
    public void loadThis(ObjectNode node) throws ConfigLoadingFailedException {
        setValue(getConverter().revert(node.get("value").asText()));
    }
}
