package hk.eric.funnymod.modules.visual;

import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.funnymod.gui.setting.BooleanSetting;
import hk.eric.funnymod.gui.setting.EnumSetting;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.modules.ToggleableModule;
import hk.eric.funnymod.utils.EntityUtil;
import net.minecraft.world.entity.Entity;

import java.util.function.Predicate;

public class EspModule extends ToggleableModule {

    private static EspModule instance;
    public static final BooleanSetting player = new BooleanSetting("Player", "EspPlayer", "shows players", true);
    public static final BooleanSetting hostile = new BooleanSetting("Hostile", "EspHostTile", "shows hostile entities", true);
    public static final BooleanSetting passive = new BooleanSetting("Passive", "EspPassive", "shows passive entities", true);
    public static final BooleanSetting neutral = new BooleanSetting("Neutral", "EspNeutral", "shows neutral entities", true);
    public static final EnumSetting<EspMode> ESP_MODE = new EnumSetting<>("ESP Mode", "EspMode", null, EspMode.GLOWING, EspMode.class);
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "EspKeybind", null, -1, () -> instance.toggle());

    public static final Predicate<Entity> shouldGlow = (entity) ->
            (hostile.isOn() && EntityUtil.isHostile(entity)) ||
                    (passive.isOn() && EntityUtil.isPassive(entity)) ||
                    (neutral.isOn() && EntityUtil.isNeutral(entity)) ||
                    (player.isOn() && EntityUtil.isPlayer(entity));

    public EspModule() {
        super("Esp", null);
        instance = this;
        settings.add(player);
        settings.add(hostile);
        settings.add(passive);
        settings.add(neutral);
        settings.add(ESP_MODE);
        settings.add(keybind);
    }

    public static IToggleable getToggle() {
        return instance.isEnabled();
    }

    public enum EspMode {
        GLOWING,
        BOXES,
        BOUNDING_BOXES,
        NONE
    }
}