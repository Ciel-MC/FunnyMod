package com.lukflug.panelstudio.setting;

/**
 * Setting representing an adjustable number.
 * @author lukflug
 */
public interface INumberSetting<E extends Number> extends ISetting<E> {
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


	E getStep();

	double getStepAsDouble();
	
	@Override
    Class<E> getSettingClass();
}
