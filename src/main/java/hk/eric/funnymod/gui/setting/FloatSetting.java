package hk.eric.funnymod.gui.setting;

import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.setting.INumberSetting;
import hk.eric.funnymod.utils.Constants;
import hk.eric.funnymod.utils.classes.Converters;
import hk.eric.funnymod.utils.classes.TwoWayFunction;

import java.util.function.Consumer;

public class FloatSetting extends SavableSetting<Float> implements INumberSetting<Float> {
    public final float min,max,step;

    public FloatSetting(String displayName, String configName, String description, float min, float max, float value) {
        this(displayName, configName, description, Constants.alwaysTrue, min, max, value);
    }

    public FloatSetting(String displayName, String configName, String description, IBoolean visible, float min, float max, float value) {
        this(displayName, configName, description, visible, min, max, value, null);
    }

    public FloatSetting(String displayName, String configName, String description, float min, float max, float value, Consumer<Float> onChange) {
        this(displayName, configName, description, Constants.alwaysTrue, min, max, value, onChange);
    }

    public FloatSetting(String displayName, String configName, String description, IBoolean visible, float min, float max, float value, Consumer<Float> onChange) {
        this(displayName, configName, description, visible, min, max, value, 0.01F, onChange);
    }

    public FloatSetting(String displayName, String configName, String description, float min, float max, float value, float step) {
        this(displayName, configName, description, Constants.alwaysTrue, min, max, value, step);
    }

    public FloatSetting(String displayName, String configName, String description, IBoolean visible, float min, float max, float value, float step) {
        this(displayName, configName, description, visible, min, max, value, step, null);
    }

    public FloatSetting(String displayName, String configName, String description, float min, float max, float value, float step, Consumer<Float> onChange) {
        this(displayName, configName, description, Constants.alwaysTrue, min, max, value, step, onChange);
    }

    public FloatSetting(String displayName, String configName, String description, IBoolean visible, float min, float max, float value, float step, Consumer<Float> onChange) {
        super(displayName,configName,description,visible,value,onChange);
        this.min=min;
        this.max=max;
        this.step=step;
    }

    @Override
    public double getNumber() {
        return getValue();
    }

    @Override
    public void setNumber(double value) {
        setValue(Math.round(value/step)*step);
    }

    @Override
    public double getMaximumValue() {
        return max;
    }

    @Override
    public double getMinimumValue() {
        return min;
    }

    @Override
    public Float getStep() {
        return step;
    }

    @Override
    public double getStepAsDouble() {
        return step;
    }

    @Override
    public Class<Float> getSettingClass() {
        return Float.class;
    }

    @Override
    public TwoWayFunction<Float, String> getConverter() {
        return Converters.FLOAT_CONVERTER;
    }
}
