package com.lukflug.panelstudio.layout;

import com.lukflug.panelstudio.base.Animation;
import com.lukflug.panelstudio.base.SimpleToggleable;
import com.lukflug.panelstudio.component.IComponent;
import com.lukflug.panelstudio.setting.IKeybindSetting;
import com.lukflug.panelstudio.setting.IStringSetting;
import com.lukflug.panelstudio.theme.ThemeTuple;
import com.lukflug.panelstudio.widget.ITextFieldKeys;
import com.lukflug.panelstudio.widget.KeybindComponent;
import com.lukflug.panelstudio.widget.TextField;
import hk.eric.funnymod.utils.classes.getters.CachedLambdaGetter;
import hk.eric.funnymod.utils.classes.getters.Getter;

import java.util.function.IntPredicate;
import java.util.function.Supplier;

/**
 * Default implementation of the component generator.
 * @author lukflug
 */
public class ComponentGenerator implements IComponentGenerator {
	/**
	 * The scancode predicate for delete keybind key.
	 */
	protected final IntPredicate keybindKey;
	/**
	 * The text field character filter predicate. 
	 */
	protected final IntPredicate charFilter;
	/**
	 * The text field function key predicates.
	 */
	protected final ITextFieldKeys keys;
	
	/**
	 * Constructor.
	 * @param keybindKey the scancode predicate for delete keybind key
	 * @param charFilter the text field character filter predicate
	 * @param keys the text field function key predicates
	 */
	public ComponentGenerator (IntPredicate keybindKey, IntPredicate charFilter, ITextFieldKeys keys) {
		this.keybindKey=keybindKey;
		this.charFilter=charFilter;
		this.keys=keys;
	}
	
	@Override
	public IComponent getKeybindComponent (IKeybindSetting setting, Supplier<Animation> animation, IComponentAdder adder, ThemeTuple theme, int colorLevel, Getter<Boolean> isContainer) {
		return new KeybindComponent(setting, new CachedLambdaGetter<>(theme::getKeybindRenderer, isContainer, 2).toGetter()) {
			@Override
			public int transformKey (int scancode) {
				return keybindKey.test(scancode)?0:scancode;
			}
		};
	}
	
	@Override
	public IComponent getStringComponent (IStringSetting setting, Supplier<Animation> animation, IComponentAdder adder, ThemeTuple theme, int colorLevel, Getter<Boolean> isContainer) {
		return new TextField(setting,keys,0,new SimpleToggleable(false), new CachedLambdaGetter<>(bl -> theme.getTextRenderer(false, bl), isContainer, 2).toGetter()) {
			@Override
			public boolean allowCharacter(char character) {
				return charFilter.test(character);
			}
		};
	}
}
