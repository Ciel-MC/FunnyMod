package hk.eric.funnymod.mixin.flags;

import hk.eric.funnymod.utils.HasFlag;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerboundMovePlayerPacket.class)
public abstract class ServerboundMovePlayerPacketFlag implements HasFlag {

    private boolean flag = false;

    @Override
    public boolean getFlag(HasFlag.Flags flag) {
        if (flag == HasFlag.Flags.SENT_BY_FUNNY_MOD) {
            return this.flag;
        }else {
            throw new IllegalArgumentException("Doesn't have this flag");
        }
    }

    @Override
    public void setFlag(HasFlag.Flags flag, Object value) {
        if (flag == HasFlag.Flags.SENT_BY_FUNNY_MOD) {
            this.flag = (boolean) value;
        }else {
            throw new IllegalArgumentException("Doesn't have this flag");
        }
    }
}
