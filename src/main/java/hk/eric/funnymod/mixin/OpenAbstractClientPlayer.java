package hk.eric.funnymod.mixin;

import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractClientPlayer.class)
public interface OpenAbstractClientPlayer {
    @Accessor("playerInfo")
    PlayerInfo getPlayerInfo();

    @Accessor("playerInfo")
    void setPlayerInfo(PlayerInfo playerInfo);
}
