package com.lukflug.panelstudio.layout;

import com.lukflug.panelstudio.base.*;
import com.lukflug.panelstudio.component.ComponentProxy;
import com.lukflug.panelstudio.component.IComponent;
import com.lukflug.panelstudio.component.IFixedComponent;
import com.lukflug.panelstudio.container.VerticalContainer;
import com.lukflug.panelstudio.popup.PopupTuple;
import com.lukflug.panelstudio.setting.ILabeled;
import com.lukflug.panelstudio.setting.Labeled;
import com.lukflug.panelstudio.theme.RendererTuple;
import com.lukflug.panelstudio.theme.ThemeTuple;
import com.lukflug.panelstudio.widget.Button;
import com.lukflug.panelstudio.widget.ClosableComponent;
import hk.eric.ericLib.utils.classes.getters.Getter;

import java.util.function.Supplier;

/**
 * Utility to add child components.
 *
 * @author lukflug
 */
public record ChildUtil(int width, Supplier<Animation> animation,
						PopupTuple popupType) {
	/**
	 * Constructor.
	 *
	 * @param width     pop-up width
	 * @param animation animation supplier
	 * @param popupType pop-up type
	 */
	public ChildUtil {
	}

	/**
	 * Add a child container.
	 *
	 * @param <T>        the render state type
	 * @param label      the container label
	 * @param title      the title component
	 * @param container  the container itself
	 * @param state      the render state supplier
	 * @param stateClass the render state class
	 * @param parent     the parent component
	 * @param gui        the component adder for pop-ups
	 * @param theme      the theme to be used
	 * @param mode       the child mode to be used
	 */
	public <T> void addContainer(ILabeled label, IComponent title, IComponent container, Supplier<T> state, Class<T> stateClass, VerticalContainer parent, IComponentAdder gui, ThemeTuple theme, ChildMode mode) {
		IFixedComponent popup;
		IToggleable toggle;
		boolean drawTitle = mode == ChildMode.DRAG_POPUP;
		switch (mode) {
			case DOWN -> parent.addComponent(new ClosableComponent<>(title, container, state, getAnimatedToggleable(animation.get()), theme.getPanelRenderer(stateClass), false));
			case POPUP, DRAG_POPUP -> {
				toggle = new SimpleToggleable(false);
				Button<T> button = new Button<>(new Labeled(label.getDisplayName(), label.getDescription(), () -> drawTitle && label.isVisible().isOn()), state, Getter.fixed(theme.getButtonRenderer(stateClass, true)));
				if (popupType.dynamicPopup())
					popup = ClosableComponent.createDynamicPopup(button, container, state, animation.get(), new RendererTuple<>(stateClass, theme), popupType.popupSize(), toggle, width);
				else
					popup = ClosableComponent.createStaticPopup(button, container, state, animation.get(), new RendererTuple<>(stateClass, theme), popupType.popupSize(), toggle, () -> width, false, "", false);
				parent.addComponent(new ComponentProxy<>(title) {
					@Override
					public void handleButton(Context context, int button) {
						super.handleButton(context, button);
						if (button == IInterface.RBUTTON && context.isClicked(button)) {
							context.getPopupDisplayer().displayPopup(popup, context.getRect(), toggle, popupType.popupPos());
							context.releaseFocus();
						}
					}
				});
				gui.addPopup(popup);
			}
		}
	}

	/**
	 * Get animated toggleable.
	 *
	 * @param animation the animation to be used
	 * @return the animated toggleable to be used
	 */
	private AnimatedToggleable getAnimatedToggleable(Animation animation) {
		return new AnimatedToggleable(new SimpleToggleable(false), animation);
	}


	/**
	 * Enum listing the ways a child component can be added.
	 *
	 * @author lukflug
	 */
	public enum ChildMode {
		/**
		 * Component is added as a closable component with title bar.
		 */
		DOWN,
		/**
		 * Component is added as button that shows pop-up.
		 */
		POPUP,
		/**
		 * Component is added as button that shows draggable pop-up with title bar.
		 */
		DRAG_POPUP
	}
}
