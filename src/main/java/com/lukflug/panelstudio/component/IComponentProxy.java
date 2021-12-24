package com.lukflug.panelstudio.component;

import java.util.function.Consumer;

import com.lukflug.panelstudio.base.Context;

/**
 * Proxy redirecting calls to other component.
 * @author lukflug
 * @param <T> the component type
 */
@FunctionalInterface
public interface IComponentProxy<T extends IComponent> extends IComponent {
	@Override
	default String getTitle() {
		return getComponent().getTitle();
	}

	@Override
	default void render(Context context) {
		doOperation(context,getComponent()::render);
	}

	@Override
	default void handleButton(Context context, int button) {
		doOperation(context,subContext->getComponent().handleButton(subContext,button));
	}

	@Override
	default void handleKey(Context context, int scancode) {
		doOperation(context,subContext->getComponent().handleKey(subContext,scancode));
	}
	
	@Override
	default void handleChar(Context context, char character) {
		doOperation(context,subContext->getComponent().handleChar(subContext,character));
	}

	@Override
	default void handleScroll(Context context, int diff) {
		doOperation(context,subContext->getComponent().handleScroll(subContext,diff));
	}

	@Override
	default void getHeight(Context context) {
		doOperation(context,getComponent()::getHeight);
	}

	@Override
	default void enter() {
		getComponent().enter();
	}

	@Override
	default void exit() {
		getComponent().exit();
	}

	@Override
	default void releaseFocus() {
		getComponent().releaseFocus();
	}

	@Override
	default boolean isVisible() {
		return getComponent().isVisible();
	}
	
	/**
	 * Returns the current component being redirected.
	 * @return the component
	 */
	T getComponent();

	/**
	 * Perform a context-sensitive operation.
	 * @param context the context to use
	 * @param operation the operation to perform
	 * @return the sub-context of the component
	 */
	default Context doOperation(Context context, Consumer<Context> operation) {
		Context subContext=getContext(context);
		operation.accept(subContext);
		if (subContext!=context) {
			if (subContext.focusReleased()) context.releaseFocus();
			else if (subContext.focusRequested()) context.requestFocus();
			context.setHeight(getHeight(subContext.getSize().height));
			if (subContext.getDescription()!=null) context.setDescription(subContext.getDescription());
		}
		return subContext;
	}
	
	/**
	 * Function to determine visible height.
	 * @param height the component height
	 * @return the visible height
	 */
	default int getHeight(int height) {
		return height;
	}
	
	/**
	 * Get the context for the content component.
	 * @param context the parent context
	 * @return the child context
	 */
	default Context getContext(Context context) {
		return context;
	}
}
