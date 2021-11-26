package com.lukflug.panelstudio.component;

import java.awt.Point;
import java.awt.Rectangle;

import com.lukflug.panelstudio.base.IInterface;
import com.lukflug.panelstudio.config.IPanelConfig;
import com.lukflug.panelstudio.popup.IPopupPositioner;

/**
 * Combination of {@link IComponentProxy} and {@link IFixedComponent}.
 * @author lukflug
 * @param <T> the component type
 */
@FunctionalInterface
public interface IFixedComponentProxy<T extends IFixedComponent> extends IComponentProxy<T>,IFixedComponent {
	@Override
    default Point getPosition(IInterface inter) {
		return getComponent().getPosition(inter);
	}
	
	@Override
    default void setPosition(IInterface inter, Point position) {
		getComponent().setPosition(inter,position);
	}
	
	@Override
    default void setPosition(IInterface inter, Rectangle component, Rectangle panel, IPopupPositioner positioner) {
		getComponent().setPosition(inter,component,panel,positioner);
	}
	
	@Override
    default int getWidth(IInterface inter) {
		return getComponent().getWidth(inter);
	}
	
	@Override
    default boolean savesState() {
		return getComponent().savesState();
	}
	
	@Override
    default void saveConfig(IInterface inter, IPanelConfig config) {
		getComponent().saveConfig(inter,config);
	}
	
	@Override
    default void loadConfig(IInterface inter, IPanelConfig config) {
		getComponent().loadConfig(inter,config);
	}
	
	@Override
    default String getConfigName() {
		return getComponent().getConfigName();
	}
}
