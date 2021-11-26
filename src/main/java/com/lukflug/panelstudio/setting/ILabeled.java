package com.lukflug.panelstudio.setting;

import com.lukflug.panelstudio.base.IBoolean;

/**
 * Represent object with label and description.
 * @author lukflug
 */
@FunctionalInterface
public interface ILabeled {
	/**
	 * Get display name of the object.
	 * @return the display name
	 */
    String getDisplayName();
	
	/**
	 * Get object description.
	 * @return the object description
	 */
	default String getDescription() {
		return null;
	}
	
	/**
	 * Returns boolean interface indicating whether the object is visible.
	 * @return the visibility of the setting
	 */
	default IBoolean isVisible() {
		return ()->true;
	}
}
