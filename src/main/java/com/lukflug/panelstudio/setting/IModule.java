package com.lukflug.panelstudio.setting;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.funnymod.exceptions.ConfigLoadingFailedException;

import java.util.stream.Stream;

/**
 * Interface representing a module.
 * @author lukflug
 */
public interface IModule extends ILabeled {
	/**
	 * Returns a toggleable indicating whether the module is enabled, which may be null.
	 * @return whether the module is enabled
	 */
	IToggleable getToggleable();
	
	/**
	 * Get list of settings in module.
	 * @return stream of settings
	 */
	Stream<ISetting<?>> getSettings();

	ObjectNode save();

	void load(ObjectNode node) throws ConfigLoadingFailedException;
}
