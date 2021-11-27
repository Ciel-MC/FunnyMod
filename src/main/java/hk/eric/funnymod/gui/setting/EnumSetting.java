package hk.eric.funnymod.gui.setting;

import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.setting.IEnumSetting;
import com.lukflug.panelstudio.setting.ILabeled;
import com.lukflug.panelstudio.setting.ISetting;
import hk.eric.funnymod.utils.classes.TwoWayFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class EnumSetting<E extends Enum<E>> extends SavableSetting<E> implements IEnumSetting<E> {
	private final Class<E> settingClass;
	private final ILabeled[] array;

	private int value;

	private final EnumMap<E,List<ISetting<?>>> subSettings;
	private final List<ISetting<?>> globalSubSettings = new ArrayList<>();

	public EnumSetting(String displayName, String configName, String description, E value, Class<E> settingClass) {
		this(displayName, configName, description, value, settingClass, e -> e::name);
	}

	public EnumSetting(String displayName, String configName, String description, E value, Class<E> settingClass, Function<E, ILabeled> nameFunction) {
		super(displayName, configName, description, value);
		this.settingClass = settingClass;
		array=Arrays.stream(settingClass.getEnumConstants()).map(nameFunction).toArray(ILabeled[]::new);
		this.value = value.ordinal();
		subSettings = new EnumMap<>(settingClass);
	}

	public EnumSetting(String displayName, String configName, String description, IBoolean visible, E value, Class<E> settingClass) {
		this(displayName, configName, description, visible, value, settingClass, e -> e::name);
	}

	public EnumSetting(String displayName, String configName, String description, IBoolean visible, E value, Class<E> settingClass, Function<E, ILabeled> nameFunction) {
		super(displayName, configName, description, visible, value);
		this.settingClass = settingClass;
		array=Arrays.stream(settingClass.getEnumConstants()).map(nameFunction).toArray(ILabeled[]::new);
		this.value = value.ordinal();
		subSettings = new EnumMap<>(settingClass);
	}

	public EnumSetting(String displayName, String configName, String description, E value, Consumer<E> onChange, Class<E> settingClass) {
		this(displayName, configName, description, value, onChange, settingClass, e -> e::name);
	}

	public EnumSetting(String displayName, String configName, String description, E value, Consumer<E> onChange, Class<E> settingClass, Function<E, ILabeled> nameFunction) {
		super(displayName, configName, description, value, onChange);
		this.settingClass = settingClass;
		array=Arrays.stream(settingClass.getEnumConstants()).map(nameFunction).toArray(ILabeled[]::new);
		this.value = value.ordinal();
		subSettings = new EnumMap<>(settingClass);
	}

	public EnumSetting(String displayName, String configName, String description, IBoolean visible, E value, Consumer<E> onChange, Class<E> settingClass) {
		this(displayName, configName, description, visible, value, onChange, settingClass, e -> e::name);
	}

	public EnumSetting(String displayName, String configName, String description, IBoolean visible, E value, Consumer<E> onChange, Class<E> settingClass, Function<E, ILabeled> nameFunction) {
		super(displayName, configName, description, visible, value, onChange);
		this.settingClass = settingClass;
		array=Arrays.stream(settingClass.getEnumConstants()).map(nameFunction).toArray(ILabeled[]::new);
		this.value = value.ordinal();
		subSettings = new EnumMap<>(settingClass);
	}

	@Override
	public void increment() {
		E[] array=settingClass.getEnumConstants();
		int index=getValue().ordinal()+1;
		if (index>=array.length) index=0;
		setValue(array[index]);
	}

	@Override
	public void decrement() {
		E[] array=settingClass.getEnumConstants();
		int index=getValue().ordinal()-1;
		if (index<0) index=array.length-1;
		setValue(array[index]);
	}
//
//	@Override
//	public E fromString(String value) {
//		return Enum.valueOf(settingClass, value);
//	}

	@Override
	public TwoWayFunction<E, String> getConverter() {
		return new TwoWayFunction<>() {
			@Override
			public String convert(E e) {
				return e.name();
			}

			@Override
			public E revert(String s) {
				return Enum.valueOf(settingClass, s);
			}
		};
	}

	@Override
	public int getValueIndex() {
		return getValue().ordinal();
	}

	@Override
	public void setValueIndex(int index) {
		setValue(settingClass.getEnumConstants()[index]);
	}

	@Override
	public ILabeled[] getAllowedValues() {
		return array;
	}

	@Override
	public E getValue() {
		return settingClass.getEnumConstants()[value];
	}

	@Override
	public void setValue(E value) {
		super.setValue(value);
		this.value = value.ordinal();
	}

	@Override
	public Class<E> getSettingClass() {
		return settingClass;
	}
}
