package hk.eric.funnymod.modules.combat.killaura;

import hk.eric.funnymod.gui.setting.EnumSetting;
import hk.eric.funnymod.modules.combat.KillAuraModule;

public class KillauraModes {

    private static final KillauraMode infinite = new InfiniteKillAuraMode();

    public static KillauraMode getMode(EnumSetting<KillAuraModule.KillAuraMode> setting) {
        return getMode(setting.getValue());
    }

    public static KillauraMode getMode(KillAuraModule.KillAuraMode mode) {
        return switch (mode) {
            case NORMAL -> null;
            case TELEPORT -> infinite;
        };
    }
}
