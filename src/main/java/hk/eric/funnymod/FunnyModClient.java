package hk.eric.funnymod;

import com.viaversion.fabric.mc117.ViaFabric;
import com.viaversion.fabric.mc117.ViaFabricClient;
import hk.eric.funnymod.gui.Gui;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

import static net.fabricmc.api.EnvType.CLIENT;

@Environment(CLIENT)
public class FunnyModClient implements ClientModInitializer {

    public static Minecraft mc = Minecraft.getInstance();

    @Override
    public void onInitializeClient() {
        ViaFabric.config.setClientSideEnabled(true);
        new Gui().init();
    }

}
