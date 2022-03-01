package hk.eric.funnymod.modules.mcqp;

import com.lukflug.panelstudio.base.IToggleable;
import hk.eric.funnymod.event.EventHandler;
import hk.eric.funnymod.event.events.PacketEvent;
import hk.eric.funnymod.gui.setting.BooleanSetting;
import hk.eric.funnymod.gui.setting.KeybindSetting;
import hk.eric.funnymod.modules.ToggleableModule;
import hk.eric.funnymod.utils.PacketUtil;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundCommandSuggestionPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;

public class MCQPatchModule extends ToggleableModule {

    private static double x = 0,y = 0,z = 0;
    private static float yaw = 0,bodyYaw = 0 ,pitch = 0;

    private static final EventHandler<PacketEvent.SendPacketEvent> packetEventSendPacketEventHandler = new EventHandler<>() {
        @Override
        public void handle(PacketEvent.SendPacketEvent event) {
            Packet<?> packet = event.getPacket();
            if (no_recommendation.isOn() && packet instanceof ServerboundCommandSuggestionPacket suggestionPacket && (suggestionPacket.getCommand().split(" ").length > 1 || suggestionPacket.getCommand().length() > 10)) {
                event.setCancelled(true);
            }
            if (no_move.isOn() && packet instanceof ServerboundMovePlayerPacket && !(packet instanceof ServerboundMovePlayerPacket.StatusOnly)) {
                event.setCancelled(true);
                PacketUtil.sendPacket(PacketUtil.createStatusOnly(getPlayer().isOnGround()));
            }
        }
    };

    private static MCQPatchModule instance;
    public static final BooleanSetting no_recommendation = new BooleanSetting("No Recommendation", "MCQPatchNoRecommendation", "No command recommendation to prevent disconnect.spam", false);
    public static final BooleanSetting no_move = new BooleanSetting("Moven't", "MCQPatchNoMove", "Prevent player from moving", false, (bl) -> {
        if (bl) {
            x = getPlayer().getX();
            y = getPlayer().getY();
            z = getPlayer().getZ();
            yaw = getPlayer().getYHeadRot();
            bodyYaw = getPlayer().getYRot();
            pitch = getPlayer().getXRot();
        } else {
            if (getPlayer() != null) {
                getPlayer().setPos(x, y, z);
                getPlayer().setYHeadRot(yaw);
                getPlayer().setYRot(bodyYaw);
                getPlayer().setXRot(pitch);
            }
        }
    });
    public static final KeybindSetting noMoveBind = new KeybindSetting("Moven't Toggle", "MCQPatchNoMoveToggle", "Toggle MCQPatch NoMove", -1, no_move::toggle);
    public static final BooleanSetting no_hits = new BooleanSetting("No hits", "MCQPatchNoHit", "No vanilla hits", false);
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "MCQPatchKeybind", null, () -> true, -1, () -> instance.toggle(), true);

    public MCQPatchModule() {
        super("MCQPatch", "Toggles some bypass for MCQP", () -> true);
        instance = this;

        settings.add(no_recommendation);
        settings.add(no_move);
        settings.add(noMoveBind);
        settings.add(keybind);

        registerOnOffHandler(packetEventSendPacketEventHandler);
    }

    public static IToggleable getToggle() {
        return instance.getToggleable();
    }

}