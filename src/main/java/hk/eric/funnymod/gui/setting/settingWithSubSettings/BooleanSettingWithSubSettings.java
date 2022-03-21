package hk.eric.funnymod.gui.setting.settingWithSubSettings;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.setting.HasSubSettingsImpl;
import com.lukflug.panelstudio.setting.ISetting;
import hk.eric.funnymod.exceptions.ConfigLoadingFailedException;
import hk.eric.funnymod.gui.setting.BooleanSetting;
import hk.eric.funnymod.utils.SettingUtil;
import hk.eric.ericLib.utils.classes.Converters;
import hk.eric.ericLib.utils.classes.TwoWayFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class BooleanSettingWithSubSettings extends BooleanSetting implements HasSubSettingsImpl<Boolean> {

    private final Map<Boolean, List<ISetting<?>>> subSettings = new HashMap<>();
    private final List<ISetting<?>> globalSubSettings = new ArrayList<>();

    public BooleanSettingWithSubSettings(String displayName, String configName, String description, Boolean value, ISetting<?>... subSettings) {
        super(displayName, configName, description, value);
        addSubSettings(true, subSettings);
    }

    public BooleanSettingWithSubSettings(String displayName, String configName, String description, IBoolean visible, Boolean value, ISetting<?>... subSettings) {
        super(displayName, configName, description, visible, value);
        addSubSettings(true, subSettings);
    }

    public BooleanSettingWithSubSettings(String displayName, String configName, String description, Boolean value, Consumer<Boolean> onChange, ISetting<?>... subSettings) {
        super(displayName, configName, description, value, onChange);
        addSubSettings(true, subSettings);
    }

    public BooleanSettingWithSubSettings(String displayName, String configName, String description, IBoolean visible, Boolean value, Consumer<Boolean> onChange, ISetting<?>... subSettings) {
        super(displayName, configName, description, visible, value, onChange);
        addSubSettings(true, subSettings);
    }

    @Override
    public List<ISetting<?>> getGlobalSubSettings() {
        return globalSubSettings;
    }

    @Override
    public Map<Boolean, List<ISetting<?>>> getSubSettingsMap() {
        return subSettings;
    }

    @Override
    public TwoWayFunction<Boolean, String> getConverter() {
        return Converters.BOOLEAN_CONVERTER;
    }

    @Override
    public ObjectNode save() {
        ObjectNode node = super.save();
        SettingUtil.getSavableStream(getAllSubSettings()).forEach((setting) -> node.set(setting.getConfigName(), setting.save()));
        return node;
    }

    @Override
    public void load(ObjectNode node) throws ConfigLoadingFailedException {
        super.load(node);
        AtomicBoolean error = new AtomicBoolean(false);
        SettingUtil.getSavableStream(getAllSubSettings()).forEach(setting -> {
            try {
                setting.load((ObjectNode) node.get(setting.getConfigName()));
            } catch (ConfigLoadingFailedException e) {
                error.set(true);
            }
        });
        if (error.get()) {
            throw new ConfigLoadingFailedException();
        }
    }
}
