package hk.eric.funnymod.utils;

import hk.eric.funnymod.event.EventHandler;
import hk.eric.funnymod.event.EventManager;
import hk.eric.funnymod.event.events.ChatReceivedEvent;
import hk.eric.funnymod.event.events.GuiOpenEvent;
import hk.eric.funnymod.event.events.TitleEvents;
import org.jetbrains.annotations.Blocking;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class WaitUtils {

    public static List<Predicate<GuiOpenEvent>> waitingForNewScreen = new LinkedList<>();
    private static final EventHandler<GuiOpenEvent> waitForScreen = new EventHandler<>() {
        @Override
        public void handle(GuiOpenEvent event) {
            waitingForNewScreen.removeIf((predicate) -> predicate.test(event));
        }
    };

    public static List<Predicate<ChatReceivedEvent>> waitingForChat = new LinkedList<>();
    private static final EventHandler<ChatReceivedEvent> waitForChat = new EventHandler<>() {
        @Override
        public void handle(ChatReceivedEvent event) {
            waitingForChat.removeIf((predicate) -> predicate.test(event));
        }
    };

    public static List<Predicate<TitleEvents>> waitingForTitle = new LinkedList<>();
    public static final EventHandler<TitleEvents> waitForTitle = new EventHandler<>() {
        @Override
        public void handle(TitleEvents event) {
            waitingForTitle.removeIf((predicate) -> predicate.test(event));
        }
    };

    static {
        EventManager.getInstance().register(waitForScreen);
        EventManager.getInstance().register(waitForChat);
        EventManager.getInstance().register(waitForTitle);
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

    @Blocking
    public static void waitForTitle(Predicate<TitleEvents> predicate) {
        waitingForTitle.add(predicate);
        while (waitingForTitle.contains(predicate)) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Blocking
    public static void waitForTitle() {
        waitForChat((event) -> true);
    }
}
