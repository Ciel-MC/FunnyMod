package hk.eric.funnymod.utils.teleport;

import hk.eric.funnymod.utils.classes.PosRot;
import hk.eric.funnymod.utils.classes.getters.Getter;
import hk.eric.funnymod.utils.classes.pathFind.Node;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @param startingPos The starting position of the player
 * @param position The position to go to
 * @param lookAt Whether to look at the position or not
 * @param lookAtPos The position to look at
 * @param lookAnchor The anchor to calculate rotation from
 * @param vertPhase Whether to use vertical phase or not
 * @param stepSize How many blocks before sending a packet
 * @param corner Whether to force a movement packet at corners or not
 * @param pathfind False if no packets should be sent on the way
 * @param snapBack True if the player should snap back to the starting position after teleporting
 * @param onGround Supplier for whether the player is on the ground or not in each packet
 * @param packetSender The packet sender
 * @param playerPosition The player position
 * @param maxStep The maximum step size for pathfinding
 * @param diagonal Whether to use diagonal movement or not
 * @param ended Determine whether the movement is ended or not
 * @param goIfFail Whether to move an incomplete path or not
 */
public record TeleportSetting(
        Vec3 startingPos,
        Vec3 position,
        boolean lookAt,
        Vec3 lookAtPos,
        Function<Vec3, Vec3> lookAnchor,
        boolean vertPhase,
        int stepSize,
        boolean corner,
        boolean pathfind,
        boolean snapBack,
        Getter<Boolean> onGround,
        Consumer<Packet<?>> packetSender,
        Consumer<PosRot> playerPosition,
        int maxStep,
        boolean diagonal,
        BiFunction<Node, Node, Boolean> ended,
        boolean goIfFail,
        Predicate<List<Node>> canMove
) {
    public static TeleportSettingBuilder builder() {
        return new TeleportSettingBuilder();
    }
}