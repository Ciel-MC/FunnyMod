package hk.eric.funnymod.gui.setting;

import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.base.IToggleable;
import com.lukflug.panelstudio.setting.IKeybindSetting;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.network.chat.TranslatableComponent;

public class KeybindSetting extends Setting<Integer> implements IKeybindSetting {
	private final Runnable trigger;

	public KeybindSetting(String displayName, String configName, String description, IBoolean visible, Integer value) {
		this(displayName, configName, description, visible, value, null);
	}

	public KeybindSetting (String displayName, String configName, String description, IBoolean visible, Integer value, Runnable trigger) {
		super(displayName,configName,description,visible,value);
		this.trigger = trigger;
	}

	@Override
	public int getKey() {
		return getValue();
	}

	@Override
	public void setKey (int key) {
		setValue(key);
	}

	@Override
	public String getKeyName() {
		String translationKey=InputConstants.Type.KEYSYM.getOrCreate(getKey()).getName();
		String translation=new TranslatableComponent(translationKey).getString();
		if (!translation.equals(translationKey)) return translation;
		return InputConstants.Type.KEYSYM.getOrCreate(getKey()).getDisplayName().getString();
	}

	public Runnable getAction() {
		return trigger;
	}
}
