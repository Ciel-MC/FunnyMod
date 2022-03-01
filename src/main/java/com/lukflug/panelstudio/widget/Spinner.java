package com.lukflug.panelstudio.widget;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.base.IInterface;
import com.lukflug.panelstudio.base.SimpleToggleable;
import com.lukflug.panelstudio.component.HorizontalComponent;
import com.lukflug.panelstudio.container.HorizontalContainer;
import com.lukflug.panelstudio.container.VerticalContainer;
import com.lukflug.panelstudio.setting.INumberSetting;
import com.lukflug.panelstudio.setting.IStringSetting;
import com.lukflug.panelstudio.setting.Labeled;
import com.lukflug.panelstudio.theme.IContainerRenderer;
import com.lukflug.panelstudio.theme.ITheme;
import com.lukflug.panelstudio.theme.ThemeTuple;
import hk.eric.funnymod.utils.classes.getters.CachedLambdaGetter;
import hk.eric.funnymod.utils.classes.getters.Getter;

/**
 * A spinner for fine-tuning numerical settings.
 * @author lukflug
 */
public class Spinner extends HorizontalContainer {
	/**
	 * Constructor.
	 * @param setting the number setting to be used
	 * @param theme the theme to be used
	 * @param isContainer whether this is a title bar
	 * @param allowInput whether text input is allowed
	 * @param keys the keyboard predicates for the text box
	 */
	@SuppressWarnings("rawtypes")
	public Spinner (INumberSetting setting, ThemeTuple theme, Getter<Boolean> isContainer, boolean allowInput, ITextFieldKeys keys) {
		super(setting,new IContainerRenderer(){});
		TextField textField=new TextField(new IStringSetting() {
			private String value=null;
			private long lastTime;
			
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
				if (value!=null && System.currentTimeMillis()-lastTime>500) {
					if (value.isEmpty()) value="0";
					if (value.endsWith(".")) value+='0';
					double number=Double.parseDouble(value);
					if (number>setting.getMaximumValue()) number=setting.getMaximumValue();
					else if (number<setting.getMinimumValue()) number=setting.getMinimumValue();
					setting.setNumber(number);
					value=null;
				}
				if (value==null) return setting.getValueName();
				else return value;
			}

			@Override
			public void setValue (String string) {
				if (value==null) lastTime=System.currentTimeMillis();
				value= string;
			}
		},keys,0,new SimpleToggleable(false), new CachedLambdaGetter<>(bl -> theme.getTextRenderer(true, bl), isContainer, 2).toGetter()) {
			@Override
			public boolean allowCharacter(char character) {
				if (!allowInput) return false;
				return (character>='0' && character<='9') || (character=='.'&&!setting.getSettingState().contains("."));
			}
		};
		addComponent(new HorizontalComponent<>(textField,0,1));
		VerticalContainer buttons=new VerticalContainer(setting,new IContainerRenderer(){});
		buttons.addComponent(new Button<>(new Labeled(null, null), () -> null,  new CachedLambdaGetter<>(bl -> theme.getSmallButtonRenderer(ITheme.UP, bl), isContainer, 2)) {
			@Override
			public void handleButton(Context context, int button) {
				super.handleButton(context, button);
				if (button == IInterface.LBUTTON && context.isClicked(button)) {
					double number = setting.getNumber();
					number += setting.getStepAsDouble();
					if (number <= setting.getMaximumValue()) setting.setNumber(number);
				}
			}

			@Override
			public int getHeight() {
				return textField.getHeight() / 2;
			}
		});
		buttons.addComponent(new Button<>(new Labeled(null, null), () -> null, new CachedLambdaGetter<>(bl -> theme.getSmallButtonRenderer(ITheme.DOWN, bl), isContainer, 2)) {
			@Override
			public void handleButton(Context context, int button) {
				super.handleButton(context, button);
				if (button == IInterface.LBUTTON && context.isClicked(button)) {
					double number = setting.getNumber();
					number -= setting.getStepAsDouble();
					if (number >= setting.getMinimumValue()) setting.setNumber(number);
				}
			}

			@Override
			public int getHeight() {
				return textField.getHeight() / 2;
			}
		});
		addComponent(new HorizontalComponent<>(buttons, textField.getHeight(), 0) {
			@Override
			public int getWidth(IInterface inter) {
				return textField.getHeight();
			}
		});
	}
}
