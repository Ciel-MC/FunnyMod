package com.lukflug.panelstudio.setting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

	@Override
	default ObjectNode saveThis() {
		return new ObjectMapper().createObjectNode().put("value", getSettingState());
	}

	@Override
	default void loadThis(ObjectNode node) {
		if (getSettingState() != node.get("value").asBoolean()) {
			toggle();
		}
	}
}
