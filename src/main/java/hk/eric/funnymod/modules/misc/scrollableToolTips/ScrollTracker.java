package hk.eric.funnymod.modules.misc.scrollableToolTips;

import hk.eric.funnymod.modules.misc.TooltipScrollingModule;
import net.minecraft.network.chat.Component;

import java.util.List;

public class ScrollTracker {
    // render functions are called every frame, so the offset needs to be saved somewhere
    public static int currentXOffset = 0;
    public static int currentYOffset = 0;
    
    // save the currently selected item, the scroll offset will reset if the user hovers over a different item
    private static List<Component> currentItem;

    private static final int scrollSize = 5;

    public static void scroll(ScrollDirection direction) {
        switch (direction) {
            case UP -> currentYOffset -= scrollSize;
            case DOWN -> currentYOffset += scrollSize;
            case LEFT -> currentXOffset -= scrollSize;
            case RIGHT -> currentXOffset += scrollSize;
        }
    }

    private static void resetScroll () {
        currentXOffset = 0;
        currentYOffset = 0;
    }

    public static int getOffset(Offset offset) {
        if (TooltipScrollingModule.getToggle().isOn()) {
            return switch (offset) {
                case X -> currentXOffset;
                case Y -> currentYOffset;
            };
        }else {
            return 0;
        }
    }

    // Custom equality function because a standard .equals didn't work.
    private static boolean isEqual (List<Component> item1, List<Component> item2) {
        if (item1 == null || item2 == null || item1.size() != item2.size()) {
            return false;
        }

        for (int i = 0; i < item1.size(); ++i) {
            if (!item1.get(i).getString().equals(item2.get(i).getString())) {
                return false;
            }
        }
        return true;
    }

    // Reset the tracker to default values.
    public static void reset () {
        resetScroll();
        currentItem = null; // Using null instead of just clearing the list because not all of Minecraft's Component Lists can be cleared for some reason and that can cause an error.
    }

    public static void setItem (List<Component> item) {
        if (!isEqual(currentItem, item)) {
            resetScroll();
            currentItem = item;
        }
    }

    public enum Offset {
        X, Y
    }

    public enum ScrollDirection {
        UP, DOWN, LEFT, RIGHT
    }
}