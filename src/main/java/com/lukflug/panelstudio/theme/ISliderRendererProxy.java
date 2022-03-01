package com.lukflug.panelstudio.theme;

import java.awt.Rectangle;

import com.lukflug.panelstudio.base.Context;

/**
 * Proxy redirecting calls.
 * @author lukflug
 */
@FunctionalInterface
public interface ISliderRendererProxy extends ISliderRenderer {
	@Override
    default void renderSlider(Context context, String title, String state, boolean focus, double value) {
		getRenderer().renderSlider(context,title,state,focus,value);
	}

	@Override
    default int getDefaultHeight() {
		return getRenderer().getDefaultHeight();
	}

	@Override
    default Rectangle getSlideArea(Context context, String title, String state) {
		return getRenderer().getSlideArea(context,title,state);
	}

	/**
	 * The renderer to be redirected to.
	 * @return the renderer
	 */
    ISliderRenderer getRenderer();
}
