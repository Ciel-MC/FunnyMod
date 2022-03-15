package hk.eric.funnymod.utils.teleport;

import hk.eric.funnymod.utils.classes.PosRot;
import hk.eric.funnymod.utils.classes.getters.Getter;
import hk.eric.funnymod.utils.classes.pathFind.Node;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class TeleportSettingBuilder {

    private static final Logger logger = LogManager.getLogger("Teleport");

    private Vec3 startingPos = null;
    private Vec3 position = null;
    private boolean lookAt;
    private Vec3 lookAtPos;
    private Function<Vec3, Vec3> lookAnchor;
    private boolean vertPhase;
    private int stepSize = 1;
    private boolean corner;
    private boolean pathfind;
    private boolean snapBack;
    private Getter<Boolean> onGround = null;
    private Consumer<Packet<?>> packetSender = null;
    private Consumer<PosRot> playerPosition = null;
    private int maxStep;
    private boolean diagonal;
    private BiFunction<Node, Node, Boolean> ended = null;
    private boolean goIfFail;
    private Predicate<List<Node>> canGo = (nodes) -> true;

    public TeleportSettingBuilder setTarget(Vec3 position) {
        this.position = position;
        return this;
    }

    public TeleportSettingBuilder snapback() {
        return snapback(null);
    }

    public TeleportSettingBuilder snapback(@Nullable Vec3 startingPos) {
        if (startingPos != null) {
            this.startingPos = startingPos;
        }else {
            if (this.startingPos == null) {
                throw new IllegalArgumentException("Starting position has to be specified if snapback is set to true");
            }
        }
        this.snapBack = true;
        return this;
    }

    public TeleportSettingBuilder lookAt(Vec3 lookAtPos, Function<Vec3, Vec3> lookAnchor) {
        if (lookAtPos == null) {
            if (this.lookAtPos == null) {
                throw new IllegalArgumentException("Look at position has to be specified if look at is set to true");
            }
        }else {
            this.lookAtPos = lookAtPos;
        }
        if (lookAnchor == null) {
            if (this.lookAnchor == null) {
                throw new IllegalArgumentException("Look anchor has to be specified if look at is set to true");
            }
        }else {
            this.lookAnchor = lookAnchor;
        }
        this.lookAt = true;
        return this;
    }

    public TeleportSettingBuilder verticalPhase() {
        this.vertPhase = true;
        return this;
    }

    public TeleportSettingBuilder stepSize(int stepSize) {
        this.stepSize = stepSize;
        return this;
    }

    public TeleportSettingBuilder corner() {
        this.corner = true;
        return this;
    }

    public TeleportSettingBuilder pathfind() {
        this.pathfind = true;
        return this;
    }

    public TeleportSettingBuilder onGround(Getter<Boolean> onGround) {
        this.onGround = onGround;
        return this;
    }

    public TeleportSettingBuilder packetSender(Consumer<Packet<?>> packetSender) {
        this.packetSender = packetSender;
        return this;
    }

    public TeleportSettingBuilder playerPosition(Consumer<PosRot> playerPosition) {
        this.playerPosition = playerPosition;
        return this;
    }

    public TeleportSettingBuilder maxStep(int maxStep) {
        this.maxStep = maxStep;
        return this;
    }

    public TeleportSettingBuilder diagonal() {
        this.diagonal = true;
        return this;
    }

    public TeleportSettingBuilder ended(BiFunction<Node, Node, Boolean> ended) {
        this.ended = ended;
        return this;
    }

    public TeleportSettingBuilder goIfFail() {
        this.goIfFail = true;
        return this;
    }

    public TeleportSettingBuilder canGo(Predicate<List<Node>> canGo) {
        this.canGo = canGo;
        return this;
    }

    public TeleportSetting build() {
        if (position == null) {
            throw new IllegalArgumentException("Position has to be specified");
        }
        if (lookAt && (lookAtPos == null || lookAnchor == null)) {
            throw new IllegalArgumentException("Look at position and anchor have to be specified if look at is set to true");
        }
        if (onGround == null) {
            throw new IllegalArgumentException("On ground has to be specified");
        }
        if (packetSender == null) {
            throw new IllegalArgumentException("Packet sender has to be specified");
        }
        if ((snapBack || pathfind) && startingPos == null) {
            throw new IllegalArgumentException("Starting position has to be specified if snapback or pathfinding is set to true");
        }
        if (ended == null) {
            throw new IllegalArgumentException("Ended has to be specified");
        }
        if (!pathfind) {
            if (diagonal) {
                logger.warn("Teleport is set, diagonal should not be set");
            }
            if (vertPhase) {
                logger.warn("Teleport is set, vertical phase should not be set");
            }
            if (corner) {
                logger.warn("Teleport is set, corner should not be set");
            }
            if (goIfFail) {
                logger.warn("Teleport is set, go if fail should not be set");
            }
        }
        if (!snapBack) {
            if (playerPosition == null) {
                throw new IllegalArgumentException("Player position has to be specified if snapback is set to false");
            }
        }
        return new TeleportSetting(startingPos, position, lookAt, lookAtPos, lookAnchor, vertPhase, stepSize, corner, pathfind, snapBack, onGround, packetSender, playerPosition, maxStep, diagonal, ended, goIfFail, canGo);
    }
}
