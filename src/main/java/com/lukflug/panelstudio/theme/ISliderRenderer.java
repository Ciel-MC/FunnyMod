package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;

import java.awt.*;

/**
 * Interface abstracting the rendering of a slider.
 * @author lukflug
 */
public interface ISliderRenderer {
	/**
	 * Render a slider.
	 * @param context the context to be used
	 * @param title the title of the slider
	 * @param state the display state of the slider
	 * @param focus the focus state of the slider
	 * @param value the value of the slider (between 0 and 1)
	 */
    void renderSlider(Context context, String title, String state, boolean focus, double value);
	
	/**
	 * Returns the default height.
	 * @return the default height
	 */
    int getDefaultHeight();
	
	/**
	 * Get the slidable area.
	 * @param context the context to be used
	 * @param title the title of the slider
	 * @param state the display state of the slider
	 * @return the rectangle representing the area that can be slided
	 */
	default Rectangle getSlideArea(Context context, String title, String state) {
		return context.getRect();
	}
}
