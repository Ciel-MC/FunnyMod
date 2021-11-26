package com.lukflug.panelstudio.setting;

/**
 * Setting representing an adjustable number.
 * @author lukflug
 */
public interface INumberSetting extends ISetting<String> {
	/**
	 * Get the number as double.
	 * @return the current setting
	 */
    double getNumber();
	
	/**
	 * Set the number.
	 * @param value the new number
	 */
    void setNumber(double value);
	
	/**
	 * Get the maximum allowed value for the setting.
	 * @return maximum value
	 */
    double getMaximumValue();
	
	/**
	 * Get the minimum allowed value for the setting.
	 * @return minimum value
	 */
    double getMinimumValue();
	

	/**
	 * Get the setting's precision.
	 * @return decimal precision
	 */
    int getPrecision();
	
	@Override
    default String getSettingState() {
		if (getPrecision()==0) return ""+(int)getNumber();
		else return String.format("%."+getPrecision()+"f",getNumber());
	}
	
	@Override
    default Class<String> getSettingClass() {
		return String.class;
	}
}
