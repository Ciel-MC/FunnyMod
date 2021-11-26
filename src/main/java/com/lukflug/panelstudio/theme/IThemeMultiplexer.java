package com.lukflug.panelstudio.theme;

import java.awt.Color;

import com.lukflug.panelstudio.base.IInterface;

/**
 * Base class used to enable switch themes "on-the-fly".
 * It provides the renderers for the components.
 * In this way, the renderers can effectively be switched, without changing the field in the components itself.
 * @author lukflug
 */
@FunctionalInterface
public interface IThemeMultiplexer extends ITheme {
	@Override
    default void loadAssets(IInterface inter) {
		getTheme().loadAssets(inter);
	}

	@Override
    default IDescriptionRenderer getDescriptionRenderer() {
		return (IDescriptionRendererProxy) ()->getTheme().getDescriptionRenderer();
	}

	@Override
    default IContainerRenderer getContainerRenderer(int logicalLevel, int graphicalLevel, boolean horizontal) {
		return (IContainerRendererProxy) ()->getTheme().getContainerRenderer(logicalLevel,graphicalLevel,horizontal);
	}
	
	@Override
    default <T> IPanelRenderer<T> getPanelRenderer(Class<T> type, int logicalLevel, int graphicalLevel) {
		return (IPanelRendererProxy<T>) ()->getTheme().getPanelRenderer(type,logicalLevel,graphicalLevel);
	}
	
	@Override
    default <T> IScrollBarRenderer<T> getScrollBarRenderer(Class<T> type, int logicalLevel, int graphicalLevel) {
		return (IScrollBarRendererProxy<T>) ()->getTheme().getScrollBarRenderer(type,logicalLevel,graphicalLevel);
	}
	
	@Override
    default <T> IEmptySpaceRenderer<T> getEmptySpaceRenderer(Class<T> type, int logicalLevel, int graphicalLevel, boolean container) {
		return (IEmptySpaceRendererProxy<T>) ()->getTheme().getEmptySpaceRenderer(type,logicalLevel,graphicalLevel,container);
	}

	@Override
    default <T> IButtonRenderer<T> getButtonRenderer(Class<T> type, int logicalLevel, int graphicalLevel, boolean container) {
		return (IButtonRendererProxy<T>) ()->getTheme().getButtonRenderer(type,logicalLevel,graphicalLevel,container);
	}
	
	@Override
    default IButtonRenderer<Void> getSmallButtonRenderer(int symbol, int logicalLevel, int graphicalLevel, boolean container) {
		return (IButtonRendererProxy<Void>) ()->getTheme().getSmallButtonRenderer(symbol,logicalLevel,graphicalLevel,container);
	}

	@Override
    default IButtonRenderer<String> getKeybindRenderer(int logicalLevel, int graphicalLevel, boolean container) {
		return (IButtonRendererProxy<String>) ()->getTheme().getKeybindRenderer(logicalLevel,graphicalLevel,container);
	}

	@Override
    default ISliderRenderer getSliderRenderer(int logicalLevel, int graphicalLevel, boolean container) {
		return (ISliderRendererProxy) ()->getTheme().getSliderRenderer(logicalLevel,graphicalLevel,container);
	}
	
	@Override
    default IRadioRenderer getRadioRenderer(int logicalLevel, int graphicalLevel, boolean container) {
		return (IRadioRendererProxy) ()->getTheme().getRadioRenderer(logicalLevel,graphicalLevel,container);
	}
	
	@Override
    default IResizeBorderRenderer getResizeRenderer() {
		return (IResizeBorderRendererProxy) ()->getTheme().getResizeRenderer();
	}
	
	@Override
    default ITextFieldRenderer getTextRenderer(boolean embed, int logicalLevel, int graphicalLevel, boolean container) {
		return (ITextFieldRendererProxy) ()->getTheme().getTextRenderer(embed,logicalLevel,graphicalLevel,container);
	}
	
	@Override
    default ISwitchRenderer<Boolean> getToggleSwitchRenderer(int logicalLevel, int graphicalLevel, boolean container) {
		return (ISwitchRendererProxy<Boolean>) ()->getTheme().getToggleSwitchRenderer(logicalLevel,graphicalLevel,container);
	}
	
	@Override
    default ISwitchRenderer<String> getCycleSwitchRenderer(int logicalLevel, int graphicalLevel, boolean container) {
		return (ISwitchRendererProxy<String>) ()->getTheme().getCycleSwitchRenderer(logicalLevel,graphicalLevel,container);
	}
	
	@Override
    default IColorPickerRenderer getColorPickerRenderer() {
		return (IColorPickerRendererProxy) ()->getTheme().getColorPickerRenderer();
	}

	@Override
    default int getBaseHeight() {
		return getTheme().getBaseHeight();
	}

	@Override
    default Color getMainColor(boolean focus, boolean active) {
		return getTheme().getMainColor(focus,active);
	}

	@Override
    default Color getBackgroundColor(boolean focus) {
		return getTheme().getBackgroundColor(focus);
	}

	@Override
    default Color getFontColor(boolean focus) {
		return getTheme().getFontColor(focus);
	}

	@Override
    default void overrideMainColor(Color color) {
		getTheme().overrideMainColor(color);
	}

	@Override
    default void restoreMainColor() {
		getTheme().restoreMainColor();
	}
	
	/**
	 * Abstract method that returns the current theme.
	 * @return the current theme
	 */
    ITheme getTheme();
}
