package com.lukflug.panelstudio.setting;

import com.lukflug.panelstudio.base.Animation;

@SuppressWarnings("rawtypes")
public record AnimatedEnum(IEnumSetting setting,
						   Animation animation) {

	public double getValue() {
		int index = setting.getValueIndex();
		if (animation.getTarget() != index) animation.setValue(index);
		return animation.getValue();
	}
}
