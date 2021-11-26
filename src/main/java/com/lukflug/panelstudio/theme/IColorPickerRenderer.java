package com.lukflug.panelstudio.theme;

import java.awt.Color;
import java.awt.Point;

import com.lukflug.panelstudio.base.Context;

/**
 * Interface abstracting the rendering of a color picker.
 * @author lukflug
 */
public interface IColorPickerRenderer {
	/**
	 * Renders the color picker.
	 * @param context the current context
	 * @param focus the focus state
	 * @param color the currently selected color
	 */
    void renderPicker(Context context, boolean focus, Color color);
	
	/**
	 * Map a point on the color picker to a color.
	 * @param context the current context
	 * @param color the currently selected color
	 * @param point the point to be mapped
	 * @return the color corresponding to the point on screen
	 */
    Color transformPoint(Context context, Color color, Point point);
	
	/**
	 * Returns the default height.
	 * @param width the width of the color picker
	 * @return the default height
	 */
    int getDefaultHeight(int width);
}
