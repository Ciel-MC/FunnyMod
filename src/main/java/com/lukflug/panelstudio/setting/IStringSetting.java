package com.lukflug.panelstudio.setting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Setting representing a text.
 * @author lukflug
 */
public interface IStringSetting extends ISetting<String> {
	/**
	 * Get current string value.
	 * @return the current text
	 */
	String getValue();
	
	/**
	 * Set the string value.
	 * @param string new text
	 */
	void setValue(String string);

	@Override
	default String getSettingState() {
		return getValue();
	}

	@Override
	default Class<String> getSettingClass() {
		return String.class;
	}

	@Override
	default ObjectNode saveThis() {
		return new ObjectMapper().createObjectNode().put("value", getValue());
	}

	@Override
	default void loadThis(ObjectNode node) {
		setValue(node.get("value").asText());
	}
}
