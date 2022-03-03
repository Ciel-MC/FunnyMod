package com.lukflug.panelstudio.popup;

import com.lukflug.panelstudio.component.IScrollSize;

/**
 * Data structure used to reduce argument count.
 * Describes what type of pop-up a certain layout should use.
 *
 * @author lukflug
 */
public record PopupTuple(IPopupPositioner popupPos, boolean dynamicPopup,
						 IScrollSize popupSize) {
	/**
	 * Constructor.
	 *
	 * @param popupPos     the value for {@link #popupPos}
	 * @param dynamicPopup the value for {@link #dynamicPopup}
	 * @param popupSize    the value for {@link #popupSize}
	 */
	public PopupTuple {
	}
}
