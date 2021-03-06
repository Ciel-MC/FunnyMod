package com.lukflug.panelstudio.widget;

import com.lukflug.panelstudio.setting.IColorSetting;
import com.lukflug.panelstudio.theme.ThemeTuple;
import hk.eric.ericLib.utils.classes.getters.Getter;

/**
 * Full color picker with field and slider.
 * @author lukflug
 */
public class ColorPickerComponent extends ColorComponent {
	/**
	 * Constructor.
	 * @param setting the setting in question
	 * @param theme the theme to be used
	 */
	public ColorPickerComponent (IColorSetting setting, ThemeTuple theme) {
		super(setting,theme);
	}

	@Override
	public void populate (ThemeTuple theme) {
		addComponent(new ToggleButton(new RainbowToggle(), Getter.fixed(theme.getButtonRenderer(Boolean.class,false))));
		addComponent(new ColorPicker(setting,theme.theme.getColorPickerRenderer()));
		addComponent(new NumberSlider(new ColorNumber(0), Getter.fixed(theme.getSliderRenderer(false))));
		addComponent(new NumberSlider(new ColorNumber(3), Getter.fixed(theme.getSliderRenderer(false))));
	}

}
