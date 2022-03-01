package com.lukflug.panelstudio.setting;

import java.awt.Color;

/**
 * Setting representing a color.
 * @author lukflug
 */
public interface IColorSetting extends ISetting<Color> {
	/**
	 * Get the current value for the color setting.
	 * @return the current color
	 */
    Color getValue();
	
	/**
	 * Set the non-rainbow color.
	 * @param value the value
	 */
    void setValue(Color value);
	
	/**
	 * Get the color, ignoring the rainbow.
	 * @return the color ignoring the rainbow
	 */
    Color getColor();
	
	/**
	 * Check if rainbow is enabled.
	 * @return set, if the rainbow is enabled
	 */
    boolean getRainbow();
	
	/**
	 * Enable or disable the rainbow.
	 * @param rainbow set, if rainbow should be enabled
	 */
    void setRainbow(boolean rainbow);
	
	/**
	 * Returns whether setting should have alpha slider
	 * @return whether alpha is enabled
	 */
	default boolean hasAlpha() {
		return false;
	}
	
	/**
	 * Returns whether setting has rainbow option.
	 * @return whether setting allows rainbow
	 */
	default boolean allowsRainbow() {
		return true;
	}
	
	/**
	 * Returns true for HSB model, false for RGB model
	 * @return returns whether HSB model should be used
	 */
	default boolean hasHSBModel() {
		return false;
	}
	
	@Override
    default Color getSettingState() {
		return getValue();
	}
	
	@Override
    default Class<Color> getSettingClass() {
		return Color.class;
	}
}
