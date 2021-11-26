package hk.eric.funnymod;

import com.viaversion.fabric.mc117.ViaFabric;
import hk.eric.funnymod.gui.Gui;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

import static net.fabricmc.api.EnvType.CLIENT;

@Environment(CLIENT)
public class FunnyModClient implements ClientModInitializer {

    /*TODO:
    *  Mob ESP
    *  Freecam
    *  Slot locking
    *  XP boost time left
    *  Stat change preview*/

    public static final Minecraft mc = Minecraft.getInstance();

    @Override
    public void onInitializeClient() {
        ViaFabric.config.setClientSideEnabled(true);
        new Gui().init();
    }

}
