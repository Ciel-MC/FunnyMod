package hk.eric.funnymod.gui.setting.settingWithSubSettings;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.setting.HasSubSettingsImpl;
import com.lukflug.panelstudio.setting.ISetting;
import hk.eric.funnymod.gui.setting.ColorSetting;
import hk.eric.funnymod.utils.SettingUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColorSettingWithSubSetting extends ColorSetting implements HasSubSettingsImpl<Color> {

    private final Map<Color, List<ISetting<?>>> subSettings = new HashMap<>();
    private final List<ISetting<?>> globalSubSettings = new ArrayList<>();

    public ColorSettingWithSubSetting(String displayName, String configName, String description, boolean hasAlpha, boolean allowsRainbow, Color value, boolean rainbow) {
        super(displayName, configName, description, hasAlpha, allowsRainbow, value, rainbow);
    }

    public ColorSettingWithSubSetting(String displayName, String configName, String description, IBoolean visible, boolean hasAlpha, boolean allowsRainbow, Color value, boolean rainbow) {
        super(displayName, configName, description, visible, hasAlpha, allowsRainbow, value, rainbow);
    }

    @Override
    public List<ISetting<?>> getGlobalSubSettings() {
        return globalSubSettings;
    }

    @Override
    public Map<Color, List<ISetting<?>>> getSubSettingsMap() {
        return subSettings;
    }

    @Override
    public ObjectNode save() {
        ObjectNode node = super.save();
        SettingUtil.getSavableStream(getAllSubSettings()).forEach((setting) -> node.set(setting.getConfigName(), setting.save()));
        return node;
    }

    @Override
    public void load(ObjectNode node){
        super.load(node);
        SettingUtil.getSavableStream(getAllSubSettings()).forEach(setting -> setting.load((ObjectNode) node.get(setting.getConfigName())));
    }
}
