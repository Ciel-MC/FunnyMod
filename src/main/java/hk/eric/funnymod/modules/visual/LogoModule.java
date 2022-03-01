package hk.eric.funnymod.modules.visual;

import com.lukflug.panelstudio.base.Animation;
import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.base.IInterface;
import com.lukflug.panelstudio.base.IToggleable;
import com.lukflug.panelstudio.component.IFixedComponent;
import com.lukflug.panelstudio.container.IContainer;
import com.lukflug.panelstudio.hud.HUDComponent;
import com.lukflug.panelstudio.setting.IClient;
import hk.eric.funnymod.gui.ClickGUI;
import hk.eric.funnymod.gui.setting.BooleanSetting;
import hk.eric.funnymod.gui.setting.ColorSetting;
import hk.eric.funnymod.gui.setting.IntegerSetting;
import hk.eric.funnymod.modules.HasComponents;
import hk.eric.funnymod.modules.ToggleableModule;

import java.awt.*;
import java.util.Collections;
import java.util.Set;
import java.util.function.Supplier;

public class LogoModule extends ToggleableModule implements HasComponents {
	private static LogoModule instance;
	private static final IntegerSetting rotation=new IntegerSetting("Image Rotation","rotation","How to rotate the image.",0,3,0);
	private static final BooleanSetting parity=new BooleanSetting("Flip Image","parity","Whether to flip the image or not.",false);
	private static final ColorSetting color=new ColorSetting("Logo Color","color","The color to modulate the logo with.",true,true,new Color(255,255,255,128),true);
	
	public LogoModule() {
		super("Logo","Module that displays the PanelStudio icon on HUD.",true);
		instance=this;
		settings.add(rotation);
		settings.add(parity);
		settings.add(color);
	}

	public Set<IFixedComponent> getComponents(IClient client, IContainer<IFixedComponent> container, Supplier<Animation> animation) {
		int image = ClickGUI.inter.loadImage("logo.png");
		return Collections.singleton(new HUDComponent(() -> "Logo", new Point(100, 10), "logo") {
			@Override
			public void render(Context context) {
				super.render(context);
				context.getInterface().drawImage(context.getRect(), rotation.getValue(), parity.getValue(), image, color.getValue());
			}

			@Override
			public Dimension getSize(IInterface inter) {
				return new Dimension(141, 61);
			}
		});
	}
	
	public static IToggleable getToggle() {
		return instance.getToggleable();
	}
}
