package hk.eric.funnymod.chat;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.fabric.FabricClientAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;

public class ChatManager {

    private static final Component prefix = Component.text("[").color(NamedTextColor.GRAY).append(Component.text("Funny").color(NamedTextColor.AQUA)).append(Component.text("]").color(NamedTextColor.GRAY)).append(Component.text(" "));
    private static final JoinConfiguration prefixConfig = JoinConfiguration.builder().prefix(prefix).build();

    private static final Audience client = FabricClientAudiences.of().audience();

    public static void sendMessage(String message) {
        sendMessage(message, true);
    }

    public static void sendMessage(String message, boolean prefix) {
        sendMessage(Component.text(message), prefix);
    }

    public static void sendMessage(Component message) {
        sendMessage(message, true);
    }

    public static void sendMessage(Component message, boolean prefix) {
        Component componentToSend = prefix? Component.join(prefixConfig, message) : message;
        client.sendMessage(componentToSend);
    }
    
    public static Component getPrefix() {
        return prefix;
    }
}
