package hk.eric.funnymod.gui.setting;

import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.setting.ISetting;
import hk.eric.funnymod.utils.Constants;

import java.util.function.Consumer;

public abstract class Setting<T> implements ISetting<T> {
	public final String displayName,description;
	public IBoolean visible;
	protected T value;
	protected final Consumer<T> onChange;

	public Setting(String displayName, String description, T value) {
		this(displayName, description, Constants.alwaysTrue , value);
	}

	public Setting(String displayName, String description, IBoolean visible, T value) {
		this(displayName, description, visible, value, null);
	}

	public Setting(String displayName, String description, T value, Consumer<T> onChange) {
		this(displayName, description, Constants.alwaysTrue, value, onChange);
	}

	public Setting (String displayName, String description, IBoolean visible, T value, Consumer<T> onChange) {
		this.displayName=displayName;
		this.description=description;
		this.visible=visible;
		this.value=value;
		this.onChange=onChange;
	}

	@Override
	public T getValue() {
		return value;
	}

	@Override
	public void setValue (T value) {
		this.value=value;
		if(onChange!=null) onChange.accept(value);
	}

	@Override
	public String getDisplayName() {
		return displayName;
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
