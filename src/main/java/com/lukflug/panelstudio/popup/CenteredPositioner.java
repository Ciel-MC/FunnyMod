package com.lukflug.panelstudio.popup;

import com.lukflug.panelstudio.base.IInterface;

import java.awt.*;
import java.util.function.Supplier;

/**
 * Dynamic pop-up positioner that displays the pop-up centered around in a given rectangle.
 *
 * @author lukflug
 */
public record CenteredPositioner(Supplier<Rectangle> rect) implements IPopupPositioner {
	/**
	 * Constructor.
	 *
	 * @param rect the rectangle supplier for centering
	 */
	public CenteredPositioner {
	}

	@Override
	public Point getPosition(IInterface inter, Dimension popup, Rectangle component, Rectangle panel) {
		Rectangle rect = this.rect.get();
		return new Point(rect.x + rect.width / 2 - popup.width / 2, rect.y + rect.height / 2 - popup.height / 2);
	}
}
