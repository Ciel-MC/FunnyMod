package hk.eric.funnymod.gui.setting.children;

import hk.eric.funnymod.gui.setting.Setting;

import java.util.List;

public interface HasChildren<T> {

    default void addChildren(T state, Setting<?>... children) {
        for (Setting<?> child : children) {
            addChild(state, child);
        }
    }

    <E> void addChild(T state, Setting<E> child);

    List<Setting<?>> getChildren(T state);

    List<Setting<?>> getCurrentChildren();

    List<Setting<?>> getAllChildren();

    void removeChild(T state, Setting<?> child);

    void removeAllChildren(T state);

    void removeAllChildren();
}
