package hk.eric.funnymod.modules;

import hk.eric.funnymod.FunnyModClient;
import hk.eric.funnymod.gui.Gui;
import org.lwjgl.glfw.GLFW;

import hk.eric.funnymod.gui.setting.EnumSetting;
import hk.eric.funnymod.gui.setting.IntegerSetting;
import hk.eric.funnymod.gui.setting.KeybindSetting;

public class ClickGUIModule extends Module {
	public static final EnumSetting<ColorModel> colorModel= new EnumSetting<>("Color Model", "colorModel", "Whether to use RGB or HSB.", () -> true, ColorModel.RGB, ColorModel.class);
	public static final IntegerSetting rainbowSpeed=new IntegerSetting("Rainbow Speed","rainbowSpeed","The speed of the color hue cycling.",()->true,1,100,32);
	public static final IntegerSetting scrollSpeed=new IntegerSetting("Scroll Speed","scrollSpeed","The speed of scrolling.",()->true,0,20,10);
	public static final IntegerSetting animationSpeed=new IntegerSetting("Animation Speed","animationSpeed","The speed of GUI animations.",()->true,0,1000,200);
	public static final EnumSetting<Theme> theme= new EnumSetting<>("Theme", "theme", "What theme to use.", () -> true, Theme.Impact, Theme.class);
	public static final EnumSetting<Layout> layout= new EnumSetting<>("Layout", "layout", "What layout to use.", () -> true, Layout.SinglePanel, Layout.class);
	public static final KeybindSetting keybind=new KeybindSetting("Keybind","keybind","The key to toggle the module.",()->true,GLFW.GLFW_KEY_RIGHT_SHIFT,()-> Gui.getGUI().enterGUI());
	
	public ClickGUIModule() {
		super("ClickGUI","Module containing ClickGUI settings.",()->true);
		settings.add(colorModel);
		settings.add(rainbowSpeed);
		settings.add(scrollSpeed);
		settings.add(animationSpeed);
		settings.add(theme);
		settings.add(layout);
		settings.add(keybind);
	}
	
	public enum ColorModel {
		RGB,HSB
	}
	
	public enum Theme {
		Clear,GameSense,Rainbow,Windows31,Impact
	}
	
	public enum Layout {
		ClassicPanel,PopupPanel,DraggablePanel,SinglePanel,PanelMenu,ColorPanel,CSGOHorizontal,CSGOVertical,CSGOCategory,SearchableCSGO
	}
}
