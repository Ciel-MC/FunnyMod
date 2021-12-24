package hk.eric.funnymod.modules;

import com.lukflug.panelstudio.base.Animation;
import com.lukflug.panelstudio.component.IFixedComponent;
import com.lukflug.panelstudio.container.IContainer;
import com.lukflug.panelstudio.setting.IClient;

import java.util.Set;
import java.util.function.Supplier;

public interface HasComponents {
    Set<IFixedComponent> getComponents(IClient client, IContainer<IFixedComponent> container, Supplier<Animation> animation);
}
