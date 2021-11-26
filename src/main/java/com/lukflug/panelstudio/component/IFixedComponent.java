package com.lukflug.panelstudio.component;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.base.IInterface;
import com.lukflug.panelstudio.config.IPanelConfig;
import com.lukflug.panelstudio.popup.IPopup;
import com.lukflug.panelstudio.popup.IPopupPositioner;

/**
 * Interface representing a {@link Component} that has a fixed position
 * (i.e. the position isn't determined by the parent via {@link Context}).
 * @author lukflug
 */
public interface IFixedComponent extends IComponent,IPopup {
	/**
	 * Get the current component position.
	 * @param inter current interface
	 * @return current position
	 */
    Point getPosition(IInterface inter);
	
	/**
	 * Set the current component position.
	 * @param inter current interface
	 * @param position new position
	 */
    void setPosition(IInterface inter, Point position);
	
	@Override
    default void setPosition(IInterface inter, Rectangle component, Rectangle panel, IPopupPositioner positioner) {
		setPosition(inter,positioner.getPosition(inter,null,component,panel));
	}
	
	/**
	 * Get the component width.
	 * @param inter current interface
	 * @return component width
	 */
    int getWidth(IInterface inter);
	
	/**
	 * Returns whether this component allows its state to be saved.
	 * @return true, if this component saves its state
	 */
    boolean savesState();
	
	/**
	 * Saves the component state
	 * @param inter current interface
	 * @param config configuration to use
	 */
    void saveConfig(IInterface inter, IPanelConfig config);
	
	/**
	 * Loads the component state
	 * @param inter current interface
	 * @param config configuration to use
	 */
    void loadConfig(IInterface inter, IPanelConfig config);
	
	/**
	 * Returns the name to identify the component for saving position and size.
	 * @return the config name of the component
	 */
    String getConfigName();
}
