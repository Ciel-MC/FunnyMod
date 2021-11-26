package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;

/**
 * Proxy redirecting calls.
 * @author lukflug
 */
@FunctionalInterface
public interface IContainerRendererProxy extends IContainerRenderer {
	@Override
    default void renderBackground(Context context, boolean focus) {
		getRenderer().renderBackground(context,focus);
	}
	
	@Override
    default int getBorder() {
		return getRenderer().getBorder();
	}
	
	@Override
    default int getLeft() {
		return getRenderer().getLeft();
	}
	
	@Override
    default int getRight() {
		return getRenderer().getRight();
	}
	
	@Override
    default int getTop() {
		return getRenderer().getTop();
	}
	
	@Override
    default int getBottom() {
		return getRenderer().getBottom();
	}

	/**
	 * The renderer to be redirected to.
	 * @return the renderer
	 */
    IContainerRenderer getRenderer();
}
