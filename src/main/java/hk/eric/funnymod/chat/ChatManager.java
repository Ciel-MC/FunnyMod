package hk.eric.funnymod.chat;

import hk.eric.funnymod.utils.classes.caches.Cache;
import hk.eric.funnymod.utils.classes.caches.HashMapCache;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.fabric.FabricClientAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

public class ChatManager {

    private static final Component prefixTemplate = Component.empty()
            .append(Component.text("[")).color(NamedTextColor.GRAY)
            .append(Component.text("Funny").color(NamedTextColor.AQUA));

    private static final Cache<String, JoinConfiguration> prefixCache = new HashMapCache<>((s) -> {
        if (s == null) {
            return JoinConfiguration.builder().prefix(
                    prefixTemplate
                    .append(Component.text("]").color(NamedTextColor.GRAY))
                    .append(Component.text(" "))
            ).build();
        }else {
            return JoinConfiguration.builder().prefix(prefixTemplate
                    .append(Component.text(" - ")).color(TextColor.color(50, 50, 50))/*.color(NamedTextColor.AQUA)*/
                    .append(Component.text(s).color(NamedTextColor.AQUA))
                    .append(Component.text("]").color(NamedTextColor.GRAY))
                    .append(Component.text(" "))
                    ).build();
        }
    });

    private static final JoinConfiguration prefixConfig = JoinConfiguration.builder().build();

    private static final Audience client = FabricClientAudiences.of().audience();

    public static void sendMessage(String message) {
        sendMessage(message, true);
    }

    public static void sendMessage(String module, String message) {
        sendMessage(module, message, true);
    }

    public static void sendMessage(String message, boolean prefix) {
        sendMessage(Component.text(message), prefix);
    }

    public static void sendMessage(String module, String message, boolean prefix) {
        sendMessage(module, Component.text(message), prefix);
    }

    public static void sendMessage(Component message) {
        sendMessage(message, true);
    }

    public static void sendMessage(String module, Component message) {
        sendMessage(module, message, true);
    }

    public static void sendMessage(Component message, boolean prefix) {
        sendMessage(null, message, prefix);
    }

    public static void sendMessage(String module, Component message, boolean prefix) {
        if (prefix) {
            client.sendMessage(Component.join(prefixCache.get(module), message));
        }else {
            client.sendMessage(message);
        }
    }
}
