package hk.eric.funnymod.gui;

import baritone.api.event.events.type.EventState;
import hk.eric.funnymod.event.EventListener;
import hk.eric.funnymod.event.EventManager;
import hk.eric.funnymod.event.events.KeyEvent;
import hk.eric.funnymod.event.events.TickEvent;
import hk.eric.funnymod.modules.Category;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public class Gui {
    private static ClickGUI gui;
    private boolean initialized = false;

    public void init() {
        Category.init();
        ClientTickEvents.START_CLIENT_TICK.register((startTick)->new TickEvent(EventState.PRE).call());
        ClientTickEvents.END_CLIENT_TICK.register((endTick)->new TickEvent(EventState.POST).call());
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!initialized) {
                gui = new ClickGUI();
                HudRenderCallback.EVENT.register((cli, tickDelta) -> gui.render());
                initialized = true;
            }
        });
        EventManager.getInstance().register(this);
    }

    @EventListener
    public void handleKey(KeyEvent event) {
        int key = event.getKey(), action = event.getAction();
        if(action == GLFW.GLFW_RELEASE || action == GLFW.GLFW_REPEAT) return;
        if(Minecraft.getInstance().screen != null) return;
        gui.handleKeyEvent(key);
    }

    public static ClickGUI getGUI() {
        return gui;
    }
}
