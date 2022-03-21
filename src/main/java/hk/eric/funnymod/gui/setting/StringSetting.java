package hk.eric.funnymod.gui.setting;

import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.setting.IStringSetting;
import hk.eric.ericLib.utils.classes.Converters;
import hk.eric.ericLib.utils.classes.TwoWayFunction;
import hk.eric.funnymod.utils.Constants;

public class StringSetting extends SavableSetting<String> implements IStringSetting {
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

	@Override
	public TwoWayFunction<String, String> getConverter() {
		return Converters.STRING_CONVERTER;
	}
}
