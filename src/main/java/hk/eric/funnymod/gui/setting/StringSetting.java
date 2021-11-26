package hk.eric.funnymod.gui.setting;

import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.setting.IStringSetting;
import hk.eric.funnymod.utils.Constants;

public class StringSetting extends Setting<String> implements IStringSetting {
	public StringSetting(String displayName, String configName, String description, String value) {
		this(displayName, configName, description, Constants.alwaysTrue, value);
	}

	public StringSetting (String displayName, String configName, String description, IBoolean visible, String value) {
		super(displayName,configName,description,visible,value);
	}

	@Override
	public String getValueName() {
		return getValue();
	}
}
