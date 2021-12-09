package hk.eric.funnymod.gui.setting;

import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.setting.INumberSetting;
import com.lukflug.panelstudio.setting.ISetting;
import hk.eric.funnymod.utils.classes.Converters;
import hk.eric.funnymod.utils.classes.TwoWayFunction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class IntegerSetting extends SavableSetting<Integer> implements INumberSetting<Integer> {
	public final int min,max,step;

	private final Map<Integer,List<ISetting<?>>> subSettings = new HashMap<>();

	public IntegerSetting(String displayName, String configName, String description, int min, int max, Integer value) {
		this(displayName, configName, description, min, max, value, 1);
	}

	public IntegerSetting(String displayName, String configName, String description, int min, int max, Integer value, int step) {
		super(displayName, configName, description, value);
		this.min = min;
		this.max = max;
		this.step = step;
	}

	public IntegerSetting(String displayName, String configName, String description, IBoolean visible, int min, int max, Integer value) {
		this(displayName, configName, description, visible, min, max, value, 1);
	}

	public IntegerSetting(String displayName, String configName, String description, IBoolean visible, int min, int max, Integer value, int step) {
		super(displayName, configName, description, visible, value);
		this.min = min;
		this.max = max;
		this.step = step;
	}

	public IntegerSetting(String displayName, String configName, String description, int min, int max, Integer value, Consumer<Integer> onChange) {
		this(displayName, configName, description, min, max, value, onChange, 1);
	}

	public IntegerSetting(String displayName, String configName, String description, int min, int max, Integer value, Consumer<Integer> onChange, int step) {
		super(displayName, configName, description, value, onChange);
		this.min = min;
		this.max = max;
		this.step = step;
	}

	public IntegerSetting(String displayName, String configName, String description, IBoolean visible, int min, int max, Integer value, Consumer<Integer> onChange) {
		this(displayName, configName, description, visible, min, max, value, onChange, 1);
	}
	public IntegerSetting(String displayName, String configName, String description, IBoolean visible, int min, int max, Integer value, Consumer<Integer> onChange, int step) {
		super(displayName, configName, description, visible, value, onChange);
		this.min = min;
		this.max = max;
		this.step = step;
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
	public Integer getStep() {
		return step;
	}

	@Override
	public double getStepAsDouble() {
		return getStep();
	}

	@Override
	public Class<Integer> getSettingClass() {
		return Integer.class;
	}

	@Override
	public TwoWayFunction<Integer, String> getConverter() {
		return Converters.INTEGER_CONVERTER;
	}
}
