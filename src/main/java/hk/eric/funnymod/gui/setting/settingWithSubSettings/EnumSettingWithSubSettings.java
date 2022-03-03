package hk.eric.funnymod.gui.setting.settingWithSubSettings;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.setting.HasSubSettingsImpl;
import com.lukflug.panelstudio.setting.ILabeled;
import com.lukflug.panelstudio.setting.ISetting;
import hk.eric.funnymod.exceptions.ConfigLoadingFailedException;
import hk.eric.funnymod.gui.setting.EnumSetting;
import hk.eric.funnymod.utils.SettingUtil;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;

public class EnumSettingWithSubSettings<E extends Enum<E>> extends EnumSetting<E> implements HasSubSettingsImpl<E> {

    private final EnumMap<E, List<ISetting<?>>> subSettings;
    private final List<ISetting<?>> globalSubSettings = new ArrayList<>();

    public EnumSettingWithSubSettings(String displayName, String configName, String description, E value, Class<E> settingClass) {
        super(displayName, configName, description, value, settingClass);
        subSettings = new EnumMap<>(settingClass);
    }

    public EnumSettingWithSubSettings(String displayName, String configName, String description, E value, Class<E> settingClass, Function<E, ILabeled> nameFunction) {
        super(displayName, configName, description, value, settingClass, nameFunction);
        subSettings = new EnumMap<>(settingClass);
    }

    public EnumSettingWithSubSettings(String displayName, String configName, String description, IBoolean visible, E value, Class<E> settingClass) {
        super(displayName, configName, description, visible, value, settingClass);
        subSettings = new EnumMap<>(settingClass);
    }

    public EnumSettingWithSubSettings(String displayName, String configName, String description, IBoolean visible, E value, Class<E> settingClass, Function<E, ILabeled> nameFunction) {
        super(displayName, configName, description, visible, value, settingClass, nameFunction);
        subSettings = new EnumMap<>(settingClass);
    }

    public EnumSettingWithSubSettings(String displayName, String configName, String description, E value, Consumer<E> onChange, Class<E> settingClass) {
        super(displayName, configName, description, value, onChange, settingClass);
        subSettings = new EnumMap<>(settingClass);
    }

    public EnumSettingWithSubSettings(String displayName, String configName, String description, E value, Consumer<E> onChange, Class<E> settingClass, Function<E, ILabeled> nameFunction) {
        super(displayName, configName, description, value, onChange, settingClass, nameFunction);
        subSettings = new EnumMap<>(settingClass);
    }

    public EnumSettingWithSubSettings(String displayName, String configName, String description, IBoolean visible, E value, Consumer<E> onChange, Class<E> settingClass) {
        super(displayName, configName, description, visible, value, onChange, settingClass);
        subSettings = new EnumMap<>(settingClass);
    }

    public EnumSettingWithSubSettings(String displayName, String configName, String description, IBoolean visible, E value, Consumer<E> onChange, Class<E> settingClass, Function<E, ILabeled> nameFunction) {
        super(displayName, configName, description, visible, value, onChange, settingClass, nameFunction);
        subSettings = new EnumMap<>(settingClass);
    }

    @Override
    public List<ISetting<?>> getGlobalSubSettings() {
        return globalSubSettings;
    }

    @Override
    public Map<E, List<ISetting<?>>> getSubSettingsMap() {
        return subSettings;
    }

    @Override
    public void addSubSetting(E state, ISetting<?> subSetting) {
        subSetting.setIsVisible(() -> getValue() == state);
        if (subSettings.containsKey(state)) {
            subSettings.get(state).add(subSetting);
        }else {
            subSettings.put(state, new ArrayList<>());
            subSettings.get(state).add(subSetting);
        }
    }

    @Override
    public ObjectNode save() {
        ObjectNode node = super.save();
        SettingUtil.getSavableStream(getAllSubSettings()).forEach((setting) -> node.set(setting.getConfigName(), setting.save()));
        return node;
    }

    @Override
    public void load(ObjectNode node) throws ConfigLoadingFailedException {
        super.load(node);
        AtomicBoolean error = new AtomicBoolean(false);
        SettingUtil.getSavableStream(getAllSubSettings()).forEach(setting -> {
            try {
                setting.load((ObjectNode) node.get(setting.getConfigName()));
            } catch (ConfigLoadingFailedException e) {
                error.set(true);
            }
        });
        if (error.get()) {
            throw new ConfigLoadingFailedException();
        }
    }
}
