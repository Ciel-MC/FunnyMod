package hk.eric.funnymod.modules.misc;

import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.ericLib.utils.ClientPacketUtil;
import hk.eric.ericLib.utils.StringUtil;
import hk.eric.funnymod.chat.ChatManager;
import hk.eric.funnymod.config.ConfigManager;
import hk.eric.funnymod.event.EventHandler;
import hk.eric.funnymod.event.events.PlayerChatEvent;
import hk.eric.funnymod.exceptions.ConfigLoadingFailedException;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.modules.Category;
import hk.eric.funnymod.modules.ToggleableModule;
import net.minecraft.network.protocol.game.ServerboundChatPacket;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class CommandModule extends ToggleableModule {

    private static final String[] fuck = {
            "⢸⠉⠉⠁⠀⡇⠀⢸⠀⢰⠉⠉⠆⠀⡇⡠⠊",
            "⢸⠉⠉⠀⠀⡇⠀⢸⠀⢸⠀⠀⡀⠀⡟⢄⠀",
            "⠘⠀⠀⠀⠀⠑⠒⠊⠀⠈⠒⠒⠁⠀⠃⠀⠑"
    };

    private static CommandModule instance;
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "CommandKeybind", null, -1,()-> instance.toggle(), true);

    private static final EventHandler<PlayerChatEvent> commandHandler = new EventHandler<>() {
        @Override
        public void handle(PlayerChatEvent event) {
            if (event.getMessage().contains("{fuck}")) {
                for (String s : fuck) {
                    ClientPacketUtil.send(new ServerboundChatPacket(event.getMessage().replace("{fuck}", s)));
                }
                event.setCancelled(true);
            }
            handleCommand(event);
        }
    };

    public CommandModule() {
        super("Command", "Commands", true);
        instance = this;

        settings.add(keybind);

        registerOnOffHandler(commandHandler);
    }

    public static IToggleable getToggle() {
        return instance.getToggleable();
    }

    public static void handleCommand(PlayerChatEvent event) {
        String message = event.getMessage();
        if (message.startsWith(";")) {
            event.setCancelled(true);
            Queue<String> strings = new LinkedList<>(List.of(message.substring(1).split(" ")));
            String command = StringUtil.getString(strings);
            switch (command) {
                case "config","cfg","c" -> {
                    String subCommand = StringUtil.getString(strings);
                    switch (subCommand) {
                        case "save" -> ConfigManager.save(strings.poll());
                        case "load" -> {
                            try {
                                ConfigManager.load(strings.poll());
                            } catch (ConfigLoadingFailedException e) {
                                ChatManager.sendMessage("Some or all of the settings failed to load.");
                            }
                        }
                        default -> ChatManager.sendMessage("Available options: save, load");
                    }
                }
                case "toggle" -> Category.getAllModules().forEach(module -> {
                    if (command.equalsIgnoreCase(module.getDisplayName())) {
                        if (module instanceof IToggleable) {
                            ((IToggleable) module).toggle();
                        }
                    }
                });
                default -> ChatManager.sendMessage("Unknown command.");
            }
        }
    }
}