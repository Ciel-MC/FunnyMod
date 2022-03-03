package hk.eric.funnymod.utils;

import hk.eric.funnymod.event.EventHandler;
import hk.eric.funnymod.event.EventManager;
import hk.eric.funnymod.event.events.ChatReceivedEvent;
import hk.eric.funnymod.event.events.GuiOpenEvent;
import hk.eric.funnymod.event.events.TitleEvents;
import hk.eric.funnymod.utils.classes.pairs.Pair;
import org.jetbrains.annotations.Blocking;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;

public class WaitUtils {

    public static final List<Pair<Predicate<GuiOpenEvent>, CompletableFuture<Void>>> waitingForNewScreen = new LinkedList<>();
    private static final EventHandler<GuiOpenEvent> waitForScreen = new EventHandler<>() {
        @Override
        public void handle(GuiOpenEvent event) {
            waitingForNewScreen.removeIf(pair -> {
                if (pair.getFirst().test(event)) {
                    pair.getSecond().complete(null);
                    return true;
                }else {
                    return false;
                }
            });
        }
    };

    public static final List<Pair<Predicate<ChatReceivedEvent>, CompletableFuture<Void>>> waitingForChat = new LinkedList<>();
    private static final EventHandler<ChatReceivedEvent> waitForChat = new EventHandler<>() {
        @Override
        public void handle(ChatReceivedEvent event) {
            waitingForChat.removeIf(pair -> {
                if (pair.getFirst().test(event)) {
                    pair.getSecond().complete(null);
                    return true;
                }else {
                    return false;
                }
            });
        }
    };

    public static final List<Pair<Predicate<TitleEvents.TitleEvent>, CompletableFuture<Void>>> waitingForTitle = new LinkedList<>();
    public static final EventHandler<TitleEvents.TitleEvent> waitForTitle = new EventHandler<>() {
        @Override
        public void handle(TitleEvents.TitleEvent event) {
            waitingForTitle.removeIf(pair -> {
                if (pair.getFirst().test(event)) {
                    pair.getSecond().complete(null);
                    return true;
                }else {
                    return false;
                }
            });
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
            CompletableFuture<Void> future = new CompletableFuture<>();
            waitingForNewScreen.add(new Pair<>(predicate, future));
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Blocking
    public static void waitForNewScreen() {
        waitForNewScreen((guiOpenEvent) -> true);
    }

    @Blocking
    public static void waitForChat(Predicate<ChatReceivedEvent> predicate) {
        try {
            CompletableFuture<Void> future = new CompletableFuture<>();
            waitingForChat.add(new Pair<>(predicate, future));
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Blocking
    public static void waitForChat() {
        waitForChat((chatReceivedEvent) -> true);
    }

    @Blocking
    public static void waitForTitle(Predicate<TitleEvents.TitleEvent> predicate) {
        try {
            CompletableFuture<Void> future = new CompletableFuture<>();
            waitingForTitle.add(new Pair<>(predicate, future));
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Blocking
    public static void waitForTitle() {
        waitForChat((event) -> true);
    }
}
