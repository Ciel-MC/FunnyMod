package com.lukflug.panelstudio.widget;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.component.FocusableComponent;
import com.lukflug.panelstudio.setting.ILabeled;
import com.lukflug.panelstudio.theme.IButtonRenderer;
import hk.eric.funnymod.utils.classes.getters.Getter;

import java.util.function.Supplier;

/**
 * Button widget class.
 * @author lukflug
 */
public class Button<T> extends FocusableComponent {
	/**
	 * The button state supplier.
	 */
	protected final Supplier<T> state;
	/**
	 * Renderer for this component.
	 */
	protected final Getter<IButtonRenderer<T>> rendererGetter;

	/**
	 * Constructor.
	 * @param label the label for the component
	 * @param state the button state supplier
	 * @param rendererGetter the renderer for this component
	 */
	public Button (ILabeled label, Supplier<T> state, Getter<IButtonRenderer<T>> rendererGetter) {
		super(label);
		this.rendererGetter = rendererGetter;
		this.state=state;
	}
	
	@Override
	public void render (Context context) {
		super.render(context);
		rendererGetter.get().renderButton(context,getTitle(),hasFocus(context),state.get());
	}

	@Override
	protected int getHeight() {
		return rendererGetter.get().getDefaultHeight();
	}
}
