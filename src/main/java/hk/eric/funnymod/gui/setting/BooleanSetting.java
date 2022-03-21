package hk.eric.funnymod.gui.setting;

import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.setting.IBooleanSetting;
import hk.eric.ericLib.utils.classes.Converters;
import hk.eric.ericLib.utils.classes.TwoWayFunction;

import java.util.function.Consumer;

public class BooleanSetting extends SavableSetting<Boolean> implements IBooleanSetting {

	public BooleanSetting(String displayName, String configName, String description, Boolean value) {
		super(displayName, configName, description, value);
	}

	public BooleanSetting(String displayName, String configName, String description, IBoolean visible, Boolean value) {
		super(displayName, configName, description, visible, value);
	}

	public BooleanSetting(String displayName, String configName, String description, Boolean value, Consumer<Boolean> onChange) {
		super(displayName, configName, description, value, onChange);
	}

	public BooleanSetting(String displayName, String configName, String description, IBoolean visible, Boolean value, Consumer<Boolean> onChange) {
		super(displayName, configName, description, visible, value, onChange);
	}

	@Override
	public void toggle() {
		setValue(!getValue());
	}

	@Override
	public boolean isOn() {
		return getValue();
	}

	@Override
	public TwoWayFunction<Boolean, String> getConverter() {
		return Converters.BOOLEAN_CONVERTER;
	}
}
