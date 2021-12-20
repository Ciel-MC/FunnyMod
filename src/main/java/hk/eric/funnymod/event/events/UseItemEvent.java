package hk.eric.funnymod.event.events;

import hk.eric.funnymod.event.EventCancellable;
import net.minecraft.world.item.ItemStack;

public class UseItemEvent extends EventCancellable {

    //Fire the event using UseItemCallback

    private final ItemStack item;

    public UseItemEvent(ItemStack item) {
        this.item = item;
    }

    public ItemStack getItem() {
        return item;
    }

    @Override
    public EventCancellable call() {
        return super.call();
    }
}
