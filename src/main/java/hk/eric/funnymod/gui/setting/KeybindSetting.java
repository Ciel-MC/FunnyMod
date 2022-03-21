package hk.eric.funnymod.gui.setting;

import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.setting.IKeybindSetting;
import com.mojang.blaze3d.platform.InputConstants;
import hk.eric.ericLib.utils.classes.Converters;
import hk.eric.ericLib.utils.classes.TwoWayFunction;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.function.Consumer;

public class KeybindSetting extends SavableSetting<Integer> implements IKeybindSetting {
	
	private final Runnable trigger;
	private final boolean alwaysTrigger;

	public KeybindSetting(String displayName, String configName, String description, Integer value) {
		this(displayName, configName, description, value, false);
	}

	public KeybindSetting(String displayName, String configName, String description, Integer value, boolean alwaysTrigger) {
		this(displayName, configName, description, value, (Runnable) null);
	}

	public KeybindSetting(String displayName, String configName, String description, Integer value, Runnable trigger) {
		this(displayName, configName, description, value, trigger, false);
	}

	public KeybindSetting(String displayName, String configName, String description, Integer value, Runnable trigger, boolean alwaysTrigger) {
		super(displayName, configName, description, value);
		this.trigger = trigger;
		this.alwaysTrigger = alwaysTrigger;
	}

	public KeybindSetting(String displayName, String configName, String description, IBoolean visible, Integer value) {
		this(displayName, configName, description, visible, value, false);
	}

	public KeybindSetting(String displayName, String configName, String description, IBoolean visible, Integer value, boolean alwaysTrigger) {
		this(displayName, configName, description, visible, value, (Runnable) null);
	}

	public KeybindSetting(String displayName, String configName, String description, IBoolean visible, Integer value, Runnable trigger) {
		this(displayName, configName, description, visible, value, trigger, false);
	}

	public KeybindSetting(String displayName, String configName, String description, IBoolean visible, Integer value, Runnable trigger, boolean alwaysTrigger) {
		super(displayName, configName, description, visible, value);
		this.trigger = trigger;
		this.alwaysTrigger = alwaysTrigger;
	}

	public KeybindSetting(String displayName, String configName, String description, Integer value, Consumer<Integer> onChange) {
		this(displayName, configName, description, value, onChange, false);
	}

	public KeybindSetting(String displayName, String configName, String description, Integer value, Consumer<Integer> onChange, boolean alwaysTrigger) {
		this(displayName, configName, description, value, onChange, null);
	}

	public KeybindSetting(String displayName, String configName, String description, Integer value, Consumer<Integer> onChange, Runnable trigger) {
		this(displayName, configName, description, value, onChange, trigger, false);
	}

	public KeybindSetting(String displayName, String configName, String description, Integer value, Consumer<Integer> onChange, Runnable trigger, boolean alwaysTrigger) {
		super(displayName, configName, description, value, onChange);
		this.trigger = trigger;
		this.alwaysTrigger = alwaysTrigger;
	}

	public KeybindSetting(String displayName, String configName, String description, IBoolean visible, Integer value, Consumer<Integer> onChange) {
		this(displayName, configName, description, visible, value, onChange, false);
	}

	public KeybindSetting(String displayName, String configName, String description, IBoolean visible, Integer value, Consumer<Integer> onChange, boolean alwaysTrigger) {
		this(displayName, configName, description, visible, value, onChange, null);
	}

	public KeybindSetting(String displayName, String configName, String description, IBoolean visible, Integer value, Consumer<Integer> onChange, Runnable trigger) {
		this(displayName, configName, description, visible, value, onChange, trigger, false);
	}

	public KeybindSetting(String displayName, String configName, String description, IBoolean visible, Integer value, Consumer<Integer> onChange, Runnable trigger, boolean alwaysTrigger) {
		super(displayName, configName, description, visible, value, onChange);
		this.trigger = trigger;
		this.alwaysTrigger = alwaysTrigger;
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

	@Override
	public String getValueName() {
		return getKeyName();
	}

	@Override
	public TwoWayFunction<Integer, String> getConverter() {
		return Converters.INTEGER_CONVERTER;
	}

	public boolean isAlwaysTrigger() {
		return alwaysTrigger;
	}
}
