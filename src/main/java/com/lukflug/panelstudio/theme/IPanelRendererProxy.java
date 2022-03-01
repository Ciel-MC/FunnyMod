package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;

/**
 * Proxy redirecting calls.
 * @author lukflug
 * @param <T> type representing state of the panel
 */
@FunctionalInterface
public interface IPanelRendererProxy<T> extends IPanelRenderer<T> {
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
	
	@Override
    default void renderPanelOverlay(Context context, boolean focus, T state, boolean open) {
		getRenderer().renderPanelOverlay(context,focus,state,open);
	}
	
	@Override
    default void renderTitleOverlay(Context context, boolean focus, T state, boolean open) {
		getRenderer().renderTitleOverlay(context,focus,state,open);
	}

	/**
	 * The renderer to be redirected to.
	 * @return the renderer
	 */
    IPanelRenderer<T> getRenderer();
}
