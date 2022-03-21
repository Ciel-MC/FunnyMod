package hk.eric.funnymod.modules.mcqp.MCQPAura;

import hk.eric.ericLib.utils.classes.lamdba.TriConsumer;
import hk.eric.ericLib.utils.classes.lamdba.TriFunction;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public interface AuraMode {
    TriFunction<Stream<LivingEntity>, LocalPlayer, Double, List<LivingEntity>> getEntities();

    TriConsumer<LivingEntity, LocalPlayer, Consumer<Packet<?>>> getAttack();
}
