package hk.eric.funnymod.modules.misc;

import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.funnymod.FunnyModClient;
import hk.eric.funnymod.config.ConfigManager;
import hk.eric.funnymod.event.EventHandler;
import hk.eric.funnymod.event.events.PlayerChatEvent;
import hk.eric.funnymod.exceptions.ConfigLoadingFailedException;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.modules.Category;
import hk.eric.funnymod.modules.ToggleableModule;
import net.minecraft.network.chat.TextComponent;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class CommandModule extends ToggleableModule {

    private static CommandModule instance;
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "CommandKeybind", null, -1,()-> instance.toggle());

    private static final EventHandler<PlayerChatEvent> commandHandler = new EventHandler<PlayerChatEvent>() {
        @Override
        public void handle(PlayerChatEvent event) {
            String message = event.getMessage();
            if (message.startsWith(";")) {
                event.setCancelled(true);
                Queue<String> strings = new LinkedList<>(List.of(message.substring(1).split(" ")));
                String command = strings.remove();
                switch (command) {
                    case "config","cfg","c" -> {
                        switch (strings.remove()) {
                            case "save" -> {
                                ConfigManager.save(strings.remove());
                            }
                            case "load" -> {
                                try {
                                    ConfigManager.load(strings.remove());
                                } catch (ConfigLoadingFailedException e) {
                                    FunnyModClient.mc.gui.getChat().addMessage(new TextComponent("Some of all of the settings failed to load."));
                                }
                            }
                        }
                    }
                    case "toggle" -> Category.getAllModules().forEach(module -> {
                        if(command.equalsIgnoreCase(module.getDisplayName())) {
                            if(module instanceof IToggleable) {
                                ((IToggleable) module).toggle();
                            }
                        }
                    });
                    default -> FunnyModClient.mc.gui.getChat().addMessage(new TextComponent("Unknown command."));
                }
            }
        }
    };

    public CommandModule() {
        super("Command", "Commands");
        instance = this;
        settings.add(keybind);
    }

    public static IToggleable getToggle() {
        return instance.isEnabled();
    }

}