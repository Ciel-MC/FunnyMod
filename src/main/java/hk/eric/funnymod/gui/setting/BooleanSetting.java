package hk.eric.funnymod.gui.setting;

import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.setting.IBooleanSetting;

import java.util.function.Consumer;

public class BooleanSetting extends Setting<Boolean> implements IBooleanSetting {
	public BooleanSetting (String displayName, String configName, String description, IBoolean visible, Boolean value) {
		super(displayName,configName,description,visible,value);
	}

	public BooleanSetting (String displayName, String configName, String description, IBoolean visible, Boolean value, Consumer<Boolean> onChange) {
		super(displayName,configName,description,visible,value, onChange);
	}

	@Override
	public void toggle() {
		setValue(!getValue());
	}

	@Override
	public boolean isOn() {
		return getValue();
	}
}
