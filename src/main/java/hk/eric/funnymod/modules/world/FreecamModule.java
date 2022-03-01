package hk.eric.funnymod.modules.world;

import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.modules.ToggleableModule;
import net.minecraft.client.player.RemotePlayer;

public class FreecamModule extends ToggleableModule {

    private static FreecamModule instance;
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "FreecamKeybind", null, () -> true, -1, () -> instance.toggle(), true);

    public FreecamModule() {
        super("Freecam", "Allows your camera to move away from your body", () -> true);
        instance = this;
        settings.add(keybind);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        RemotePlayer player = new RemotePlayer(getLevel(), getPlayer().getGameProfile());
        player.copyPosition(getPlayer());
        player.setOnGround(getPlayer().isOnGround());
        getLevel().addPlayer(-2137, player);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public static IToggleable getToggle() {
        return instance.getToggleable();
    }

}