package hk.eric.funnymod.mixin;

import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundChatPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.UUID;

@Mixin(ClientboundChatPacket.class)
public interface OpenClientboundChatPacket {
    @Accessor("message")
    @Mutable
    void setMessage(Component message);

    @Accessor("type")
    @Mutable
    void setType(ChatType type);

    @Accessor("sender")
    @Mutable
    void setSender(UUID uuid);
}
