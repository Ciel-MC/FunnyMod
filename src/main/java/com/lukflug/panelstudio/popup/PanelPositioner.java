package com.lukflug.panelstudio.popup;

import com.lukflug.panelstudio.base.IInterface;

import java.awt.*;

/**
 * Static pop-up positioner, that positions the pop-up on the side of the panel.
 *
 * @author lukflug
 */
public record PanelPositioner(Point offset) implements IPopupPositioner {
	/**
	 * Constructor.
	 *
	 * @param offset the offset relative to the current cursor position
	 */
	public PanelPositioner {
	}

	@Override
	public Point getPosition(IInterface inter, Dimension popup, Rectangle component, Rectangle panel) {
		return new Point(panel.x + panel.width + offset.x, component.y + offset.y);
	}
}
