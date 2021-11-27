package hk.eric.funnymod.gui.setting;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.setting.IColorSetting;
import com.lukflug.panelstudio.theme.ITheme;
import hk.eric.funnymod.modules.ClickGUIModule;
import hk.eric.funnymod.modules.ClickGUIModule.ColorModel;
import hk.eric.funnymod.utils.Constants;
import hk.eric.funnymod.utils.ObjectUtil;
import hk.eric.funnymod.utils.classes.TwoWayFunction;

import java.awt.*;

public class ColorSetting extends SavableSetting<Color> implements IColorSetting {
	private boolean hasAlpha,allowsRainbow;
	private boolean rainbow;

	public ColorSetting(String displayName, String configName, String description, boolean hasAlpha, boolean allowsRainbow, Color value, boolean rainbow) {
		this(displayName, configName, description, Constants.alwaysTrue, hasAlpha, allowsRainbow, value, rainbow);
	}

	public ColorSetting (String displayName, String configName, String description, IBoolean visible, boolean hasAlpha, boolean allowsRainbow, Color value, boolean rainbow) {
		super(displayName,configName,description,visible,value);
		this.hasAlpha=hasAlpha;
		this.allowsRainbow=allowsRainbow;
		this.rainbow=rainbow;
	}
	
	@Override
	public Color getValue() {
		if (rainbow) {
			int speed=ClickGUIModule.rainbowSpeed.getValue();
			return ITheme.combineColors(Color.getHSBColor((System.currentTimeMillis()%(360*speed))/(float)(360*speed),1,1),super.getValue());
		}
		else return super.getValue();
	}

	@Override
	public TwoWayFunction<Color, String> getConverter() {
		return new TwoWayFunction<>() {
			@Override
			public String convert(Color color) {
				return String.valueOf(color.getRGB());
			}

			@Override
			public Color revert(String s) {
				return new Color(Integer.parseInt(s));
			}
		};
	}

	@Override
	public Color getColor() {
		return super.getValue();
	}

	@Override
	public boolean getRainbow() {
		return rainbow;
	}

	@Override
	public void setRainbow (boolean rainbow) {
		this.rainbow=rainbow;
	}
	
	@Override
	public boolean hasAlpha() {
		return hasAlpha;
	}
	
	@Override
	public boolean allowsRainbow() {
		return allowsRainbow;
	}
	
	@Override
	public boolean hasHSBModel() {
		return ClickGUIModule.colorModel.getValue()==ColorModel.HSB;
	}

	@Override
	public ObjectNode saveThis() {
		return ObjectUtil.getObjectNode().put("value", getValue().getRGB()).put("rainbow",rainbow).put("allowsRainbow",allowsRainbow).put("hasAlpha",hasAlpha);
	}

	@Override
	public void loadThis(ObjectNode node) {
		if (node.has("value")) {
			int rgb=node.get("value").asInt();
			setValue(new Color(rgb));
		}
		if (node.has("rainbow")) {
			rainbow=node.get("rainbow").asBoolean();
		}
		if (node.has("allowsRainbow")) {
			allowsRainbow=node.get("allowsRainbow").asBoolean();
		}
		if (node.has("hasAlpha")) {
			hasAlpha=node.get("hasAlpha").asBoolean();
		}
	}
}
