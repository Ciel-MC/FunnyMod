package hk.eric.funnymod.gui.setting.settingWithSubSettings;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.setting.HasSubSettingsImpl;
import com.lukflug.panelstudio.setting.ISetting;
import hk.eric.funnymod.exceptions.ConfigLoadingFailedException;
import hk.eric.funnymod.gui.setting.DoubleSetting;
import hk.eric.funnymod.utils.SettingUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class DoubleSettingWithSubSetting extends DoubleSetting implements HasSubSettingsImpl<Double> {

    private final Map<Double, List<ISetting<?>>> subSettings = new HashMap<>();
    private final List<ISetting<?>> globalSubSettings = new ArrayList<>();

    public DoubleSettingWithSubSetting(String displayName, String configName, String description, double min, double max, double value) {
        super(displayName, configName, description, min, max, value);
    }

    public DoubleSettingWithSubSetting(String displayName, String configName, String description, IBoolean visible, double min, double max, double value) {
        super(displayName, configName, description, visible, min, max, value);
    }

    public DoubleSettingWithSubSetting(String displayName, String configName, String description, double min, double max, double value, Consumer<Double> onChange) {
        super(displayName, configName, description, min, max, value, onChange);
    }

    public DoubleSettingWithSubSetting(String displayName, String configName, String description, IBoolean visible, double min, double max, double value, Consumer<Double> onChange) {
        super(displayName, configName, description, visible, min, max, value, onChange);
    }

    public DoubleSettingWithSubSetting(String displayName, String configName, String description, double min, double max, double value, double step) {
        super(displayName, configName, description, min, max, value, step);
    }

    public DoubleSettingWithSubSetting(String displayName, String configName, String description, IBoolean visible, double min, double max, double value, double step) {
        super(displayName, configName, description, visible, min, max, value, step);
    }

    public DoubleSettingWithSubSetting(String displayName, String configName, String description, double min, double max, double value, double step, Consumer<Double> onChange) {
        super(displayName, configName, description, min, max, value, step, onChange);
    }

    public DoubleSettingWithSubSetting(String displayName, String configName, String description, IBoolean visible, double min, double max, double value, double step, Consumer<Double> onChange) {
        super(displayName, configName, description, visible, min, max, value, step, onChange);
    }

    @Override
    public List<ISetting<?>> getGlobalSubSettings() {
        return globalSubSettings;
    }

    @Override
    public Map<Double, List<ISetting<?>>> getSubSettingsMap() {
        return subSettings;
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
