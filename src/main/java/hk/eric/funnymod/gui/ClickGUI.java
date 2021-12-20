package hk.eric.funnymod.gui;

import com.lukflug.panelstudio.base.*;
import com.lukflug.panelstudio.component.IComponent;
import com.lukflug.panelstudio.component.IFixedComponent;
import com.lukflug.panelstudio.component.IResizable;
import com.lukflug.panelstudio.component.IScrollSize;
import com.lukflug.panelstudio.container.IContainer;
import com.lukflug.panelstudio.hud.HUDGUI;
import com.lukflug.panelstudio.layout.*;
import com.lukflug.panelstudio.layout.ChildUtil.ChildMode;
import com.lukflug.panelstudio.mc17.MinecraftHUDGUI;
import com.lukflug.panelstudio.popup.CenteredPositioner;
import com.lukflug.panelstudio.popup.MousePositioner;
import com.lukflug.panelstudio.popup.PanelPositioner;
import com.lukflug.panelstudio.popup.PopupTuple;
import com.lukflug.panelstudio.setting.*;
import com.lukflug.panelstudio.theme.*;
import com.lukflug.panelstudio.widget.*;
import hk.eric.funnymod.FunnyModClient;
import hk.eric.funnymod.gui.setting.*;
import hk.eric.funnymod.modules.Category;
import hk.eric.funnymod.modules.ClickGUIModule;
import hk.eric.funnymod.modules.ClickGUIModule.Theme;
import hk.eric.funnymod.modules.mcqp.MCQPHudModule;
import hk.eric.funnymod.modules.visual.LogoModule;
import hk.eric.funnymod.modules.visual.TabGUIModule;
import net.minecraft.ChatFormatting;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.*;

public class ClickGUI extends MinecraftHUDGUI {
	private final GUIInterface inter;
	private final HUDGUI gui;
	public static final int WIDTH=120,HEIGHT=12,DISTANCE=6,BORDER=2;
	
	private static final String title = FunnyModClient.MOD_NAME;

	@Override
	public void removed() {
		if (getGUI().getGUIVisibility().isOn()) getGUI().getGUIVisibility().toggle();
		if (getGUI().getHUDVisibility().isOn()) getGUI().getHUDVisibility().toggle();
	}

	public ClickGUI() {
		// Getting client structure ...
		IClient client=Category.getClient();
		/* Set to false in order to disable horizontal clipping, this may cause graphical glitches,
		 * but will let you see long text, even if it is too long to fit in the panel. */
		inter=new GUIInterface(true) {
			@Override
			protected String getResourcePrefix() {
				return FunnyModClient.MOD_ID + ":";
			}
		};
		// Instantiating theme ...
		ITheme theme=new OptimizedTheme(new ThemeSelector(inter));
		// Instantiating GUI ...
		IToggleable guiToggle=new SimpleToggleable(false);
		IToggleable hudToggle=new SimpleToggleable(false);
		gui=new HUDGUI(inter,theme.getDescriptionRenderer(), new MousePositioner(new Point(10,10)),guiToggle,hudToggle);
		// Creating animation ...
		Supplier<Animation> animation=()->new SettingsAnimation(ClickGUIModule.animationSpeed::getValue, inter::getTime);
		// Populating HUD ...
		gui.addHUDComponent(TabGUIModule.getComponent(client, new IContainer<>() {
			@Override
			public boolean addComponent(IFixedComponent component) {
				return gui.addHUDComponent(component);
			}

			@Override
			public boolean addComponent(IFixedComponent component, IBoolean visible) {
				return gui.addHUDComponent(component, visible);
			}

			@Override
			public boolean removeComponent(IFixedComponent component) {
				return gui.removeComponent(component);
			}
		},animation),TabGUIModule.getToggle(),animation.get(),theme,BORDER);
		gui.addHUDComponent(LogoModule.getComponent(inter),LogoModule.getToggle(),animation.get(),theme,BORDER);

		gui.addHUDComponent(MCQPHudModule.getComponent(),MCQPHudModule.getToggle(),animation.get(),theme,BORDER);

		// Creating popup types ...
		BiFunction<Context,Integer,Integer> scrollHeight=(context,componentHeight)->Math.min(componentHeight,Math.max(HEIGHT*4,ClickGUI.this.height-context.getPos().y-HEIGHT));
		PopupTuple popupType=new PopupTuple(new PanelPositioner(new Point(0,0)),false,new IScrollSize() {
			@Override
			public int getScrollHeight (Context context, int componentHeight) {
				return scrollHeight.apply(context,componentHeight);
			}
		});
		PopupTuple colorPopup=new PopupTuple(new CenteredPositioner(()->new Rectangle(new Point(0,0),inter.getWindowSize())),true,new IScrollSize() {
			@Override
			public int getScrollHeight (Context context, int componentHeight) {
				return scrollHeight.apply(context,componentHeight);
			}
		});
		// Defining resize behavior ...
		IntFunction<IResizable> resizable=width->new IResizable() {
			final Dimension size=new Dimension(width,320);
			
			@Override
			public Dimension getSize() {
				return new Dimension(size);
			}

			@Override
			public void setSize (Dimension size) {
				this.size.width=size.width;
				this.size.height=size.height;
				if (size.width<75) this.size.width=75;
				if (size.height<50) this.size.height=50;
			}
		};
		// Defining scroll behavior ...
		Function<IResizable,IScrollSize> resizableHeight=size->new IScrollSize() {
			@Override
			public int getScrollHeight (Context context, int componentHeight) {
				return size.getSize().height;
			}
		};
		// Defining function keys ...
		IntPredicate keybindKey=scancode->scancode==GLFW.GLFW_KEY_DELETE;
		IntPredicate charFilter=character-> character>=' ';
		ITextFieldKeys keys=new ITextFieldKeys() {
			@Override
			public boolean isBackspaceKey (int scancode) {
				return scancode==GLFW.GLFW_KEY_BACKSPACE;
			}

			@Override
			public boolean isDeleteKey (int scancode) {
				return scancode==GLFW.GLFW_KEY_DELETE;
			}

			@Override
			public boolean isInsertKey (int scancode) {
				return scancode==GLFW.GLFW_KEY_INSERT;
			}

			@Override
			public boolean isLeftKey (int scancode) {
				return scancode==GLFW.GLFW_KEY_LEFT;
			}

			@Override
			public boolean isRightKey (int scancode) {
				return scancode==GLFW.GLFW_KEY_RIGHT;
			}

			@Override
			public boolean isHomeKey (int scancode) {
				return scancode==GLFW.GLFW_KEY_HOME;
			}

			@Override
			public boolean isEndKey (int scancode) {
				return scancode==GLFW.GLFW_KEY_END;
			}

			@Override
			public boolean isCopyKey (int scancode) {
				return scancode==GLFW.GLFW_KEY_C;
			}

			@Override
			public boolean isPasteKey (int scancode) {
				return scancode==GLFW.GLFW_KEY_V;
			}

			@Override
			public boolean isCutKey (int scancode) {
				return scancode==GLFW.GLFW_KEY_X;
			}

			@Override
			public boolean isAllKey (int scancode) {
				return scancode==GLFW.GLFW_KEY_A;
			}			
		};
		
		// Normal generator
		IComponentGenerator generator=new ComponentGenerator(keybindKey,charFilter,keys);
		// Use cycle switches instead of buttons
		IComponentGenerator cycleGenerator=new ComponentGenerator(keybindKey,charFilter,keys) {
			@Override
			public IComponent getEnumComponent (IEnumSetting setting, Supplier<Animation> animation, IComponentAdder adder, ThemeTuple theme, int colorLevel, boolean isContainer) {
				return new CycleSwitch(setting,theme.getCycleSwitchRenderer(isContainer));
			}
		};
		// Use all the fancy widgets with text boxes
		IComponentGenerator csgoGenerator=new ComponentGenerator(keybindKey,charFilter,keys) {
			@Override
			public IComponent getBooleanComponent (IBooleanSetting setting, Supplier<Animation> animation, IComponentAdder adder, ThemeTuple theme, int colorLevel, boolean isContainer) {
				return new ToggleSwitch(setting,theme.getToggleSwitchRenderer(isContainer));
			}
			
			@Override
			public IComponent getEnumComponent (IEnumSetting setting, Supplier<Animation> animation, IComponentAdder adder, ThemeTuple theme, int colorLevel, boolean isContainer) {
				return new DropDownList(setting,theme,isContainer,false,keys,new IScrollSize(){},adder::addPopup) {
					@Override
					protected Animation getAnimation() {
						return animation.get();
					}

					@Override
					public boolean allowCharacter (char character) {
						return charFilter.test(character);
					}

					@Override
					protected boolean isUpKey (int key) {
						return key==GLFW.GLFW_KEY_UP;
					}

					@Override
					protected boolean isDownKey (int key) {
						return key==GLFW.GLFW_KEY_DOWN;
					}

					@Override
					protected boolean isEnterKey (int key) {
						return key==GLFW.GLFW_KEY_ENTER;
					}
				};
			}
			
			@Override
			public IComponent getNumberComponent (INumberSetting setting, Supplier<Animation> animation, IComponentAdder adder, ThemeTuple theme, int colorLevel, boolean isContainer) {
				return new Spinner(setting,theme,isContainer,true,keys);
			}
			
			@Override
			public IComponent getColorComponent (IColorSetting setting, Supplier<Animation> animation, IComponentAdder adder, ThemeTuple theme, int colorLevel, boolean isContainer) {
				return new ColorPickerComponent(setting,new ThemeTuple(theme.theme,theme.logicalLevel,colorLevel));
			}
		};
		
		// Classic Panel
		IComponentAdder classicPanelAdder=new PanelAdder(gui,false,()->ClickGUIModule.layout.getValue()==ClickGUIModule.Layout.ClassicPanel,title->"classicPanel_"+title) {
			@Override
			protected IResizable getResizable (int width) {
				return resizable.apply(width);
			}
			
			@Override
			protected IScrollSize getScrollSize (IResizable size) {
				return resizableHeight.apply(size);
			}
		};
		ILayout classicPanelLayout=new PanelLayout(WIDTH,new Point(DISTANCE,DISTANCE),(WIDTH+DISTANCE)/2,HEIGHT+DISTANCE,animation,level->ChildMode.DOWN,level->ChildMode.DOWN,popupType);
		classicPanelLayout.populateGUI(classicPanelAdder,generator,client,theme);
		// Pop-up Panel
		IComponentAdder popupPanelAdder=new PanelAdder(gui,false,()->ClickGUIModule.layout.getValue()==ClickGUIModule.Layout.PopupPanel,title->"popupPanel_"+title) {
			@Override
			protected IResizable getResizable (int width) {
				return resizable.apply(width);
			}
			
			@Override
			protected IScrollSize getScrollSize (IResizable size) {
				return resizableHeight.apply(size);
			}
		};
		ILayout popupPanelLayout=new PanelLayout(WIDTH,new Point(DISTANCE,DISTANCE),(WIDTH+DISTANCE)/2,HEIGHT+DISTANCE,animation,level->ChildMode.POPUP,level->ChildMode.DOWN,popupType);
		popupPanelLayout.populateGUI(popupPanelAdder,generator,client,theme);
		// Draggable Panel
		IComponentAdder draggablePanelAdder=new PanelAdder(gui,false,()->ClickGUIModule.layout.getValue()==ClickGUIModule.Layout.DraggablePanel,title->"draggablePanel_"+title) {
			@Override
			protected IResizable getResizable (int width) {
				return resizable.apply(width);
			}
			
			@Override
			protected IScrollSize getScrollSize (IResizable size) {
				return resizableHeight.apply(size);
			}
		};
		ILayout draggablePanelLayout=new PanelLayout(WIDTH,new Point(DISTANCE,DISTANCE),(WIDTH+DISTANCE)/2,HEIGHT+DISTANCE,animation,level->level==0?ChildMode.DRAG_POPUP:ChildMode.DOWN,level->ChildMode.DOWN,popupType);
		draggablePanelLayout.populateGUI(draggablePanelAdder,generator,client,theme);
		// Single Panel
		IComponentAdder singlePanelAdder=new SinglePanelAdder(gui,new Labeled(title,null),theme,new Point(10,10),WIDTH*Category.values().length,animation,()->ClickGUIModule.layout.getValue()==ClickGUIModule.Layout.SinglePanel,"singlePanel") {
			@Override
			protected IResizable getResizable (int width) {
				return resizable.apply(width);
			}
			
			@Override
			protected IScrollSize getScrollSize (IResizable size) {
				return resizableHeight.apply(size);
			}
		};
		ILayout singlePanelLayout=new PanelLayout(WIDTH,new Point(DISTANCE,DISTANCE),(WIDTH+DISTANCE)/2,HEIGHT+DISTANCE,animation,level->ChildMode.DOWN,level->ChildMode.DOWN,popupType);
		singlePanelLayout.populateGUI(singlePanelAdder,generator,client,theme);
		// Panel Menu
		IComponentAdder panelMenuAdder=new StackedPanelAdder(gui,new Labeled(title,null),theme,new Point(10,10),WIDTH,animation,ChildMode.POPUP,new PanelPositioner(new Point(0,0)),()->ClickGUIModule.layout.getValue()==ClickGUIModule.Layout.PanelMenu,"panelMenu");
		ILayout panelMenuLayout=new PanelLayout(WIDTH,new Point(DISTANCE,DISTANCE),(WIDTH+DISTANCE)/2,HEIGHT+DISTANCE,animation,level->ChildMode.POPUP,level->ChildMode.POPUP,popupType);
		panelMenuLayout.populateGUI(panelMenuAdder,generator,client,theme);
		// Color Panel
		IComponentAdder colorPanelAdder=new PanelAdder(gui,false,()->ClickGUIModule.layout.getValue()==ClickGUIModule.Layout.ColorPanel,title->"colorPanel_"+title) {
			@Override
			protected IResizable getResizable (int width) {
				return resizable.apply(width);
			}
			
			@Override
			protected IScrollSize getScrollSize (IResizable size) {
				return resizableHeight.apply(size);
			}
		};
		ILayout colorPanelLayout=new PanelLayout(WIDTH,new Point(DISTANCE,DISTANCE),(WIDTH+DISTANCE)/2,HEIGHT+DISTANCE,animation,level->ChildMode.DOWN,level->ChildMode.POPUP,colorPopup);
		colorPanelLayout.populateGUI(colorPanelAdder,cycleGenerator,client,theme);
		// Horizontal CSGO
		AtomicReference<IResizable> horizontalResizable= new AtomicReference<>(null);
		IComponentAdder horizontalCSGOAdder=new PanelAdder(gui,true,()->ClickGUIModule.layout.getValue()==ClickGUIModule.Layout.CSGOHorizontal,title->"horizontalCSGO_"+title) {
			@Override
			protected IResizable getResizable (int width) {
				horizontalResizable.set(resizable.apply(width));
				return horizontalResizable.get();
			}
		};
		ILayout horizontalCSGOLayout=new CSGOLayout(new Labeled(title,null),new Point(100,100),480,WIDTH,animation,"Enabled",true,true,2,ChildMode.POPUP,colorPopup) {
			@Override
			public int getScrollHeight (Context context, int componentHeight) {
				return resizableHeight.apply(horizontalResizable.get()).getScrollHeight(null,height);
			}
		};
		horizontalCSGOLayout.populateGUI(horizontalCSGOAdder,csgoGenerator,client,theme);
		// Vertical CSGO
		AtomicReference<IResizable> verticalResizable= new AtomicReference<>(null);
		IComponentAdder verticalCSGOAdder=new PanelAdder(gui,true,()->ClickGUIModule.layout.getValue()==ClickGUIModule.Layout.CSGOVertical,title->"verticalCSGO_"+title) {
			@Override
			protected IResizable getResizable (int width) {
				verticalResizable.set(resizable.apply(width));
				return verticalResizable.get();
			}
		};
		ILayout verticalCSGOLayout=new CSGOLayout(new Labeled(title,null),new Point(100,100),480,WIDTH,animation,"Enabled",false,true,2,ChildMode.POPUP,colorPopup) {
			@Override
			public int getScrollHeight (Context context, int componentHeight) {
				return resizableHeight.apply(verticalResizable.get()).getScrollHeight(null,height);
			}
		};
		verticalCSGOLayout.populateGUI(verticalCSGOAdder,csgoGenerator,client,theme);
		// Category CSGO
		AtomicReference<IResizable> categoryResizable= new AtomicReference<>(null);
		IComponentAdder categoryCSGOAdder=new PanelAdder(gui,true,()->ClickGUIModule.layout.getValue()==ClickGUIModule.Layout.CSGOCategory,title->"categoryCSGO_"+title) {
			@Override
			protected IResizable getResizable (int width) {
				categoryResizable.set(resizable.apply(width));
				return categoryResizable.get();
			}
		};
		ILayout categoryCSGOLayout=new CSGOLayout(new Labeled(title,null),new Point(100,100),480,WIDTH,animation,"Enabled",false,false,2,ChildMode.POPUP,colorPopup) {
			@Override
			public int getScrollHeight (Context context, int componentHeight) {
				return resizableHeight.apply(categoryResizable.get()).getScrollHeight(null,height);
			}
		};
		categoryCSGOLayout.populateGUI(categoryCSGOAdder,csgoGenerator,client,theme);
		// Searchable CSGO
		AtomicReference<IResizable> searchableResizable= new AtomicReference<>(null);
		IComponentAdder searchableCSGOAdder=new PanelAdder(gui,true,()->ClickGUIModule.layout.getValue()==ClickGUIModule.Layout.SearchableCSGO,title->"searchableCSGO_"+title) {
			@Override
			protected IResizable getResizable (int width) {
				searchableResizable.set(resizable.apply(width));
				return searchableResizable.get();
			}
		};
		ILayout searchableCSGOLayout=new SearchableLayout(new Labeled(title,null),new Labeled("Search",null),new Point(100,100),480,WIDTH,animation,"Enabled",2,ChildMode.POPUP,colorPopup, Comparator.comparing(ILabeled::getDisplayName),charFilter,keys) {
			@Override
			public int getScrollHeight (Context context, int componentHeight) {
				return resizableHeight.apply(searchableResizable.get()).getScrollHeight(null,height);
			}
		};
		searchableCSGOLayout.populateGUI(searchableCSGOAdder,csgoGenerator,client,theme);
	}

	@Override
	public HUDGUI getGUI() {
		return gui;
	}

	@Override
	protected GUIInterface getInterface() {
		return inter;
	}

	@Override
	protected int getScrollSpeed() {
		return ClickGUIModule.scrollSpeed.getValue();
	}

	@Override
	public void handleKeyEvent(int scancode) {
		super.handleKeyEvent(scancode);
	}

	private class ThemeSelector implements IThemeMultiplexer {
		protected final Map<Theme,ITheme> themes= new EnumMap<>(Theme.class);
		
		public ThemeSelector (IInterface inter) {
			BooleanSetting clearGradient=new BooleanSetting("Gradient","gradient","Whether the title bars should have a gradient.",()->ClickGUIModule.theme.getValue()==Theme.Clear,true);
			BooleanSetting ignoreDisabled=new BooleanSetting("Ignore Disabled","ignoreDisabled","Have the rainbow drawn for disabled containers.",()->ClickGUIModule.theme.getValue()==Theme.Rainbow,false);
			BooleanSetting buttonRainbow=new BooleanSetting("Button Rainbow","buttonRainbow","Have a separate rainbow for each component.",()->ClickGUIModule.theme.getValue()==Theme.Rainbow,false);
			IntegerSetting rainbowGradient=new IntegerSetting("Rainbow Gradient","rainbowGradient","How fast the rainbow should repeat.",()->ClickGUIModule.theme.getValue()==Theme.Rainbow,150,50,300);
			ClickGUIModule.theme.addGlobalSubSetting(clearGradient);
			ClickGUIModule.theme.addGlobalSubSetting(ignoreDisabled);
			ClickGUIModule.theme.addGlobalSubSetting(buttonRainbow);
			ClickGUIModule.theme.addGlobalSubSetting(rainbowGradient);
			addTheme(Theme.Clear,new ClearTheme(new ThemeScheme(Theme.Clear), clearGradient::getValue,9,3,1,": "+ChatFormatting.GRAY));
			addTheme(Theme.GameSense,new GameSenseTheme(new ThemeScheme(Theme.GameSense),9,4,5,": "+ChatFormatting.GRAY));
			addTheme(Theme.Rainbow,new RainbowTheme(new ThemeScheme(Theme.Rainbow), ignoreDisabled::getValue, buttonRainbow::getValue, rainbowGradient::getValue,9,3,": "+ChatFormatting.GRAY));
			addTheme(Theme.Windows31,new Windows31Theme(new ThemeScheme(Theme.Windows31),9,2,9,": "+ChatFormatting.DARK_GRAY));
			addTheme(Theme.Impact,new ImpactTheme(new ThemeScheme(Theme.Impact),9,4));
		}
		
		@Override
		public ITheme getTheme() {
			return themes.getOrDefault(ClickGUIModule.theme.getValue(),themes.get(Theme.GameSense));
		}
		
		private void addTheme (Theme key, ITheme value) {
			themes.put(key,new OptimizedTheme(value));
			value.loadAssets(inter);
		}

		
		private class ThemeScheme implements IColorScheme {
			private final Theme themeValue;
			private final String themeName;
			
			public ThemeScheme (Theme themeValue) {
				this.themeValue=themeValue;
				this.themeName=themeValue.toString().toLowerCase();
			}
			
			@Override
			public void createSetting (ITheme theme, String name, String description, boolean hasAlpha, boolean allowsRainbow, Color color, boolean rainbow) {
				ClickGUIModule.theme.addGlobalSubSetting(new ColorSetting(name,themeName+"-"+name,description,()->ClickGUIModule.theme.getValue()==themeValue,hasAlpha,allowsRainbow,color,rainbow));
			}

			@Override
			public Color getColor (String name) {
				return ((ColorSetting)ClickGUIModule.theme.getAllSubSettings().filter(setting-> {
					if (setting instanceof ColorSetting setting1) {
						return setting1.getConfigName().equals(themeName + "-" + name);
					}else {
						return false;
					}
				}).findFirst().orElse(null)).getValue();
			}
		}
	}
}
