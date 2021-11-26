package hk.eric.funnymod.gui.setting.children;

import com.lukflug.panelstudio.base.IBoolean;
import hk.eric.funnymod.gui.setting.BooleanSetting;
import hk.eric.funnymod.gui.setting.Setting;

import java.util.ArrayList;
import java.util.List;

public class BooleanSettingWithChildren extends BooleanSetting implements HasChildren<Boolean> {
    final List<Setting<?>> trueChildren = new ArrayList<>();
    final List<Setting<?>> falseChildren = new ArrayList<>();

    public BooleanSettingWithChildren(String displayName, String configName, String description, IBoolean visible, Boolean value, Setting<?>... children) {
        super(displayName, configName, description, visible, value);
        addChildren(value, children);
    }

    @Override
    public <E> void addChild(Boolean state, Setting<E> child) {
        child.visible = () -> getValue() == state;
        getChildrenList(state).add(child);
    }

    private List<Setting<?>> getChildrenList(boolean value) {
        return value?trueChildren:falseChildren;
    }

    @Override
    public List<Setting<?>> getChildren(Boolean state) {
        return getChildrenList(state);
    }

    @Override
    public List<Setting<?>> getCurrentChildren() {
        return getChildrenList(getValue());
    }

    @Override
    public List<Setting<?>> getAllChildren() {
        ArrayList<Setting<?>> allChildren = new ArrayList<>();
        allChildren.addAll(trueChildren);
        allChildren.addAll(falseChildren);
        return allChildren;
    }

    @Override
    public void removeChild(Boolean state, Setting<?> child) {
        getChildrenList(state).remove(child);
    }

    @Override
    public void removeAllChildren(Boolean state) {
        getChildrenList(state).clear();
    }

    @Override
    public void removeAllChildren() {
        trueChildren.clear();
        falseChildren.clear();
    }
}
