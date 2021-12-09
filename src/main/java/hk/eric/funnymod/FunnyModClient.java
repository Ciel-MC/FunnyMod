package hk.eric.funnymod;

import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import com.viaversion.fabric.mc117.ViaFabric;
import hk.eric.funnymod.gui.Gui;
import hk.eric.funnymod.mixin.MixinConfigPlugin;
import hk.eric.simpleTCP.client.TCPClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;

import static net.fabricmc.api.EnvType.CLIENT;

@Environment(CLIENT)
public class FunnyModClient implements ClientModInitializer {

    public static final String MOD_ID = "funnymod";
    public static final String MOD_NAME = "Funny Mod";
    private static boolean initialized = false;
    private static IBaritone baritone;

    /*TODO:
    *  Auto farm
    *  Mob ESP - Semi done
    *  Freecam
    *  Slot locking
    *  Speed module
    *  XP boost time left
    *  Stat change preview*/

    public static final Minecraft mc = Minecraft.getInstance();

    @Override
    public void onInitializeClient() {
        if (!MixinConfigPlugin.isEnabled) return;
        ViaFabric.config.setClientSideEnabled(true);
        new Gui().init();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!initialized) {
                baritone = BaritoneAPI.getProvider().getPrimaryBaritone();
                initialized = true;
            }
        });
    }

    public static TCPClient getTcpClient() {
        return MixinConfigPlugin.client;
    }

    public static IBaritone getBaritone() {
        return baritone;
    }
}
