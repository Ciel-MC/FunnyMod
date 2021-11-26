package hk.eric.funnymod.gui.setting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.setting.INumberSetting;

import java.util.function.Consumer;

public class IntegerSetting extends Setting<Integer> implements INumberSetting {
	public final int min,max;

	public IntegerSetting(String displayName, String configName, String description, IBoolean visible, Integer min, Integer max, Integer value) {
		this(displayName, configName, description, visible, min, max, value, null);
	}

	public IntegerSetting(String displayName, String configName, String description, IBoolean visible, Integer min, Integer max, Integer value, Consumer<Integer> onChange) {
		super(displayName, configName, description, visible, value, onChange);
		this.min = min;
		this.max = max;
	}


	@Override
	public double getNumber() {
		return getValue();
	}

	@Override
	public void setNumber (double value) {
		setValue((int)Math.round(value));
	}

	@Override
	public double getMaximumValue() {
		return max;
	}

	@Override
	public double getMinimumValue() {
		return min;
	}

	@Override
	public int getPrecision() {
		return 0;
	}

	@Override
	public ObjectNode saveThis() {
		return new ObjectMapper().createObjectNode().put("value", getValue());
	}

	@Override
	public void loadThis(ObjectNode node) {
		setValue(node.get("value").asInt());
	}
}
