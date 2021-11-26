package com.lukflug.panelstudio.setting;

import com.lukflug.panelstudio.base.IToggleable;

/**
 * Interface representing boolean setting.
 * @author lukflug
 */
public interface IBooleanSetting extends ISetting<Boolean>,IToggleable {
	@Override
	default Boolean getSettingState() {
		return isOn();
	}
	
	@Override
	default Class<Boolean> getSettingClass() {
		return Boolean.class;
	}
}
