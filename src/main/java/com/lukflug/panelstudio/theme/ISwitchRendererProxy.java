package com.lukflug.panelstudio.theme;

import java.awt.Rectangle;

import com.lukflug.panelstudio.base.Context;

/**
 * Proxy redirecting calls
 * @author lukflug
 * @param <T> type representing the state of the switch
 */
@FunctionalInterface
public interface ISwitchRendererProxy<T> extends ISwitchRenderer<T>,IButtonRendererProxy<T> {
	@Override
    default Rectangle getOnField(Context context) {
		return getRenderer().getOnField(context);
	}
	
	@Override
    default Rectangle getOffField(Context context) {
		return getRenderer().getOffField(context);
	}
	
	@Override
    ISwitchRenderer<T> getRenderer();
}
