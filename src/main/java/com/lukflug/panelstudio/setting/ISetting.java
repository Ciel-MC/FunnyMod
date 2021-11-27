package com.lukflug.panelstudio.setting;

import com.lukflug.panelstudio.base.IBoolean;

/**
 * Interface representing a single setting.
 * @author lukflug
 */
public interface ISetting<T> extends ILabeled {

	T getValue();

	void setValue(T value);

	default T getSettingState() {
		return getValue();
	}

	String getDisplayName();

	String getDescription();

	IBoolean isVisible();
	
	/**
	 * Returns the class object of corresponding to the type returned by {@link #getValue()}.
	 * @return the settings class
	 */
	Class<T> getSettingClass();

	/**
	 * Get the current value.
	 * @return the name of the current enum value
	 */
	default String getValueName() {
		return getValue().toString();
	}


}
