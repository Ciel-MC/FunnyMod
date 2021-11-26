package com.lukflug.panelstudio.setting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Arrays;

/**
 * A setting representing an enumeration.
 * @author lukflug
 */
public interface IEnumSetting extends ISetting<String> {
	/**
	 * Cycle through the values of the enumeration.
	 */
	void increment();
	
	/**
	 * Cycle through the values of the enumeration in inverse order
	 */
	void decrement();
	
	/**
	 * Get the current value.
	 * @return the name of the current enum value
	 */
	String getValueName();

	void fromString(String value);
	
	/**
	 * Get a sequential number of the current enum state.
	 * @return the index of the current value
	 */
	default int getValueIndex() {
		ILabeled[] stuff =getAllowedValues();
		String compare=getValueName();
		for (int i=0;i<stuff.length;i++) {
			if (stuff[i].getDisplayName().equals(compare)) return i; 
		}
		return -1;
	}
	
	/**
	 * Set the current enum state by sequential number per {@link #getValueIndex()}.
	 * @param index the new value index
	 */
	void setValueIndex(int index);
	
	/**
	 * Get a list of allowed enum states.
	 * @return list of enum values
	 */
	ILabeled[] getAllowedValues();
	
	@Override
	default String getSettingState() {
		return getValueName();
	}
	
	@Override
	default Class<String> getSettingClass() {
		return String.class;
	}
	
	/**
	 * Get a list of enum values that are visible.
	 * @param setting the enum setting in question
	 * @return list of visible enum values
	 */
	static ILabeled[] getVisibleValues(IEnumSetting setting) {
		return Arrays.stream(setting.getAllowedValues()).filter(value->value.isVisible().isOn()).toArray(ILabeled[]::new);
	}

	@Override
	default ObjectNode saveThis() {
		return new ObjectMapper().createObjectNode().put("value", getValueName());
	}

	@Override
	default void loadThis(ObjectNode node) {
		fromString(node.get("value").asText());
	}
}
