package hk.eric.funnymod.gui.setting;

import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.setting.INumberSetting;
import hk.eric.funnymod.utils.Constants;
import hk.eric.funnymod.utils.classes.Converters;
import hk.eric.funnymod.utils.classes.TwoWayFunction;

import java.util.function.Consumer;

public class DoubleSetting extends SavableSetting<Double> implements INumberSetting<Double> {
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
		setValue(Math.round(value/step)*step);
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
	public Double getStep() {
		return step;
	}

	@Override
	public double getStepAsDouble() {
		return step;
	}

	@Override
	public Class<Double> getSettingClass() {
		return Double.class;
	}

	@Override
	public TwoWayFunction<Double, String> getConverter() {
		return Converters.DOUBLE_CONVERTER;
	}
}
