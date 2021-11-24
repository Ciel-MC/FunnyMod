package hk.eric.funnymod.gui.setting.children;

import com.lukflug.panelstudio.base.IBoolean;
import hk.eric.funnymod.gui.setting.EnumSetting;
import hk.eric.funnymod.gui.setting.Setting;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class EnumSettingWithChildren<E extends Enum<E>> extends EnumSetting<E> implements HasChildren<E> {

    private final EnumMap<E,List<Setting<?>>> childrenMap;

    public EnumSettingWithChildren(String displayName, String configName, String description, IBoolean visible, E value, Class<E> settingClass) {
        super(displayName, configName, description, visible, value, settingClass);
        childrenMap = new EnumMap<E, List<Setting<?>>>(settingClass);
    }

    @Override
    public <E1> void addChild(E state, Setting<E1> child) {
        childrenMap.putIfAbsent(state, new ArrayList<>());
        child.visible = () -> getValue() == state;
        childrenMap.get(state).add(child);
    }

    @Override
    public List<Setting<?>> getChildren(E state) {
        return childrenMap.get(state);
    }

    @Override
    public List<Setting<?>> getCurrentChildren() {
        return childrenMap.get(getValue());
    }

    @Override
    public List<Setting<?>> getAllChildren() {
        ArrayList<Setting<?>> allChildren = new ArrayList<>();
        childrenMap.forEach((state, children) -> allChildren.addAll(children));
        return allChildren;
    }
}
