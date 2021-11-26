package com.lukflug.panelstudio.setting;

import com.fasterxml.jackson.databind.node.ObjectNode;
import hk.eric.funnymod.exceptions.ConfigLoadingFailedException;

import java.util.stream.Stream;

/**
 * Interface representing a single setting.
 * @author lukflug
 */
public interface ISetting<T> extends ILabeled {
	/**
	 * Get the current setting value.
	 * @return the setting state
	 */
	T getSettingState();
	
	/**
	 * Returns the class object of corresponding to the type returned by {@link #getSettingState()}.
	 * @return the settings class
	 */
	Class<T> getSettingClass();
	
	/**
	 * Returns sub settings.
	 * @return sub-settings
	 */
	default Stream<ISetting<?>> getSubSettings() {
		return null;
	}

	default ObjectNode save() {
		ObjectNode node = saveThis();
		getSubSettings().forEach(setting -> node.set(setting.getDisplayName(), setting.save()));
		return node;
	}

	default void load(ObjectNode node) throws ConfigLoadingFailedException {
		loadThis(node);
		for (ISetting<?> setting : getSubSettings().toList()) {
			setting.load((ObjectNode) node.get(setting.getDisplayName()));
		}
	}

	ObjectNode saveThis();

	void loadThis(ObjectNode node) throws ConfigLoadingFailedException;
}
