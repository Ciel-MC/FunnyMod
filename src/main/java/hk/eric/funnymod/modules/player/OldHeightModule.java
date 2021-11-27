package hk.eric.funnymod.modules.player;

import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.funnymod.gui.setting.EnumSetting;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.gui.setting.settingWithSubSettings.BooleanSettingWithSubSetting;
import hk.eric.funnymod.modules.ToggleableModule;
import hk.eric.funnymod.utils.classes.TwoDimensionalEnumMap;
import hk.eric.funnymod.utils.classes.TypedPairList;

public class OldHeightModule extends ToggleableModule {

    private static final TwoDimensionalEnumMap<Version,HeightType,Float> HEIGHTS = TwoDimensionalEnumMap.of(Version.class, HeightType.class,
            new TypedPairList<>(Version.CURRENT, HeightType.NORMAL_HITBOX, 1.8F, HeightType.NORMAL_EYE_HEIGHT, 1.62F, HeightType.SNEAKING_HITBOX, 1.5F, HeightType.SNEAKING_EYE_HEIGHT, 1.27F),
            new TypedPairList<>(Version.OLD, HeightType.NORMAL_HITBOX, 1.8F, HeightType.NORMAL_EYE_HEIGHT, 1.8F * .85F, HeightType.SNEAKING_HITBOX, 1.65F, HeightType.SNEAKING_EYE_HEIGHT, 1.65F*.85F),
            new TypedPairList<>(Version.OLDER, HeightType.NORMAL_HITBOX, 1.8F, HeightType.NORMAL_EYE_HEIGHT, 1.62F, HeightType.SNEAKING_HITBOX, 1.8F, HeightType.SNEAKING_EYE_HEIGHT, 1.62F - 0.08F)
    );

    private static OldHeightModule instance;;
    public static final EnumSetting<Version> normal_hitbox = new EnumSetting<>("Normal hitbox version", "OldHeightNormalHitbox", "The size of your hitbox normally", Version.CURRENT, Version.class);
    public static final EnumSetting<Version> sneaking_hitbox = new EnumSetting<>("Sneaking hitbox version", "OldHeightSneakingHitbox", "The size of your hitbox when you sneak", Version.CURRENT, Version.class);
    public static final BooleanSettingWithSubSetting advancedSettings = new BooleanSettingWithSubSetting("Advanced settings", "OldHeightAdvancedSettings", "Advanced settings",false);
    public static final EnumSetting<Version> normal_eye = new EnumSetting<>("Normal eye height version", "OldHeightNormalEye", "The height of your eye when you sneak", Version.CURRENT, Version.class);
    public static final EnumSetting<Version> sneaking_eye = new EnumSetting<>("Sneaking eye height version", "OldHeightSneakingEye", "The height of your eye when you sneak", Version.CURRENT, Version.class);
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "OldHeightHeightKeybind", "", -1, () -> instance.toggle());

    public OldHeightModule() {
        super("Old heights", "Changes your hitbox and eye heights to previous versions");
        instance = this;
        normal_hitbox.setOnChange(version -> {
            if(!advancedSettings.isOn()) {
                normal_eye.setValue(version);
            }
        });
        sneaking_hitbox.setOnChange(version -> {
            if(!advancedSettings.isOn()) {
                sneaking_eye.setValue(version);
            }
        });
        settings.add(normal_hitbox);
        settings.add(sneaking_hitbox);
        settings.add(advancedSettings);
        advancedSettings.addSubSettings(true,normal_eye);
        advancedSettings.addSubSettings(true,sneaking_eye);
        settings.add(keybind);
    }

    public static IToggleable getToggle() {
        return instance.isEnabled();
    }

    public static float getHeight(HeightType type) {
        return switch (type) {
            case NORMAL_HITBOX -> getHeight(normal_hitbox.getValue(), type);
            case NORMAL_EYE_HEIGHT -> getHeight(normal_eye.getValue(), type);
            case SNEAKING_HITBOX -> getHeight(sneaking_hitbox.getValue(), type);
            case SNEAKING_EYE_HEIGHT -> getHeight(sneaking_eye.getValue(), type);
        };
    }

    public static float getHeight(Version version, HeightType type) {
        return HEIGHTS.get(version, type);
    }

    public enum HeightType {
        NORMAL_HITBOX,
        NORMAL_EYE_HEIGHT,
        SNEAKING_HITBOX,
        SNEAKING_EYE_HEIGHT
    }

    public enum Version {
        CURRENT("Current Version"),
        OLD("Pre 1.14"),
        OLDER("Pre 1.9");

        private final String name;

        Version(String versionName) {
            name = versionName;
        }

        public String getName() {
            return name;
        }
    }
}