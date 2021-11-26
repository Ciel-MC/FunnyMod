package com.lukflug.panelstudio.setting;

import java.util.Arrays;

/**
 * A setting representing an enumeration.
 * @author lukflug
 */
public interface IEnumSetting<E extends Enum<E>> extends ISetting<E> {
	/**
	 * Cycle through the values of the enumeration.
	 */
	void increment();
	
	/**
	 * Cycle through the values of the enumeration in inverse order
	 */
	void decrement();
	
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
	Class<E> getSettingClass();
	
	/**
	 * Get a list of enum values that are visible.
	 * @param setting the enum setting in question
	 * @return list of visible enum values
	 */
	static ILabeled[] getVisibleValues(IEnumSetting setting) {
		return Arrays.stream(setting.getAllowedValues()).filter(value->value.isVisible().isOn()).toArray(ILabeled[]::new);
	}
}
