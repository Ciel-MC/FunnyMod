package hk.eric.funnymod.gui.setting.settingWithSubSettings;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.setting.HasSubSettingsImpl;
import com.lukflug.panelstudio.setting.ISetting;
import hk.eric.funnymod.exceptions.ConfigLoadingFailedException;
import hk.eric.funnymod.gui.setting.IntegerSetting;
import hk.eric.funnymod.utils.SettingUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class IntegerSettingWithSubSetting extends IntegerSetting implements HasSubSettingsImpl<Integer> {

    private final Map<Integer, List<ISetting<?>>> subSettings = new HashMap<>();
    private final List<ISetting<?>> globalSubSettings = new ArrayList<>();

    public IntegerSettingWithSubSetting(String displayName, String configName, String description, Integer value, int min, int max) {
        super(displayName, configName, description, value, min, max);
    }

    public IntegerSettingWithSubSetting(String displayName, String configName, String description, IBoolean visible, Integer value, int min, int max) {
        super(displayName, configName, description, visible, value, min, max);
    }

    public IntegerSettingWithSubSetting(String displayName, String configName, String description, Integer value, int min, int max, Consumer<Integer> onChange) {
        super(displayName, configName, description, value, min, max, onChange);
    }

    public IntegerSettingWithSubSetting(String displayName, String configName, String description, IBoolean visible, Integer value, int min, int max, Consumer<Integer> onChange) {
        super(displayName, configName, description, visible, value, min, max, onChange);
    }

    @Override
    public List<ISetting<?>> getGlobalSubSettings() {
        return globalSubSettings;
    }

    @Override
    public Map<Integer, List<ISetting<?>>> getSubSettingsMap() {
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
