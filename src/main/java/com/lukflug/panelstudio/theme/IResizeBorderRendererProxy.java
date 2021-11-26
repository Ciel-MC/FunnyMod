package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;

/**
 * Proxy redirecting calls
 * @author lukflug
 */
@FunctionalInterface
public interface IResizeBorderRendererProxy extends IResizeBorderRenderer {
	@Override
    default void drawBorder(Context context, boolean focus) {
		getRenderer().drawBorder(context,focus);
	}
	
	@Override
    default int getBorder() {
		return getRenderer().getBorder();
	}
	
	/**
	 * The renderer to be redirected to.
	 * @return the renderer
	 */
    IResizeBorderRenderer getRenderer();
}
