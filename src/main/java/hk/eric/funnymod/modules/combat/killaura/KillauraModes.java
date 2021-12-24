package hk.eric.funnymod.modules.combat.killaura;

import hk.eric.funnymod.gui.setting.EnumSetting;
import hk.eric.funnymod.modules.combat.KillAuraModule;

public class KillauraModes {

    private static final KillauraMode infinite = new InfiniteKillAuraMode();

    public static KillauraMode getMode(KillAuraModule module, EnumSetting<KillAuraModule.KillAuraMode> setting) {
        return switch (setting.getValue()) {
            case NORMAL -> null;
            case TELEPORT -> infinite;
        };
    }
}
