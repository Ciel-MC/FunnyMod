package com.lukflug.panelstudio.popup;

import com.lukflug.panelstudio.base.IInterface;

import java.awt.*;

/**
 * Static pop-up positioner that positions the pop-up at a fixed position relative to the mouse pointer.
 *
 * @author lukflug
 */
public record MousePositioner(Point offset) implements IPopupPositioner {
	/**
	 * Constructor.
	 *
	 * @param offset the offset relative to the current cursor position
	 */
	public MousePositioner {
	}

	@Override
	public Point getPosition(IInterface inter, Dimension popup, Rectangle component, Rectangle panel) {
		Point pos = inter.getMouse();
		pos.translate(offset.x, offset.y);
		return pos;
	}
}
