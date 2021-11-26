package hk.eric.funnymod.gui.setting.children;

import hk.eric.funnymod.gui.setting.Setting;

import java.util.List;

public interface HasChildren<T> {

    public default void addChildren(T state,Setting<?>... children) {
        for (Setting<?> child : children) {
            addChild(state, child);
        }
    }

    public <E> void addChild(T state, Setting<E> child);

    public List<Setting<?>> getChildren(T state);

    public List<Setting<?>> getCurrentChildren();

    public List<Setting<?>> getAllChildren();

    public void removeChild(T state, Setting<?> child);

    public void removeAllChildren(T state);

    public void removeAllChildren();
}
