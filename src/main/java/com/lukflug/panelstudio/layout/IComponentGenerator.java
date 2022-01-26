package com.lukflug.panelstudio.layout;

import com.lukflug.panelstudio.base.Animation;
import com.lukflug.panelstudio.base.SimpleToggleable;
import com.lukflug.panelstudio.component.IComponent;
import com.lukflug.panelstudio.setting.*;
import com.lukflug.panelstudio.theme.ThemeTuple;
import com.lukflug.panelstudio.widget.*;
import hk.eric.funnymod.utils.classes.getters.CachedLambdaGetter;
import hk.eric.funnymod.utils.classes.getters.Getter;

import java.util.function.Supplier;

/**
 * Interface defining what components to use for settings.
 * @author lukflug
 */
public interface IComponentGenerator {
	/**
	 * Get component from a given setting object.
	 * @param setting the setting object.
	 * @param animation the animation supplier
	 * @param adder the component adder for any pop-ups
	 * @param theme the theme to be used
	 * @param colorLevel the panel nesting level, in case the component is a container (e.g. color components)
	 * @param isContainer whether this component is a title bar
	 * @return the component to be used
	 */
	@SuppressWarnings("rawtypes")
	default IComponent getComponent(ISetting<?> setting, Supplier<Animation> animation, IComponentAdder adder, ThemeTuple theme, int colorLevel, Getter<Boolean> isContainer) {
		System.out.println("Generating component for " + setting.getClass().getSimpleName());
		if (setting instanceof IBooleanSetting) {
			return getBooleanComponent((IBooleanSetting)setting,animation,adder,theme,colorLevel,isContainer);
		} else if (setting instanceof INumberSetting) {
			return getNumberComponent((INumberSetting)setting,animation,adder,theme,colorLevel,isContainer);
		} else if (setting instanceof IEnumSetting) {
			return getEnumComponent((IEnumSetting)setting,animation,adder,theme,colorLevel,isContainer);
		} else if (setting instanceof IColorSetting) {
			return getColorComponent((IColorSetting)setting,animation,adder,theme,colorLevel,isContainer);
		} else if (setting instanceof IKeybindSetting) {
			return getKeybindComponent((IKeybindSetting)setting,animation,adder,theme,colorLevel,isContainer);
		} else if (setting instanceof IStringSetting) {
			return getStringComponent((IStringSetting)setting,animation,adder,theme,colorLevel,isContainer);
		} else {
			return new Button<>(setting, () -> null, new CachedLambdaGetter<>(bl -> theme.getButtonRenderer(String.class, bl), isContainer, 2).toGetter());
		}
	}

	/**
	 * Get component from a given boolean setting object.
	 * @param setting the setting object.
	 * @param animation the animation supplier
	 * @param adder the component adder for any pop-ups
	 * @param theme the theme to be used
	 * @param colorLevel the panel nesting level, in case the component is a container (e.g. color components)
	 * @param isContainer whether this component is a title bar
	 * @return the component to be used
	 */
	default IComponent getBooleanComponent(IBooleanSetting setting, Supplier<Animation> animation, IComponentAdder adder, ThemeTuple theme, int colorLevel, Getter<Boolean> isContainer) {
		return new ToggleButton(setting, new CachedLambdaGetter<>(bl -> theme.getButtonRenderer(Boolean.class, bl), isContainer, 2).toGetter());
	}

	/**
	 * Get component from a given number setting object.
	 * @param setting the setting object.
	 * @param animation the animation supplier
	 * @param adder the component adder for any pop-ups
	 * @param theme the theme to be used
	 * @param colorLevel the panel nesting level, in case the component is a container (e.g. color components)
	 * @param isContainer whether this component is a title bar
	 * @return the component to be used
	 */
	@SuppressWarnings("rawtypes")
	default IComponent getNumberComponent(INumberSetting setting, Supplier<Animation> animation, IComponentAdder adder, ThemeTuple theme, int colorLevel, Getter<Boolean> isContainer) {
		return new NumberSlider(setting,new CachedLambdaGetter<>(theme::getSliderRenderer, isContainer, 2).toGetter());
	}

	/**
	 * Get component from a given enum setting object.
	 * @param setting the setting object.
	 * @param animation the animation supplier
	 * @param adder the component adder for any pop-ups
	 * @param theme the theme to be used
	 * @param colorLevel the panel nesting level, in case the component is a container (e.g. color components)
	 * @param isContainer whether this component is a title bar
	 * @return the component to be used
	 */
	@SuppressWarnings("rawtypes")
	default IComponent getEnumComponent(IEnumSetting setting, Supplier<Animation> animation, IComponentAdder adder, ThemeTuple theme, int colorLevel, Getter<Boolean> isContainer) {
		return new CycleButton(setting,new CachedLambdaGetter<>(str -> theme.getButtonRenderer(String.class, str), isContainer, 2).toGetter());
	}

	/**
	 * Get component from a given color setting object.
	 * @param setting the setting object.
	 * @param animation the animation supplier
	 * @param adder the component adder for any pop-ups
	 * @param theme the theme to be used
	 * @param colorLevel the panel nesting level, in case the component is a container (e.g. color components)
	 * @param isContainer whether this component is a title bar
	 * @return the component to be used
	 */
	default IComponent getColorComponent(IColorSetting setting, Supplier<Animation> animation, IComponentAdder adder, ThemeTuple theme, int colorLevel, Getter<Boolean> isContainer) {
		return new ColorSliderComponent(setting,new ThemeTuple(theme.theme,theme.logicalLevel,colorLevel));
	}

	/**
	 * Get component from a given keybind setting object.
	 * @param setting the setting object.
	 * @param animation the animation supplier
	 * @param adder the component adder for any pop-ups
	 * @param theme the theme to be used
	 * @param colorLevel the panel nesting level, in case the component is a container (e.g. color components)
	 * @param isContainer whether this component is a title bar
	 * @return the component to be used
	 */
	default IComponent getKeybindComponent(IKeybindSetting setting, Supplier<Animation> animation, IComponentAdder adder, ThemeTuple theme, int colorLevel, Getter<Boolean> isContainer) {
		return new KeybindComponent(setting, new CachedLambdaGetter<>(theme::getKeybindRenderer, isContainer, 2).toGetter());
	}

	/**
	 * Get component from a given string setting object.
	 * @param setting the setting object.
	 * @param animation the animation supplier
	 * @param adder the component adder for any pop-ups
	 * @param theme the theme to be used
	 * @param colorLevel the panel nesting level, in case the component is a container (e.g. color components)
	 * @param isContainer whether this component is a title bar
	 * @return the component to be used
	 */
	default IComponent getStringComponent(IStringSetting setting, Supplier<Animation> animation, IComponentAdder adder, ThemeTuple theme, int colorLevel, Getter<Boolean> isContainer) {
		return new TextField(setting, new ITextFieldKeys() {
			@Override
			public boolean isBackspaceKey (int scancode) {
				return false;
			}

			@Override
			public boolean isDeleteKey (int scancode) {
				return false;
			}

			@Override
			public boolean isInsertKey (int scancode) {
				return false;
			}

			@Override
			public boolean isLeftKey (int scancode) {
				return false;
			}

			@Override
			public boolean isRightKey (int scancode) {
				return false;
			}

			@Override
			public boolean isHomeKey (int scancode) {
				return false;
			}

			@Override
			public boolean isEndKey (int scancode) {
				return false;
			}

			@Override
			public boolean isCopyKey (int scancode) {
				return false;
			}

			@Override
			public boolean isPasteKey (int scancode) {
				return false;
			}

			@Override
			public boolean isCutKey (int scancode) {
				return false;
			}

			@Override
			public boolean isAllKey (int scancode) {
				return false;
			}
		},0,new SimpleToggleable(false), new CachedLambdaGetter<>(bl -> theme.getTextRenderer(false, bl), isContainer, 2).toGetter()) {
			@Override
			public boolean allowCharacter (char character) {
				return false;
			}
		};
	}
}
