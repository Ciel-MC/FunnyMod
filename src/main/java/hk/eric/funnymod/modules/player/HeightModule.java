package hk.eric.funnymod.modules.player;

import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.funnymod.gui.setting.FloatSetting;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.gui.setting.settingWithSubSettings.BooleanSettingWithSubSetting;
import hk.eric.funnymod.gui.setting.settingWithSubSettings.EnumSettingWithSubSettings;
import hk.eric.funnymod.modules.ToggleableModule;
import hk.eric.funnymod.utils.classes.*;

public class HeightModule extends ToggleableModule {

    private static HeightModule instance;
    public static final EnumSettingWithSubSettings<Version> normal_hitbox = new EnumSettingWithSubSettings<>("Normal hitbox version", "OldHeightNormalHitbox", "The size of your hitbox normally", Version.CURRENT, Version.class);
    public static final EnumSettingWithSubSettings<Version> sneaking_hitbox = new EnumSettingWithSubSettings<>("Sneaking hitbox version", "OldHeightSneakingHitbox", "The size of your hitbox when you sneak", Version.CURRENT, Version.class);
    public static final FloatSetting custom_normal_Hitbox = new FloatSetting("Custom normal hitbox", "OldHeightCustomNormalHitbox", "Custom normal hitbox size", 0.0F, 10.0F, 1.8F);
    public static final FloatSetting custom_sneaking_Hitbox = new FloatSetting("Custom sneaking hitbox", "OldHeightCustomNormalHitbox", "Custom sneaking hitbox size", 0.0F, 10.0F, 1.5F);
    public static final BooleanSettingWithSubSetting advancedSettings = new BooleanSettingWithSubSetting("Advanced settings", "OldHeightAdvancedSettings", "Advanced settings",false);
    public static final EnumSettingWithSubSettings<Version> normal_eye = new EnumSettingWithSubSettings<>("Normal eye height version", "OldHeightNormalEye", "The height of your eye when you sneak", Version.CURRENT, Version.class);
    public static final EnumSettingWithSubSettings<Version> sneaking_eye = new EnumSettingWithSubSettings<>("Sneaking eye height version", "OldHeightSneakingEye", "The height of your eye when you sneak", Version.CURRENT, Version.class);
    public static final FloatSetting custom_normal_eye = new FloatSetting("Custom normal eye height", "OldHeightCustomNormalEye", "Custom normal eye height", 0.0F, 10.0F, 1.62F);
    public static final FloatSetting custom_sneaking_eye = new FloatSetting("Custom sneaking eye height", "OldHeightCustomSneakingEye", "Custom sneaking eye height", 0.0F, 10.0F, 1.27F);
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "OldHeightHeightKeybind", null, -1, () -> instance.toggle());


    private static final TwoDimensionalEnumMap<Version,HeightType, AtomicFloat> HEIGHTS = TwoDimensionalEnumMap.of(Version.class, HeightType.class,
            new TypedPairList<>(Version.CURRENT,
                    HeightType.NORMAL_HITBOX, new FinalAtomicFloat(1.8F),
                    HeightType.NORMAL_EYE_HEIGHT, new FinalAtomicFloat(1.62F),
                    HeightType.SNEAKING_HITBOX, new FinalAtomicFloat(1.5F),
                    HeightType.SNEAKING_EYE_HEIGHT, new FinalAtomicFloat(1.27F)
            ),
            new TypedPairList<>(Version.OLD,
                    HeightType.NORMAL_HITBOX, new FinalAtomicFloat(1.8F),
                    HeightType.NORMAL_EYE_HEIGHT, new FinalAtomicFloat(1.8F * .85F),
                    HeightType.SNEAKING_HITBOX, new FinalAtomicFloat(1.65F),
                    HeightType.SNEAKING_EYE_HEIGHT, new FinalAtomicFloat(1.65F*.85F)
            ),
            new TypedPairList<>(Version.OLDER,
                    HeightType.NORMAL_HITBOX, new FinalAtomicFloat(1.8F),
                    HeightType.NORMAL_EYE_HEIGHT, new FinalAtomicFloat(1.62F),
                    HeightType.SNEAKING_HITBOX, new FinalAtomicFloat(1.8F),
                    HeightType.SNEAKING_EYE_HEIGHT, new FinalAtomicFloat(1.62F - 0.08F)
            ),
            new TypedPairList<>(Version.Custom,
                    HeightType.NORMAL_HITBOX, new LinkedAtomicFloatImpl(custom_normal_Hitbox::setValue, custom_normal_Hitbox::getValue),
                    HeightType.NORMAL_EYE_HEIGHT, new LinkedAtomicFloatImpl(custom_normal_eye::setValue, custom_normal_eye::getValue),
                    HeightType.SNEAKING_HITBOX, new LinkedAtomicFloatImpl(custom_sneaking_Hitbox::setValue, custom_sneaking_Hitbox::getValue),
                    HeightType.SNEAKING_EYE_HEIGHT, new LinkedAtomicFloatImpl(custom_sneaking_eye::setValue, custom_sneaking_eye::getValue)
            )
    );

    public HeightModule() {
        super("Custom heights", "Changes your hitbox and eye heights to previous versions");
        instance = this;
        normal_hitbox.setOnChange(version -> {
            if (!advancedSettings.isOn()) {
                normal_eye.setValue(version);
            }
        });
        sneaking_hitbox.setOnChange(version -> {
            if (!advancedSettings.isOn()) {
                sneaking_eye.setValue(version);
            }
        });
        normal_hitbox.addSubSettings(Version.Custom, custom_normal_Hitbox);
        sneaking_hitbox.addSubSettings(Version.Custom, custom_sneaking_Hitbox);
        settings.add(normal_hitbox);
        settings.add(sneaking_hitbox);
        settings.add(advancedSettings);
        normal_eye.addSubSettings(Version.Custom, custom_normal_eye);
        sneaking_eye.addSubSettings(Version.Custom, custom_sneaking_eye);
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
        return HEIGHTS.get(version, type).get();
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
        OLDER("Pre 1.9"),
        Custom("Custom");

        private final String name;

        Version(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}