package hk.eric.funnymod.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientboundOpenScreenPacket.class)
public interface OpenClientboundOpenScreenPacket {
    @Accessor("type")
    @Mutable
    int getType();

    @Accessor("type")
    @Mutable
    void setType(int type);

    @Accessor("containerId")
    @Mutable
    void setContainerId(int id);

    @Accessor("title")
    @Mutable
    void setTitle(Component title);
}
