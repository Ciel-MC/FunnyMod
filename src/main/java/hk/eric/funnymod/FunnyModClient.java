package hk.eric.funnymod;

import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import com.mojang.blaze3d.vertex.PoseStack;
import hk.eric.funnymod.event.FabricEventAdapter;
import hk.eric.funnymod.gui.Gui;
import hk.eric.funnymod.mixin.MixinConfigPlugin;
import hk.eric.simpleTCP.client.TCPClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

import static net.fabricmc.api.EnvType.CLIENT;

@Environment(CLIENT)
public class FunnyModClient implements ClientModInitializer {

    public static final String MOD_ID = "funnymod";
    public static final String MOD_NAME = "Funny Mod";
    public static final boolean debug = FabricLoader.getInstance().isDevelopmentEnvironment();
    private static boolean initialized = false;
    private static IBaritone baritone;
    private static PoseStack poseStack;

    public static final Minecraft mc = Minecraft.getInstance();

    @Override
    public void onInitializeClient() {
        if (!MixinConfigPlugin.isEnabled) return;
//        ViaFabric.config.setClientSideEnabled(true);
        new Gui().init();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!initialized) {
                baritone = BaritoneAPI.getProvider().getPrimaryBaritone();
                initialized = true;
            }
        });
        FabricEventAdapter.init();
    }

    public static PoseStack getPoseStack() {
        return poseStack;
    }

    public static void setPoseStack(PoseStack poseStack) {
        FunnyModClient.poseStack = poseStack;
    }

    @SuppressWarnings("rawtypes")
    public static TCPClient getTcpClient() {
        return null;
    }

    public static IBaritone getBaritone() {
        return baritone;
    }

    public static LocalPlayer player() {
        return mc.player;
    }
}
