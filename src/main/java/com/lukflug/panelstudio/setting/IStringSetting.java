package com.lukflug.panelstudio.setting;

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
	default Class<String> getSettingClass() {
		return String.class;
	}
}
