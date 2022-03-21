package com.lukflug.panelstudio.widget;

import com.lukflug.panelstudio.base.*;
import com.lukflug.panelstudio.component.HorizontalComponent;
import com.lukflug.panelstudio.component.IFixedComponent;
import com.lukflug.panelstudio.component.IScrollSize;
import com.lukflug.panelstudio.container.HorizontalContainer;
import com.lukflug.panelstudio.popup.IPopupPositioner;
import com.lukflug.panelstudio.setting.IEnumSetting;
import com.lukflug.panelstudio.setting.ILabeled;
import com.lukflug.panelstudio.setting.IStringSetting;
import com.lukflug.panelstudio.setting.Labeled;
import com.lukflug.panelstudio.theme.IContainerRenderer;
import com.lukflug.panelstudio.theme.ITheme;
import com.lukflug.panelstudio.theme.RendererTuple;
import com.lukflug.panelstudio.theme.ThemeTuple;
import hk.eric.ericLib.utils.classes.getters.CachedLambdaGetter;
import hk.eric.ericLib.utils.classes.getters.Getter;

import java.awt.*;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * Drop-down list widget.
 * @author lukflug
 */
public abstract class DropDownList extends HorizontalContainer {
	/**
	 * Cached input area.
	 */
	private Rectangle rect=new Rectangle();
	/**
	 * Whether focus has to be transferred to list pop-up.
	 */
	private boolean transferFocus=false;
	/**
	 * Toggle for whether the list pop-up is being displayed.
	 */
	protected final IToggleable toggle=new SimpleToggleable(false);
	
	/**
	 * Constructor.
	 * @param setting the enum setting to be used
	 * @param theme the theme to be used
	 * @param container whether this is a title bar
	 * @param allowSearch whether typing in the text box is allowed
	 * @param keys key predicates for the text box
	 * @param popupSize the scroll behavior of the list
	 * @param popupAdder consumer to handle adding list pop-up
	 */
	@SuppressWarnings("rawtypes")
	public DropDownList (IEnumSetting setting, ThemeTuple theme, Getter<Boolean> container, boolean allowSearch, ITextFieldKeys keys, IScrollSize popupSize, Consumer<IFixedComponent> popupAdder) {
		super(setting,new IContainerRenderer(){});
		AtomicReference<String> searchTerm= new AtomicReference<>(null);
		TextField textField=new TextField(new IStringSetting() {
			@Override
			public String getDisplayName() {
				return setting.getDisplayName();
			}

			@Override
			public String getDescription() {
				return setting.getDescription();
			}

			@Override
			public IBoolean isVisible() {
				return setting.isVisible();
			}

			@Override
			public String getValue() {
				String returnValue=(allowSearch&&toggle.isOn())?searchTerm.get():setting.getValueName();
				searchTerm.set(returnValue);
				return returnValue;
			}

			@Override
			public void setValue(String string) {
				searchTerm.set(string);
			}

			@Override
			public String getValueName() {
				return setting.getValueName();
			}

		},keys,0,new SimpleToggleable(false),new CachedLambdaGetter<>(str -> theme.getTextRenderer(false, str), container, 2).toGetter()) {
			@Override
			public void handleButton (Context context, int button) {
				super.handleButton(context,button);
				rect= rendererGetter.get().getTextArea(context,getTitle());
				if (button==IInterface.LBUTTON && context.isClicked(button)) transferFocus=true;
			}
			
			@Override
			public boolean hasFocus (Context context) {
				return super.hasFocus(context)||toggle.isOn();
			}
			
			@Override
			public boolean allowCharacter(char character) {
				return DropDownList.this.allowCharacter(character);
			}
		};
		addComponent(new HorizontalComponent<>(textField,0,1));
		ThemeTuple popupTheme=new ThemeTuple(theme.theme,theme.logicalLevel,0);
		Button<Void> title= new Button<>(new Labeled("", null, () -> false), () -> null, Getter.fixed(popupTheme.getButtonRenderer(Void.class, false)));
		RadioButton content=new RadioButton(new IEnumSetting() {
			final ILabeled[] values=Arrays.stream(setting.getAllowedValues()).map(value->new Labeled(value.getDisplayName(),value.getDescription(),()->{
				if (!value.isVisible().isOn()) return false;
				else if (!allowSearch) return true;
				else return value.getDisplayName().toUpperCase().contains(searchTerm.get().toUpperCase());
			})).toArray(ILabeled[]::new);
			
			@Override
			public String getDisplayName() {
				return setting.getDisplayName();
			}
			
			@Override
			public String getDescription() {
				return setting.getDescription();
			}
			
			
			@Override
			public IBoolean isVisible() {
				return setting.isVisible();
			}

			@Override
			public void increment() {
				setting.increment();
			}

			@Override
			public void decrement() {
				setting.decrement();
			}

			@Override
			public String getValueName() {
				return setting.getValueName();
			}

			@Override
			public void setValueIndex(int index) {
				setting.setValueIndex(index);
			}

			@Override
			public ILabeled[] getAllowedValues() {
				return values;
			}

			@Override
			public Object getValue() {
				return null;
			}

			@Override
			public void setValue(Object value) {
				//noinspection unchecked
				setting.setValue(value);
			}

			@SuppressWarnings("rawtypes")
			@Override
			public Class getSettingClass() {
				return setting.getSettingClass();
			}
		},popupTheme.getRadioRenderer(false),getAnimation(),false) {
			@Override
			protected boolean isUpKey(int key) {
				return DropDownList.this.isUpKey(key);
			}

			@Override
			protected boolean isDownKey(int key) {
				return DropDownList.this.isDownKey(key);
			}
		};
		IFixedComponent popup=ClosableComponent.createStaticPopup(title,content,()->null,getAnimation(), new RendererTuple<>(Void.class, popupTheme),popupSize,toggle,()->rect.width,false,"",true);
		popupAdder.accept(popup);
		IPopupPositioner positioner= (inter, popup1, component, panel) -> new Point(component.x,component.y + component.height);
		Button<Void> button= new Button<>(new Labeled(null, null), () -> null, new CachedLambdaGetter<>(bl -> theme.getSmallButtonRenderer(ITheme.DOWN, bl), container, 2)) {
			@Override
			public void handleButton(Context context, int button) {
				super.handleButton(context, button);
				rect = new Rectangle(rect.x, context.getPos().y, context.getPos().x + context.getSize().width - rect.x, context.getSize().height);
				if ((button == IInterface.LBUTTON && context.isClicked(button)) || transferFocus) {
					context.getPopupDisplayer().displayPopup(popup, rect, toggle, positioner);
					transferFocus = false;
				}
			}

			@Override
			public int getHeight() {
				return textField.getHeight();
			}
		};
		addComponent(new HorizontalComponent<>(button, textField.getHeight(), 0) {
			@Override
			public int getWidth(IInterface inter) {
				return textField.getHeight();
			}
		});
	}
	
	@Override
	public void handleKey (Context context, int scancode) {
		super.handleKey(context,scancode);
		if (toggle.isOn() && isEnterKey(scancode)) {
			toggle.toggle();
		}
	}
	
	/**
	 * Returns the animation to be used.
	 * @return the animation to be used.
	 */
	protected abstract Animation getAnimation();
	
	/**
	 * Character filter.
	 * @param character the character to check
	 * @return whether this character is allowed
	 */
	public abstract boolean allowCharacter (char character);
	
	/**
	 * Scancode predicate for moving selection up.
	 * @param key key scancode
	 * @return whether this key is to be interpreted as up
	 */
	protected abstract boolean isUpKey (int key);

	/**
	 * Scancode predicate for moving selection down.
	 * @param key key scancode
	 * @return whether this key is to be interpreted as down
	 */
	protected abstract boolean isDownKey (int key);
	
	/**
	 * Scancode predicate for selecting selection.
	 * @param key key scancode
	 * @return whether this key is to be interpreted as select
	 */
	protected abstract boolean isEnterKey (int key);
}
