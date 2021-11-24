package hk.eric.funnymod;

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
        new Gui().init();
    }

}
