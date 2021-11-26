package com.lukflug.panelstudio.theme;

import java.awt.Color;
import java.awt.Point;

import com.lukflug.panelstudio.base.Context;

/**
 * Proxy redirecting calls
 * @author lukflug
 */
@FunctionalInterface
public interface IColorPickerRendererProxy extends IColorPickerRenderer {
	@Override
    default void renderPicker(Context context, boolean focus, Color color) {
		getRenderer().renderPicker(context,focus,color);
	}
	
	@Override
    default Color transformPoint(Context context, Color color, Point point) {
		return getRenderer().transformPoint(context,color,point);
	}
	
	@Override
    default int getDefaultHeight(int width) {
		return getRenderer().getDefaultHeight(width);
	}
	
	/**
	 * The renderer to be redirected to.
	 * @return the renderer
	 */
    IColorPickerRenderer getRenderer();
}
