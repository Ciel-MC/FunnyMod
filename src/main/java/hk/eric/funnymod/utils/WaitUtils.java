package hk.eric.funnymod.utils;

import hk.eric.funnymod.event.EventHandler;
import hk.eric.funnymod.event.EventManager;
import hk.eric.funnymod.event.events.ChatReceivedEvent;
import hk.eric.funnymod.event.events.GuiOpenEvent;
import org.jetbrains.annotations.Blocking;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class WaitUtils {

    public static List<Predicate<GuiOpenEvent>> waitingForNewScreen = new ArrayList<>();
    private static final EventHandler<GuiOpenEvent> GUI_OPEN_EVENT_EVENT_HANDLER = new EventHandler<>() {
        @Override
        public void handle(GuiOpenEvent event) {
            waitingForNewScreen.removeIf((predicate) -> predicate.test(event));
        }
    };
    public static List<Predicate<ChatReceivedEvent>> waitingForChat = new ArrayList<>();
    private static final EventHandler<ChatReceivedEvent> chatReceivedEventHandler = new EventHandler<>() {
        @Override
        public void handle(ChatReceivedEvent event) {
            waitingForChat.removeIf((predicate) -> predicate.test(event));
        }
    };

    static {
        EventManager.getInstance().register(GUI_OPEN_EVENT_EVENT_HANDLER);
        EventManager.getInstance().register(chatReceivedEventHandler);
    }

    @Blocking
    public static void waitForNewScreen(Predicate<GuiOpenEvent> predicate) {
        try {
            waitingForNewScreen.add(predicate);
            while (waitingForNewScreen.contains(predicate)) {
                Thread.sleep(10);
            }
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Blocking
    public static void waitForNewScreen() {
        waitForNewScreen((guiOpenEvent) -> true);
    }

    @Blocking
    public static void waitForChat(Predicate<ChatReceivedEvent> predicate) {
        waitingForChat.add(predicate);
        while (waitingForChat.contains(predicate)) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Blocking
    public static void waitForChat() {
        waitForChat((chatReceivedEvent) -> true);
    }
}
