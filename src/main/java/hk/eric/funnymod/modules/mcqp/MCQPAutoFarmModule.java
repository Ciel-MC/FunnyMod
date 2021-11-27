package hk.eric.funnymod.modules.mcqp;

import baritone.api.IBaritone;
import baritone.api.utils.BetterBlockPos;
import baritone.api.utils.BlockOptionalMeta;
import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.funnymod.FunnyModClient;
import hk.eric.funnymod.event.EventListener;
import hk.eric.funnymod.event.events.TickEvent;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.mixin.GuiMixin;
import hk.eric.funnymod.modules.ToggleableModule;
import hk.eric.funnymod.utils.PacketUtil;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;

public class MCQPAutoFarmModule extends ToggleableModule {

    private static MCQPAutoFarmModule instance;
    private int ticks = 0;
    private boolean wasPathing = false;
    private BetterBlockPos target;
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "MCQPAutoFarmKeybind", "", () -> true, -1, () -> instance.toggle());

    public MCQPAutoFarmModule() {
        super("MCQPAutoFarm", "Automatically farms on MCQP", () -> true);
        instance = this;
        settings.add(keybind);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        getBaritone().getGetToBlockProcess().getToBlock(new BlockOptionalMeta(Blocks.MELON));
    }

    @Override
    public void onDisable() {
        super.onDisable();
        getBaritone().getPathingBehavior().cancelEverything();
    }

    @EventListener
    public void toPathStuffAndThing(TickEvent event) {
        --ticks;
        if(ticks == 0) {
            ticks = 3;

            if(((GuiMixin) FunnyModClient.mc.gui).getTitle() == null) return;

            if (getBaritone().getPathingBehavior().isPathing() && !wasPathing) {
                target = getBaritone().getPathingBehavior().getCurrent().getPath().getDest();
            }

            if (!getBaritone().getPathingBehavior().isPathing() && wasPathing) {
                LocalPlayer p = FunnyModClient.mc.player;
                if (p == null) return;
                PacketUtil.sendPacket(new ServerboundUseItemOnPacket(InteractionHand.MAIN_HAND, new BlockHitResult(p.position(), p.getDirection(), target, false)));
                ticks = 20;
            }

            if(wasPathing != getBaritone().getPathingBehavior().isPathing()) {
                wasPathing = getBaritone().getPathingBehavior().isPathing();
            }
        }
    }

    public static IToggleable getToggle() {
        return instance.isEnabled();
    }
    
    private static IBaritone getBaritone() {
        return FunnyModClient.getBaritone();
    }
}