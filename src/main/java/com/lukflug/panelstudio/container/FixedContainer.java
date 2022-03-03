package com.lukflug.panelstudio.container;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.base.Description;
import com.lukflug.panelstudio.base.IInterface;
import com.lukflug.panelstudio.base.IToggleable;
import com.lukflug.panelstudio.component.IFixedComponent;
import com.lukflug.panelstudio.config.IConfigList;
import com.lukflug.panelstudio.config.IPanelConfig;
import com.lukflug.panelstudio.popup.IPopup;
import com.lukflug.panelstudio.popup.IPopupDisplayer;
import com.lukflug.panelstudio.popup.IPopupPositioner;
import com.lukflug.panelstudio.setting.ILabeled;
import com.lukflug.panelstudio.theme.IContainerRenderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * Container with contents arranged at will.
 * @author lukflug
 */
public class FixedContainer extends Container<IFixedComponent> implements IPopupDisplayer {
	/**
	 * Whether to clip container.
	 */
	protected final boolean clip;
	/**
	 * List of static pop-ups to be displayed.
	 */
	protected final List<PopupPair> popups= new ArrayList<>();
	
	/**
	 * Constructor.
	 * @param label the label for the component
	 * @param renderer the renderer for this container
	 * @param clip whether to clip container
	*/
	public FixedContainer(ILabeled label, IContainerRenderer renderer, boolean clip) {
		super(label, renderer);
		this.clip=clip;
	}

	@Override
	public void displayPopup (IPopup popup, Rectangle rect, IToggleable visible, IPopupPositioner positioner) {
		popups.add(new PopupPair(popup, rect, visible, positioner));
	}
	
	@Override
	public void render (Context context) {
		// Set context height
		context.setHeight(getHeight());
		// Clip rectangle
		if (clip) context.getInterface().window(context.getRect());
		if (renderer!=null) renderer.renderBackground(context,context.hasFocus());
		// Find the highest component
		AtomicReference<IFixedComponent> highest= new AtomicReference<>(null);
		AtomicReference<IFixedComponent> first= new AtomicReference<>(null);
		doContextlessLoop(component->{
			if (first.get()==null) first.set(component);
			Context subContext=getSubContext(context,component,true,true);
			component.getHeight(subContext);
			if (subContext.isHovered() && highest.get()==null) highest.set(component);
		});
		// Render loop in right order (the lowest panel first)
		AtomicBoolean highestReached=new AtomicBoolean(false);
		if (highest.get()==null) highestReached.set(true);
		AtomicReference<IFixedComponent> focusComponent= new AtomicReference<>(null);
		super.doContextlessLoop(component->{
			// Check onTop state
			if (component==highest.get()) highestReached.set(true);
			// Render component
			Context subContext=getSubContext(context,component,component==first.get(),highestReached.get());
			component.render(subContext);
			// Check focus state
			if (subContext.focusReleased()) context.releaseFocus();
			else if (subContext.focusRequested()) {
				focusComponent.set(component);
				context.requestFocus();
			}
			// Check description state
			if (subContext.isHovered() && subContext.getDescription()!=null) context.setDescription(new Description(subContext.getDescription(),subContext.getRect()));
			// Deal with popups
			for (PopupPair popup: popups) {
				popup.popup.setPosition(context.getInterface(),popup.rect,subContext.getRect(),popup.positioner);
				if (!popup.visible.isOn()) popup.visible.toggle();
				if (popup.popup instanceof IFixedComponent) focusComponent.set((IFixedComponent)popup.popup);
			}
			popups.clear();
		});
		// Update focus state
		if (focusComponent.get()!=null) {
			if (removeComponent(focusComponent.get())) addComponent(focusComponent.get());
		}
		// Use container description, if necessary
		if (context.getDescription()==null && label.getDescription()!=null) context.setDescription(new Description(context.getRect(),label.getDescription()));
		// Restore clipping
		if (clip) context.getInterface().restore();
	}
	
	@Override
	protected void doContextlessLoop (Consumer<IFixedComponent> function) {
		List<ComponentState> components = new ArrayList<>(this.components);
		for (ComponentState state: components) state.update();
		for (int i=components.size()-1;i>=0;i--) {
			ComponentState state=components.get(i);
			if (state.lastVisible()) function.accept(state.component);
		}
	}

	@Override
	protected void doContextSensitiveLoop (Context context, ContextSensitiveConsumer<IFixedComponent> function) {
		// Set context height
		context.setHeight(getHeight());
		// Do loop in inverse order
		AtomicBoolean highest=new AtomicBoolean(true);
		AtomicBoolean first=new AtomicBoolean(true);
		AtomicReference<IFixedComponent> focusComponent= new AtomicReference<>(null);
		doContextlessLoop(component->{
			// Do payload operation
			Context subContext=getSubContext(context,component,first.get(),highest.get());
			first.set(false);
			function.accept(subContext,component);
			// Check focus state
			if (subContext.focusReleased()) context.releaseFocus();
			else if (subContext.focusRequested()) {
				focusComponent.set(component);
				context.requestFocus();
			}
			// Check onTop state
			if (subContext.isHovered()) highest.set(false);
			// Deal with popups
			for (PopupPair popup: popups) {
				popup.popup.setPosition(context.getInterface(),popup.rect,subContext.getRect(),popup.positioner);
				if (!popup.visible.isOn()) popup.visible.toggle();
				if (popup.popup instanceof IFixedComponent) focusComponent.set((IFixedComponent)popup.popup);
			}
			popups.clear();
		});
		// Update focus state
		if (focusComponent.get()!=null) {
			ComponentState focusState=components.stream().filter(state->state.component==focusComponent.get()).findFirst().orElse(null);
			if (focusState!=null) {
				components.remove(focusState);
				components.add(focusState);
			}
		}
	}
	
	/**
	 * Create sub-context for child component.
	 * @param context the current context
	 * @param component the component
	 * @param focus whether this component has focus within container
	 * @param highest whether this component is the highest
	 * @return the context for the child component
	 */
	protected Context getSubContext (Context context, IFixedComponent component, boolean focus, boolean highest) {
		Context subContext=new Context(context,component.getWidth(context.getInterface()),component.getPosition(context.getInterface()),context.hasFocus()&&focus,highest);
		subContext.setPopupDisplayer(this);
		return subContext;
	}
	
	/**
	 * Store the GUI state.
	 * @param inter the interface to be used
	 * @param config the configuration list to be used
	 */
	public void saveConfig (IInterface inter, IConfigList config) {
		config.begin(false);
		for (ComponentState state: components) {
			if (state.component.savesState()) {
				IPanelConfig cf=config.addPanel(state.component.getConfigName());
				if (cf!=null) state.component.saveConfig(inter,cf);
			}
		}
		config.end(false);
	}
	
	/**
	 * Load the GUI state.
	 * @param inter the interface to be used
	 * @param config the configuration list to be used
	 */
	public void loadConfig (IInterface inter, IConfigList config) {
		config.begin(true);
		for (ComponentState state: components) {
			if (state.component.savesState()) {
				IPanelConfig cf=config.getPanel(state.component.getConfigName());
				if (cf!=null) state.component.loadConfig(inter,cf);
			}
		}
		config.end(true);
	}


	/**
	 * A tuple containing all the information to display a pop-up.
	 *
	 * @author lukflug
	 */
	private record PopupPair(IPopup popup, Rectangle rect,
							   IToggleable visible,
							   IPopupPositioner positioner) {
		/**
		 * Constructor.
		 *
		 * @param popup      value for {@link #popup}
		 * @param rect       value for {@link #rect}
		 * @param visible    value for {@link #visible}
		 * @param positioner value for {@link #positioner}
		 */
		private PopupPair {
		}
	}
}
