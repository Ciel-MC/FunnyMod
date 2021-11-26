package com.lukflug.panelstudio.setting;

import hk.eric.funnymod.gui.setting.Setting;

import java.util.stream.Stream;

public interface HasSubSettings<T> {
    /**
     * Returns sub settings.
     * @return sub-settings
     */
    default Stream<ISetting<?>> getSubSettings() {
        return getAllSubSettings();
    }

    default void addSubSettings(T state, Setting<?>... children) {
        for (Setting<?> child : children) {
            addSubSetting(state, child);
        }
    }

    void addGlobalSubSetting(ISetting<?> subSetting);

    void removeGlobalSubSetting(ISetting<?> subSetting);

    Stream<ISetting<?>> geGlobalSubSettings();

    void addSubSetting(T state, ISetting<?> subSetting);

    Stream<ISetting<?>> getSubSettings(T state);

    Stream<ISetting<?>> getCurrentSubSettings();

    Stream<ISetting<?>> getAllSubSettings();

    void removeSubSetting(T state, ISetting<?> subSetting);

    void removeAllSubSettings(T state);

    void removeAllSubSettings();
}
