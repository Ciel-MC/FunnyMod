package hk.eric.funnymod.event;

import hk.eric.funnymod.event.events.UseItemEvent;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FabricEventAdapter {

    public static void init() {
        UseItemCallback.EVENT.register(FabricEventAdapter::useItemEventAdapter);
    }

    private static InteractionResultHolder<ItemStack> useItemEventAdapter(Player p, Level level, InteractionHand hand) {
        ItemStack item = p.getItemInHand(hand);
        UseItemEvent event = new UseItemEvent(item);
        if (!Thread.currentThread().getName().equals("Server Thread"))
            if (!event.call().isCancelled())
                return InteractionResultHolder.pass(event.getItem());
        return InteractionResultHolder.fail(item);
    }
}
