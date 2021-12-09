package com.lukflug.panelstudio.setting;

import hk.eric.funnymod.utils.ArrayUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface HasSubSettingsImpl<T> extends HasSubSettings<T> {

    T getValue();
    
    List<ISetting<?>> getGlobalSubSettings();
    
    Map<T, List<ISetting<?>>> getSubSettingsMap();
    
    @Override
    default void addSubSetting(T state, ISetting<?> subSetting) {
        subSetting.setIsVisible(() -> getValue() == state);
        getSubSettingsMap().computeIfAbsent(state, k -> new ArrayList<>()).add(subSetting);
    }

    @Override
    default Stream<ISetting<?>> getSubSettings(T state) {
        return ArrayUtil.combineAndStream(getGlobalSubSettings(), getSubSettingsMap().get(state));
    }

    @Override
    default Stream<ISetting<?>> getCurrentSubSettings() {
        return getSubSettings(getValue());
    }

    @Override
    default Stream<ISetting<?>> getAllSubSettings() {
        return ArrayUtil.combineAndStream(getGlobalSubSettings(), getSubSettingsMap().values().stream().flatMap(Collection::stream).collect(Collectors.toList()));
    }

    @Override
    default void removeSubSetting(T state, ISetting<?> subSetting) {
        getSubSettingsMap().get(state).remove(subSetting);
    }

    @Override
    default void removeAllSubSettings(T state) {
        getSubSettingsMap().remove(state);
    }

    @Override
    default void removeAllSubSettings() {
        if(getSubSettingsMap() != null) {
            getSubSettingsMap().clear();
        }
    }

    @Override
    default void addGlobalSubSetting(ISetting<?> subSetting) {
        getGlobalSubSettings().add(subSetting);
    }

    @Override
    default void removeGlobalSubSetting(ISetting<?> subSetting) {
        getGlobalSubSettings().remove(subSetting);
    }

    @Override
    default Stream<ISetting<?>> geGlobalSubSettings() {
        return getGlobalSubSettings().stream();
    }
}
