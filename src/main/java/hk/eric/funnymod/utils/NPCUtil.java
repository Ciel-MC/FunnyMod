package hk.eric.funnymod.utils;

import hk.eric.funnymod.FunnyModClient;
import hk.eric.funnymod.mixin.OpenLevel;
import hk.eric.funnymod.modules.combat.killaura.InfiniteKillAuraMode;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public class NPCUtil {
    /**
     * @deprecated Will be moved to {@link PlayerUtil} clickEntity method.
     */
    @Deprecated(forRemoval = true)
    public static < E extends Entity> void clickNPCByName(String name, EntityType<E> type, MouseUtil.MouseButton mouseButton) {
        if (mouseButton == MouseUtil.MouseButton.MIDDLE) {
            throw new IllegalArgumentException("You can't use middle mouse button to click NPC!");
        }
        ClientLevel level = FunnyModClient.mc.level;
        assert level != null;
        ((OpenLevel) level).callGetEntities().getAll().forEach(entity -> {
            if (entity.getType() == type && entity.getName().getString().contains(name)) {
                if (mouseButton == MouseUtil.MouseButton.LEFT) {
                    PacketUtil.sendPacket(ServerboundInteractPacket.createAttackPacket(entity,false));
                }else {
                    InfiniteKillAuraMode.moveTo(FunnyModClient.mc.player, entity, 10000, PacketUtil::sendPacket, (list) -> true, ((node, node2) -> node.distanceFloat(node2, true) < 6), true);
                    PacketUtil.sendPacket(ServerboundInteractPacket.createInteractionPacket(entity, false, InteractionHand.MAIN_HAND, entity.position()));
                }
            }
        });
    }
}
