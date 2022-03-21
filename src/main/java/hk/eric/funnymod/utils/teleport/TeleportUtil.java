package hk.eric.funnymod.utils.teleport;

import hk.eric.ericLib.utils.ClientPacketUtil;
import hk.eric.ericLib.utils.MathUtil;
import hk.eric.ericLib.utils.classes.Offset;
import hk.eric.ericLib.utils.classes.minecraftPlus.BetterBlockPos;
import hk.eric.ericLib.utils.classes.pathFind.AStarPathFinder;
import hk.eric.ericLib.utils.classes.pathFind.Node;
import hk.eric.funnymod.FunnyModClient;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public class TeleportUtil {
    public static void teleport(TeleportSetting setting) {
        if (setting.pathfind()) {
            Node path = AStarPathFinder.search(new BetterBlockPos(setting.startingPos()).asNode(), new BetterBlockPos(setting.position()).asNode(), setting.maxStep(), setting.diagonal(), setting.ended(), !setting.goIfFail(), FunnyModClient.mc.level);
            if (path != null) {
                List<Node> nodes = path.asList();
                nodes.remove(nodes.size() - 1);
                if (nodes.isEmpty() || !setting.canMove().test(nodes)) {
                    return;
                }
                Queue<Node> queue = new ArrayDeque<>(nodes);
                int counter = 0;
                Node oNode = queue.poll();
                while (!queue.isEmpty()) {
                    Node node = queue.poll();
                    Vec3 pos = node.getPos().toCenteredVec3();
                    if (counter % setting.stepSize() != 0) {
                        if (!(setting.corner() && isCorner(oNode.getPos(), node.getPos(), queue.peek() != null ? queue.peek().getPos() : null))) {
                            continue;
                        }
                    }
                    ClientPacketUtil.ServerboundMovePlayerPacketBuilder builder = ClientPacketUtil.movePlayerPacketBuilder();
                    builder.setPos(pos);
                    if (setting.lookAt()) {
                        builder.setRot(MathUtil.getLookAtRotation(setting.lookAnchor().apply(pos), setting.lookAtPos()));
                    }
                    setting.packetSender().accept(builder.build());
                    oNode = node;
                    counter++;
                }
            }
        }else {
            ClientPacketUtil.ServerboundMovePlayerPacketBuilder builder = ClientPacketUtil.movePlayerPacketBuilder();
            builder.setPos(new BetterBlockPos(setting.position()).toCenteredVec3());
            if (setting.lookAt()) {
                builder.setRot(MathUtil.getLookAtRotation(setting.lookAnchor().apply(setting.position()), setting.lookAtPos()));
            }
            setting.packetSender().accept(builder.build());
        }
    }

    private static boolean isCorner(BetterBlockPos oPos, BetterBlockPos pos, BetterBlockPos nextPos) {
        if (oPos == null || pos == null || nextPos == null) {
            return false;
        }
        return !Offset.getOffset(oPos, pos).equals(Offset.getOffset(oPos, nextPos));
    }
}
