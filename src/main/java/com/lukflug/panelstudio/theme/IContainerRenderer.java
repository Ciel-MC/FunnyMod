package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;

/**
 * Interface to describe the appearance of a container.
 * @author lukflug
 */
public interface IContainerRenderer {
	/**
	 * Render the container background.
	 * @param context the context of the container
	 * @param focus whether this container has focus
	 */
	default void renderBackground(Context context, boolean focus) {
	}
	
	/**
	 * Get the border between two components.
	 * @return the border
	 */
	default int getBorder() {
		return 0;
	}
	
	/**
	 * Get left border.
	 * @return the left border
	 */
	default int getLeft() {
		return 0;
	}
	
	/**
	 * Get right border.
	 * @return the right border
	 */
	default int getRight() {
		return 0;
	}
	
	/**
	 * Get top border.
	 * @return the top border
	 */
	default int getTop() {
		return 0;
	}
	
	/**
	 * Get bottom border.
	 * @return the bottom border
	 */
	default int getBottom() {
		return 0;
	}
}
