package com.lukflug.panelstudio.popup;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import com.lukflug.panelstudio.base.IInterface;

/**
 * Static pop-up positioner that positions the pop-up at a fixed position.
 *
 * @author lukflug
 */
public record FixedPositioner(Point pos) implements IPopupPositioner {
	/**
	 * Constructor.
	 *
	 * @param pos the position of the pop-up.
	 */
	public FixedPositioner {
	}

	@Override
	public Point getPosition(IInterface inter, Dimension popup, Rectangle component, Rectangle panel) {
		return new Point(pos);
	}
}
