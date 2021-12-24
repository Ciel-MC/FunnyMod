package hk.eric.funnymod.modules.visual;

import com.lukflug.panelstudio.base.Animation;
import com.lukflug.panelstudio.base.IToggleable;
import com.lukflug.panelstudio.component.IFixedComponent;
import com.lukflug.panelstudio.container.IContainer;
import com.lukflug.panelstudio.setting.IClient;
import com.lukflug.panelstudio.tabgui.ITabGUITheme;
import com.lukflug.panelstudio.tabgui.StandardTheme;
import com.lukflug.panelstudio.tabgui.TabGUI;
import com.lukflug.panelstudio.theme.IColorScheme;
import com.lukflug.panelstudio.theme.ITheme;
import hk.eric.funnymod.gui.setting.ColorSetting;
import hk.eric.funnymod.modules.HasComponents;
import hk.eric.funnymod.modules.ToggleableModule;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.Collections;
import java.util.Set;
import java.util.function.Supplier;

public class TabGUIModule extends ToggleableModule implements HasComponents {
	private static TabGUIModule instance;
	private static ITabGUITheme theme;
	
	public TabGUIModule() {
		super("TabGUI","HUD module that lets toggle modules.",true);
		instance=this;
		theme=new StandardTheme(new IColorScheme() {
			@Override
			public void createSetting(ITheme theme, String name, String description, boolean hasAlpha, boolean allowsRainbow, Color color, boolean rainbow) {
				ColorSetting setting=new ColorSetting(name,name,description,allowsRainbow,hasAlpha,color,rainbow);
				instance.settings.add(setting);
			}

			@Override
			public Color getColor (String name) {
				return (Color) instance.settings.stream().filter(setting -> setting.getDisplayName().equals(name)).findFirst().get().getValue();
			}
		},75,9,2,10);
	}

	public Set<IFixedComponent> getComponents(IClient client, IContainer<IFixedComponent> container, Supplier<Animation> animation) {
		return Collections.singleton(new TabGUI(() -> "TabGUI", client, theme, container, animation, key -> key == GLFW.GLFW_KEY_UP, key -> key == GLFW.GLFW_KEY_DOWN, key -> key == GLFW.GLFW_KEY_ENTER || key == GLFW.GLFW_KEY_RIGHT, key -> key == GLFW.GLFW_KEY_LEFT, new Point(10, 10), "tabGUI").getWrappedComponent());
	}
	
	public static IToggleable getToggle() {
		return instance.getToggleable();
	}
}
