package com.lukflug.panelstudio.layout;

import com.lukflug.panelstudio.base.Animation;
import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.component.FocusableComponent;
import com.lukflug.panelstudio.component.HorizontalComponent;
import com.lukflug.panelstudio.component.IComponent;
import com.lukflug.panelstudio.component.IScrollSize;
import com.lukflug.panelstudio.container.HorizontalContainer;
import com.lukflug.panelstudio.container.IContainer;
import com.lukflug.panelstudio.container.VerticalContainer;
import com.lukflug.panelstudio.layout.ChildUtil.ChildMode;
import com.lukflug.panelstudio.popup.PopupTuple;
import com.lukflug.panelstudio.setting.*;
import com.lukflug.panelstudio.theme.ITheme;
import com.lukflug.panelstudio.theme.ThemeTuple;
import com.lukflug.panelstudio.widget.Button;
import com.lukflug.panelstudio.widget.RadioButton;
import com.lukflug.panelstudio.widget.ScrollBarComponent;
import com.lukflug.panelstudio.widget.ToggleButton;
import hk.eric.funnymod.utils.Constants;
import hk.eric.ericLib.utils.classes.getters.Getter;

import java.awt.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Adds components in a tab-based layout.
 * @author lukflug
 */
public class CSGOLayout implements ILayout,IScrollSize {
	/**
	 * The panel label.
	 */
	protected final ILabeled label;
	/**
	 * The panel position.
	 */
	protected final Point position;
	/**
	 * The panel width.
	 */
	protected final int width;
	/**
	 * The animation supplier.
	 */
	protected final Supplier<Animation> animation;
	/**
	 * The title for module toggles.
	 */
	protected final String enabledButton;
	/**
	 * Whether tab list is horizontal.
	 */
	protected final boolean horizontal;
	/**
	 * Whether settings are in a separate column.
	 */
	protected final boolean moduleColumn;
	/**
	 * The width of the category column.
	 */
	protected final int columnWidth;
	/**
	 * The weight of the settings column.
	 */
	protected final int weight;
	/**
	 * The child mode to use for setting components that are containers (e.g. color components).
	 */
	protected final ChildMode colorType;
	/**
	 * The child util instance.
	 */
	protected final ChildUtil util;
	
	/**
	 * Constructor.
	 * @param label panel label
	 * @param position panel position
	 * @param width panel width
	 * @param popupWidth pop-up width
	 * @param animation animation supplier
	 * @param enabledButton title for module toggles
	 * @param horizontal whether tab list is horizontal
	 * @param moduleColumn whether settings are in a separate column
	 * @param weight weight of the module column
	 * @param colorType child mode to use for setting components that are containers (e.g. color components)
	 * @param popupType child util instance
	 */
	public CSGOLayout (ILabeled label, Point position, int width, int popupWidth, Supplier<Animation> animation, String enabledButton, boolean horizontal, boolean moduleColumn, int weight, ChildMode colorType, PopupTuple popupType) {
		this(label,position,width,popupWidth,animation,enabledButton,horizontal,moduleColumn,-1,weight,colorType,popupType);
	}
	
	/**
	 * Constructor.
	 * @param label panel label
	 * @param position panel position
	 * @param width panel width
	 * @param popupWidth pop-up width
	 * @param animation animation supplier
	 * @param enabledButton title for module toggles
	 * @param horizontal whether tab list is horizontal
	 * @param moduleColumn whether settings are in a separate column
	 * @param columnWidth the width of the category column
	 * @param weight weight of the module column
	 * @param colorType child mode to use for setting components that are containers (e.g. color components)
	 * @param popupType child util instance
	 */
	public CSGOLayout (ILabeled label, Point position, int width, int popupWidth, Supplier<Animation> animation, String enabledButton, boolean horizontal, boolean moduleColumn, int columnWidth, int weight, ChildMode colorType, PopupTuple popupType) {
		this.label=label;
		this.position=position;
		this.width=width;
		this.animation=animation;
		this.enabledButton=enabledButton;
		this.horizontal=horizontal;
		this.moduleColumn=moduleColumn;
		this.columnWidth=columnWidth;
		this.weight=weight;
		this.colorType=colorType;
		util=new ChildUtil(popupWidth,animation,popupType);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void populateGUI (IComponentAdder gui, IComponentGenerator components, IClient client, ITheme theme) {
		Button<Void> title= new Button<>(label, () -> null, Getter.fixed(theme.getButtonRenderer(Void.class, 0, 0, true)));
		HorizontalContainer window=new HorizontalContainer(label,theme.getContainerRenderer(0,horizontal?1:0,true));
		IEnumSetting catSelect;
		if (horizontal) {
			VerticalContainer container=new VerticalContainer(label,theme.getContainerRenderer(0,0,false));
			catSelect=addContainer(label,client.getCategories().map(cat->cat),container,new ThemeTuple(theme,0,1),true,button->button);
			container.addComponent(window);
			gui.addComponent(title,container,new ThemeTuple(theme,0,0),position,width,animation);
		} else {
			catSelect=addContainer(label,client.getCategories().map(cat->cat),window,new ThemeTuple(theme,0,1),false,button->wrapColumn(button,new ThemeTuple(theme,0,1), Math.max(columnWidth, 0),columnWidth>0?0:1));
			gui.addComponent(title,window,new ThemeTuple(theme,0,0),position,width,animation);
		}
		client.getCategories().forEach(category->{
			if (moduleColumn) {
				IEnumSetting modSelect=addContainer(category,category.getModules().map(mod->mod),window,new ThemeTuple(theme,1,1),false,button->wrapColumn(button,new ThemeTuple(theme,0,1),0,1),()-> catSelect.getValueName().equals(category.getDisplayName()));
				category.getModules().forEach(module->{
					VerticalContainer container=new VerticalContainer(module,theme.getContainerRenderer(1,1,false));
					window.addComponent(wrapColumn(container,new ThemeTuple(theme,1,1),0,weight),()-> catSelect.getValueName().equals(category.getDisplayName()) && modSelect.getValueName().equals(module.getDisplayName()));
					if (module.getToggleable()!=null) container.addComponent(components.getComponent(new IBooleanSetting() {
						@Override
						public Boolean getValue() {
							return module.getToggleable().isOn();
						}

						@Override
						public void setValue(Boolean value) {
							if (module.getToggleable().isOn() != value) module.getToggleable().toggle();
						}

						@Override
						public String getDisplayName() {
							return enabledButton;
						}

						@Override
						public String getDescription() {
							return module.getDescription();
						}

						@Override
						public IBoolean isVisible() {
							return module.isVisible();
						}

						@Override
						public void toggle() {
							module.getToggleable().toggle();
						}

						@Override
						public boolean isOn() {
							return module.getToggleable().isOn();
						}
					},animation,gui,new ThemeTuple(theme,1,2),2,Getter.fixed(false)));
					module.getSettings().forEach(setting->addSettingsComponent(setting,container,gui,components,new ThemeTuple(theme,2,2)));
				});
			} else {
				VerticalContainer categoryContent=new VerticalContainer(category,theme.getContainerRenderer(0,1,false));
				window.addComponent(wrapColumn(categoryContent,new ThemeTuple(theme,0,1),0,weight),()-> catSelect.getValueName().equals(category.getDisplayName()));
				category.getModules().forEach(module->{
					int graphicalLevel=1;
					FocusableComponent moduleTitle;
					if (module.getToggleable()==null) moduleTitle= new Button<>(module, () -> null, Getter.fixed(theme.getButtonRenderer(Void.class, 1, 1, true)));
					else moduleTitle=new ToggleButton(module,module.getToggleable(), Getter.fixed(theme.getButtonRenderer(Boolean.class,1,1,true)));
					VerticalContainer moduleContainer=new VerticalContainer(module,theme.getContainerRenderer(1,graphicalLevel,false));
					if (module.getToggleable()==null) util.addContainer(module,moduleTitle,moduleContainer,()->null,Void.class,categoryContent,gui,new ThemeTuple(theme,1,graphicalLevel),ChildMode.DOWN);
					else util.addContainer(module,moduleTitle,moduleContainer,()->module.getToggleable().isOn(),Boolean.class,categoryContent,gui,new ThemeTuple(theme,1,graphicalLevel),ChildMode.DOWN);
					module.getSettings().forEach(setting->addSettingsComponent(setting,moduleContainer,gui,components,new ThemeTuple(theme,2,graphicalLevel+1)));
				});
			}
		});
	}
	
	/**
	 * Add a setting component.
	 * @param <T> the setting state type
	 * @param setting the setting to be added
	 * @param container the parent container
	 * @param gui the component adder for pop-ups
	 * @param components the component generator
	 * @param theme the theme to be used
	 */
	protected <T> void addSettingsComponent (ISetting<T> setting, VerticalContainer container, IComponentAdder gui, IComponentGenerator components, ThemeTuple theme) {
		int colorLevel=(colorType==ChildMode.DOWN)?theme.graphicalLevel:0;
		boolean isContainer = setting instanceof HasSubSettings;
		IComponent component;
		if (isContainer) {
			component=components.getComponent(setting,animation,gui,theme,colorLevel, () -> ((HasSubSettings<?>) setting).getCurrentSubSettings() != null);
		} else {
			component=components.getComponent(setting,animation,gui,theme,colorLevel, Getter.fixed(false));
		}
		if (component instanceof VerticalContainer colorContainer) {
			Button<T> button= new Button<>(setting, setting::getSettingState, Getter.fixed(theme.getButtonRenderer(setting.getSettingClass(), colorType == ChildMode.DOWN)));
			util.addContainer(setting,button,colorContainer, setting::getSettingState,setting.getSettingClass(),container,gui,new ThemeTuple(theme.theme,theme.logicalLevel,colorLevel),colorType);
			if (setting instanceof HasSubSettings<?> subSettingsSetting) subSettingsSetting.getSubSettings().forEach(subSetting->addSettingsComponent(subSetting,colorContainer,gui,components,new ThemeTuple(theme.theme,theme.logicalLevel+1,colorLevel+1)));
		} else if (setting instanceof HasSubSettings<?> hasSubSettings) {
			VerticalContainer settingContainer=new VerticalContainer(setting,theme.getContainerRenderer(false));
			util.addContainer(setting,component,settingContainer, setting::getSettingState,setting.getSettingClass(),container,gui,theme,ChildMode.DOWN);
			hasSubSettings.getSubSettings().forEach(subSetting->addSettingsComponent(subSetting,settingContainer,gui,components,new ThemeTuple(theme,1,1)));
		} else {
			container.addComponent(component);
		}
	}

	/**
	 * Add a multiplexing radio button list to a parent container.
	 * @param label the radio button label
	 * @param labels list of items to multiplex
	 * @param window the parent container
	 * @param theme the theme to be used
	 * @param horizontal whether radio button is horizontal
	 * @param container mapping from radio button to container component type instance
	 * @return the enum setting controlling the radio button list
	 */
	@SuppressWarnings("rawtypes")
	protected <T extends IComponent> IEnumSetting addContainer(ILabeled label, Stream<ILabeled> labels, IContainer<T> window, ThemeTuple theme, boolean horizontal, Function<RadioButton, T> container) {
		return addContainer(label, labels, window, theme, horizontal, container, Constants.alwaysTrue);
	}

	/**
	 * Add a multiplexing radio button list to a parent container.
	 * @param <T> parent container component type
	 * @param label the radio button label
	 * @param labels list of items to multiplex
	 * @param window the parent container
	 * @param theme the theme to be used
	 * @param horizontal whether radio button is horizontal
	 * @param container mapping from radio button to container component type instance
	 * @param visible radio buttons visibility predicate
	 * @return the enum setting controlling the radio button list
	 */
	@SuppressWarnings("rawtypes")
	protected <T extends IComponent> IEnumSetting addContainer (ILabeled label, Stream<ILabeled> labels, IContainer<T> window, ThemeTuple theme, boolean horizontal, Function<RadioButton,T> container, IBoolean visible) {
		IEnumSetting setting=new IEnumSetting() {
			private int state=0;
			private final ILabeled[] array =labels.toArray(ILabeled[]::new);
			
			@Override
			public String getDisplayName() {
				return label.getDisplayName();
			}
			
			@Override
			public String getDescription() {
				return label.getDescription();
			}
			
			@Override
			public IBoolean isVisible() {
				return label.isVisible();
			}

			@Override
			public void increment() {
				state=(state+1)%array.length;
			}
			
			@Override
			public void decrement() {
				state-=1;
				if (state<0) state=array.length-1;
			}

			@Override
			public String getValueName() {
				return array[state].getDisplayName();
			}

			@Override
			public void setValueIndex (int index) {
				state=index;
			}
			
			@Override
			public int getValueIndex() {
				return state;
			}

			@Override
			public ILabeled[] getAllowedValues() {
				return array;
			}

			@Override
			public Object getValue() {
				return null;
			}

			@Override
			public void setValue(Object value) {

			}

			@SuppressWarnings("rawtypes")
			@Override
			public Class getSettingClass() {
				return null;
			}
		};
		RadioButton button=new RadioButton(setting,theme.getRadioRenderer(true),animation.get(),horizontal) {
			@Override
			protected boolean isUpKey (int key) {
				if (horizontal) return isLeftKey(key);
				else return CSGOLayout.this.isUpKey(key);
			}

			@Override
			protected boolean isDownKey (int key) {
				if (horizontal) return isRightKey(key);
				else return CSGOLayout.this.isDownKey(key);
			}
		};
		window.addComponent(container.apply(button),visible);
		return setting;
	}
	
	/**
	 * Wrap content in a scrollable horizontal component to be added as a column. 
	 * @param button the content container
	 * @param theme the theme to be used
	 * @param width the horizontal width
	 * @param weight the horizontal weight
	 * @return a horizontal component
	 */
	protected HorizontalComponent<ScrollBarComponent<Void,IComponent>> wrapColumn (IComponent button, ThemeTuple theme, int width, int weight) {
		return new HorizontalComponent<>(new ScrollBarComponent<>(button, theme.getScrollBarRenderer(Void.class), theme.getEmptySpaceRenderer(Void.class, false), theme.getEmptySpaceRenderer(Void.class, true)) {
			@Override
			public int getScrollHeight(Context context, int componentHeight) {
				return CSGOLayout.this.getScrollHeight(context, componentHeight);
			}

			@Override
			protected Void getState() {
				return null;
			}
		}, width, weight);
	}
	
	/**
	 * Keyboard predicate for navigating up.
	 * @param key the key scancode
	 * @return whether key matches
	 */
	protected boolean isUpKey (int key) {
		return false;
	}
	
	/**
	 * Keyboard predicate for navigating down.
	 * @param key the key scancode
	 * @return whether key matches
	 */
	protected boolean isDownKey (int key) {
		return false;
	}
	
	/**
	 * Keyboard predicate for navigating left.
	 * @param key the key scancode
	 * @return whether key matches
	 */
	protected boolean isLeftKey (int key) {
		return false;
	}
	
	/**
	 * Keyboard predicate for navigating right.
	 * @param key the key scancode
	 * @return whether key matches
	 */
	protected boolean isRightKey (int key) {
		return false;
	}
}
