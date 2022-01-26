package com.lukflug.panelstudio.widget;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.base.IInterface;
import com.lukflug.panelstudio.component.FocusableComponent;
import com.lukflug.panelstudio.setting.IEnumSetting;
import com.lukflug.panelstudio.theme.IButtonRenderer;
import hk.eric.funnymod.utils.classes.getters.Getter;

/**
 * Component representing an enumeration-valued setting which cycles.
 * @author lukflug
 */
public class CycleButton extends FocusableComponent {
	/**
	 * The setting in question.
	 */
	@SuppressWarnings("rawtypes")
    protected final IEnumSetting setting;
	/**
	 * The renderer to be used.
	 */
	protected final Getter<IButtonRenderer<String>> rendererGetter;
	
	/**
	 * Constructor.
	 * @param setting the setting in question
	 * @param rendererGetter the renderer for this component
	 */
	@SuppressWarnings("rawtypes")
    public CycleButton (IEnumSetting setting, Getter<IButtonRenderer<String>> rendererGetter) {
		super(setting);
		this.setting=setting;
		this.rendererGetter = rendererGetter;
	}

	@Override
	public void render (Context context) {
		super.render(context);
		rendererGetter.get().renderButton(context,getTitle(),hasFocus(context),setting.getValueName());
	}
	
	@Override
	public void handleButton (Context context, int button) {
		super.handleButton(context,button);
		if (button==IInterface.LBUTTON && context.isClicked(button)) {
			setting.increment();
		}
	}

	@Override
	protected int getHeight() {
		return rendererGetter.get().getDefaultHeight();
	}
}
