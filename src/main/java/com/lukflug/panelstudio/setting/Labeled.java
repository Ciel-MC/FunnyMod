package com.lukflug.panelstudio.setting;

import com.lukflug.panelstudio.base.IBoolean;

/**
 * Simple implementation of the ILabeled interface.
 * @author lukflug
 */
public class Labeled implements ILabeled {
	/**
	 * The name of the label.
	 */
	protected final String title;
	/**
	 * The description.
	 */
	protected final String description;
	/**
	 * The visibility.
	 */
	protected final IBoolean visible;

	/**
	 * Constructor.
	 * @param title the title of the label
	 * @param description the description
	 * @param visible the visibility
	 */
	public Labeled (String title, String description, IBoolean visible) {
		this.title=title;
		this.description=description;
		this.visible=visible;
	}
	
	@Override
	public String getDisplayName() {
		return title;
	}
	
	@Override
	public String getDescription() {
		return description;
	}
	
	@Override
	public IBoolean isVisible() {
		return visible;
	}
}
