package com.lukflug.panelstudio.config;

import java.awt.Dimension;
import java.awt.Point;

/**
 * Interface representing a single panel configuration state.
 * @author lukflug
 */
public interface IPanelConfig {
	/**
	 * Store the position of the panel.
	 * @param position the current position of the panel
	 */
    void savePositon(Point position);
	
	/**
	 * Store the size of the panel.
	 * @param size the current size of the panel
	 */
    void saveSize(Dimension size);
	
	/**
	 * Load the position of the panel.
	 * @return the stored position
	 */
    Point loadPosition();
	
	/**
	 * Load the size of the panel.
	 * @return the store size
	 */
    Dimension loadSize();
	
	/**
	 * Store the state of the panel.
	 * @param state whether the panel is open
	 */
    void saveState(boolean state);
	
	/**
	 * Load the state of the panel.
	 * @return the stored panel state
	 */
    boolean loadState();
}
