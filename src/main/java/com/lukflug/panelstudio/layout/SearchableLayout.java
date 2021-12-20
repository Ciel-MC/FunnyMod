package com.lukflug.panelstudio.layout;

import com.lukflug.panelstudio.base.Animation;
import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.base.IBoolean;
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
import com.lukflug.panelstudio.widget.ITextFieldKeys;
import com.lukflug.panelstudio.widget.ScrollBarComponent;
import com.lukflug.panelstudio.widget.SearchableRadioButton;
import hk.eric.funnymod.utils.Constants;

import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Adds components in a tab-based layout, where modules are organized flat (categories bypassed), with a search bar.
 * @author lukflug
 */
public class SearchableLayout implements ILayout,IScrollSize {
	/**
	 * The panel label.
	 */
	protected final ILabeled titleLabel;
	/**
	 * The search bar label.
	 */
	protected final ILabeled searchLabel;
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
	 * The sorting comparison method.
	 */
	protected final Comparator<IModule> comparator;
	/**
	 * The character filter for the search bar.
	 */
	protected final IntPredicate charFilter;
	/**
	 * The function key predicates for the search bar. 
	 */
	protected final ITextFieldKeys keys;
	
	/**
	 * Constructor.
	 * @param titleLabel panel label
	 * @param searchLabel search bar label
	 * @param position panel position
	 * @param width panel width
	 * @param popupWidth pop-up width
	 * @param animation animation supplier
	 * @param enabledButton title for module toggles
	 * @param weight weight of the module column
	 * @param colorType child mode to use for setting components that are containers (e.g. color components)
	 * @param popupType child util instance
	 * @param comparator sorting comparison method
	 * @param charFilter character filter for the search bar
	 * @param keys function key predicates for the search bar
	 */
	public SearchableLayout (ILabeled titleLabel, ILabeled searchLabel, Point position, int width, int popupWidth, Supplier<Animation> animation, String enabledButton, int weight, ChildMode colorType, PopupTuple popupType, Comparator<IModule> comparator, IntPredicate charFilter, ITextFieldKeys keys) {
		this.titleLabel=titleLabel;
		this.searchLabel=searchLabel;
		this.position=position;
		this.width=width;
		this.animation=animation;
		this.enabledButton=enabledButton;
		this.weight=weight;
		this.colorType=colorType;
		this.comparator=comparator;
		this.charFilter=charFilter;
		this.keys=keys;
		util=new ChildUtil(popupWidth,animation,popupType);
	}
	
	@Override
	public void populateGUI (IComponentAdder gui, IComponentGenerator components, IClient client, ITheme theme) {
		Button<Void> title= new Button<>(titleLabel, () -> null, theme.getButtonRenderer(Void.class, 0, 0, true));
		HorizontalContainer window=new HorizontalContainer(titleLabel,theme.getContainerRenderer(0,0,true));
		Supplier<Stream<IModule>> modules=()->client.getCategories().flatMap(ICategory::getModules).sorted(comparator);
		IEnumSetting modSelect=addContainer(searchLabel,modules.get().map(mod->mod),window,new ThemeTuple(theme,0,1),button->wrapColumn(button,new ThemeTuple(theme,0,1),1));
		gui.addComponent(title,window,new ThemeTuple(theme,0,0),position,width,animation);
		modules.get().forEach(module->{
			VerticalContainer container=new VerticalContainer(module,theme.getContainerRenderer(1,1,false));
			window.addComponent(wrapColumn(container,new ThemeTuple(theme,1,1),weight),()-> modSelect.getValueName().equals(module.getDisplayName()));
			if (module.isEnabled()!=null) container.addComponent(components.getComponent(new IBooleanSetting() {
				@Override
				public Boolean getValue() {
					return module.isEnabled().isOn();
				}

				@Override
				public void setValue(Boolean value) {
					if (module.isEnabled().isOn()!=value) module.isEnabled().toggle();
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
					module.isEnabled().toggle();
				}

				@Override
				public boolean isOn() {
					return module.isEnabled().isOn();
				}
			},animation,gui,new ThemeTuple(theme,1,2),2,false));
			module.getSettings().forEach(setting->addSettingsComponent(setting,container,gui,components,new ThemeTuple(theme,2,2)));
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
		boolean isContainer=setting instanceof HasSubSettings;
		IComponent component=components.getComponent(setting,animation,gui,theme,colorLevel,isContainer);
		if (component instanceof VerticalContainer colorContainer) {
			Button<T> button= new Button<>(setting, setting::getSettingState, theme.getButtonRenderer(setting.getSettingClass(), colorType == ChildMode.DOWN));
			util.addContainer(setting,button,colorContainer,()->setting.getSettingState(),setting.getSettingClass(),container,gui,new ThemeTuple(theme.theme,theme.logicalLevel,colorLevel),colorType);
			if (setting instanceof HasSubSettings<?> hasSubSettings) hasSubSettings.getSubSettings().forEach(subSetting->addSettingsComponent(subSetting,colorContainer,gui,components,new ThemeTuple(theme.theme,theme.logicalLevel+1,colorLevel+1)));
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
	 * @param container mapping from radio button to container component type instance
	 * @return the enum setting controlling the radio button list
	 */
	protected <T extends IComponent> IEnumSetting addContainer(ILabeled label, Stream<ILabeled> labels, IContainer<T> window, ThemeTuple theme, Function<SearchableRadioButton, T> container) {
		return addContainer(label, labels, window, theme, container, Constants.alwaysTrue);
	}

	/**
	 * Add a multiplexing radio button list to a parent container.
	 * @param <T> parent container component type
	 * @param label the radio button label
	 * @param labels list of items to multiplex
	 * @param window the parent container
	 * @param theme the theme to be used
	 * @param container mapping from radio button to container component type instance
	 * @param visible radio buttons visibility predicate
	 * @return the enum setting controlling the radio button list
	 */
	protected <T extends IComponent> IEnumSetting addContainer (ILabeled label, Stream<ILabeled> labels, IContainer<T> window, ThemeTuple theme, Function<SearchableRadioButton,T> container, IBoolean visible) {
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
				return array[state];
			}

			@Override
			public void setValue(Object value) {
				state= Arrays.asList(array).indexOf(value);
			}

			@Override
			public Class getSettingClass() {
				return Enum.class;
			}
		};
		SearchableRadioButton button=new SearchableRadioButton(setting,theme,true,keys) {
			@Override
			protected Animation getAnimation() {
				return animation.get();
			}

			@Override
			public boolean allowCharacter(char character) {
				return charFilter.test(character);
			}
			
			@Override
			protected boolean isUpKey (int key) {
			return SearchableLayout.this.isUpKey(key);
			}

			@Override
			protected boolean isDownKey (int key) {
				return SearchableLayout.this.isDownKey(key);
			}
		};
		window.addComponent(container.apply(button),visible);
		return setting;
	}
	
	/**
	 * Wrap content in a scrollable horizontal component to be added as a column. 
	 * @param button the content container
	 * @param theme the theme to be used
	 * @param weight the horizontal weight
	 * @return a horizontal component
	 */
	protected HorizontalComponent<ScrollBarComponent<Void,IComponent>> wrapColumn (IComponent button, ThemeTuple theme, int weight) {
		return new HorizontalComponent<>(new ScrollBarComponent<>(button, theme.getScrollBarRenderer(Void.class), theme.getEmptySpaceRenderer(Void.class, false), theme.getEmptySpaceRenderer(Void.class, true)) {
			@Override
			public int getScrollHeight(Context context, int componentHeight) {
				return SearchableLayout.this.getScrollHeight(context, componentHeight);
			}

			@Override
			protected Void getState() {
				return null;
			}
		}, 0, weight);
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
}
