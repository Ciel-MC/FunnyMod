package hk.eric.funnymod.gui.setting;

import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.setting.HasSubSettings;
import com.lukflug.panelstudio.setting.ISetting;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class SavableSettingWithChild<T> extends SavableSetting<T> implements HasSubSettings<T> {

    private T value;

    private final Map<T, List<ISetting<?>>> subSettings = new HashMap<>();
    private List<ISetting<?>> globalSubSettings = new ArrayList<>();

    public SavableSettingWithChild(String displayName, String configName, String description, T value) {
        super(displayName, configName, description, value);
    }

    public SavableSettingWithChild(String displayName, String configName, String description, IBoolean visible, T value) {
        super(displayName, configName, description, visible, value);
    }

    public SavableSettingWithChild(String displayName, String configName, String description, T value, Consumer<T> onChange) {
        super(displayName, configName, description, value, onChange);
    }

    public SavableSettingWithChild(String displayName, String configName, String description, IBoolean visible, T value, Consumer<T> onChange) {
        super(displayName, configName, description, visible, value, onChange);
    }

    @Override
    public void addSubSetting(T state, ISetting<?> subSetting) {
        subSettings.computeIfAbsent(state, k -> new ArrayList<>()).add(subSetting);
    }

    @Override
    public Stream<ISetting<?>> getSubSettings(T state) {
        List<ISetting<?>> iSettingList = new ArrayList<>(globalSubSettings);
        iSettingList.addAll(subSettings.getOrDefault(state, new ArrayList<>()));
        return iSettingList.stream();
    }

    @Override
    public Stream<ISetting<?>> getCurrentSubSettings() {
        return getSubSettings(getValue());
    }

    @Override
    public Stream<ISetting<?>> getAllSubSettings() {
        List<ISetting<?>> iSettingList = new ArrayList<>(globalSubSettings);
        iSettingList.addAll(subSettings.values().stream().flatMap(Collection::stream).collect(Collectors.toList()));
        return iSettingList.stream();
    }

    @Override
    public void removeSubSetting(T state, ISetting<?> subSetting) {
        subSettings.get(state).remove(subSetting);
    }

    @Override
    public void removeAllSubSettings(T state) {
        subSettings.remove(state);
    }

    @Override
    public void removeAllSubSettings() {
        if(subSettings != null) {
            subSettings.clear();
        }
    }

    @Override
    public void addGlobalSubSetting(ISetting<?> subSetting) {
        globalSubSettings.add(subSetting);
    }

    @Override
    public void removeGlobalSubSetting(ISetting<?> subSetting) {
        globalSubSettings.remove(subSetting);
    }

    @Override
    public Stream<ISetting<?>> geGlobalSubSettings() {
        return globalSubSettings.stream();
    }
}
