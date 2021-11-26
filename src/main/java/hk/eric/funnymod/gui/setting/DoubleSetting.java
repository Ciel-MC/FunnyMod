package hk.eric.funnymod.gui.setting;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.setting.INumberSetting;
import hk.eric.funnymod.utils.Constants;
import hk.eric.funnymod.utils.ObjectUtil;
import hk.eric.funnymod.utils.classes.TwoWayFunction;

import java.util.function.Consumer;

public class DoubleSetting extends SavableSettingWithChild<Double> implements INumberSetting<Double> {
	public final double min,max,step;

	public DoubleSetting(String displayName, String configName, String description, double min, double max, double value) {
		this(displayName, configName, description, Constants.alwaysTrue, min, max, value);
	}

	public DoubleSetting(String displayName, String configName, String description, IBoolean visible, double min, double max, double value) {
		this(displayName, configName, description, visible, min, max, value, null);
	}

	public DoubleSetting(String displayName, String configName, String description, double min, double max, double value, Consumer<Double> onChange) {
		this(displayName, configName, description, Constants.alwaysTrue, min, max, value, onChange);
	}

	public DoubleSetting(String displayName, String configName, String description, IBoolean visible, double min, double max, double value, Consumer<Double> onChange) {
		this(displayName, configName, description, visible, min, max, value, 0.01, onChange);
	}

	public DoubleSetting(String displayName, String configName, String description, double min, double max, double value, double step) {
		this(displayName, configName, description, Constants.alwaysTrue, min, max, value, step);
	}

	public DoubleSetting(String displayName, String configName, String description, IBoolean visible, double min, double max, double value, double step) {
		this(displayName, configName, description, visible, min, max, value, step, null);
	}

	public DoubleSetting(String displayName, String configName, String description, double min, double max, double value, double step, Consumer<Double> onChange) {
		this(displayName, configName, description, Constants.alwaysTrue, min, max, value, step, onChange);
	}

	public DoubleSetting(String displayName, String configName, String description, IBoolean visible, double min, double max, double value, double step, Consumer<Double> onChange) {
		super(displayName,configName,description,visible,value,onChange);
		this.min=min;
		this.max=max;
		this.step=step;
	}

	@Override
	public double getNumber() {
		return getValue();
	}

	@Override
	public void setNumber (double value) {
		double originalValue = getNumber();
		double change = value - originalValue;
		setValue(originalValue + (change - change%step));
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
		return 2;
	}

	public double getStep() {
		return step;
	}

	@Override
	public void setValue(Double value) {

	}

	@Override
	public Class<Double> getSettingClass() {
		return null;
	}

	@Override
	public TwoWayFunction<Double, String> getConverter() {
		return null;
	}

	@Override
	public ObjectNode saveThis() {
		return ObjectUtil.getObjectNode().put("value",getValue());
	}

	@Override
	public void loadThis(ObjectNode node) {
		setValue(node.get("value").asDouble());
	}
}
