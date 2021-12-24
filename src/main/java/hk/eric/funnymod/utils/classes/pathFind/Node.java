package hk.eric.funnymod.utils.classes.pathFind;


import hk.eric.funnymod.FunnyModClient;
import hk.eric.funnymod.utils.classes.minecraftPlus.BetterBlockPos;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class Node {
    private Node parent;
    private final BetterBlockPos position;

    public Node(BetterBlockPos position) {
        this.position = position;
    }

    public List<Node> getNeighbours(boolean diagonal) {
        List<Node> neighbours = new ArrayList<>();
        if (diagonal) {
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -1; z <= 1; z++) {
                        if (x == 0 && y == 0 && z == 0) {
                            continue;
                        }
                        BetterBlockPos neighbour = this.getPos().offset(x, y, z);
                        if (canGo(neighbour)) {
                            neighbours.add(new Node(neighbour));
                        }
                    }
                }
            }
        }else {
            ClientLevel level;
            if((level = FunnyModClient.mc.level) != null) {
                if (canGo(this.getPos().offset(1, 0, 0))) {
                    neighbours.add(new Node(this.getPos().offset(1, 0, 0)));
                }
                if (canGo(this.getPos().offset(-1, 0, 0))) {
                    neighbours.add(new Node(this.getPos().offset(-1, 0, 0)));
                }
                if (canGo(this.getPos().offset(0, 0, 1))) {
                    neighbours.add(new Node(this.getPos().offset(0, 0, 1)));
                }
                if (canGo(this.getPos().offset(0, 0, -1))) {
                    neighbours.add(new Node(this.getPos().offset(0, 0, -1)));
                }
                if (canGo(this.getPos().offset(0, 1, 0))) {
                    neighbours.add(new Node(this.getPos().offset(0, 1, 0)));
                }
                if (canGo(this.getPos().offset(0, -1, 0))) {
                    neighbours.add(new Node(this.getPos().offset(0, -1, 0)));
                }
            }
        }
        return neighbours;
    }

    public BetterBlockPos getPos() {
        return position;
    }

    public boolean hasParent() {
        return parent != null;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getParent() {
        return parent;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Node node) {
            return node.getPos().equals(this.getPos());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getPos().hashCode();
    }

    public int distance(Node node) {
        int xDiff = Math.abs(node.getPos().getX() - this.getPos().getX());
        int yDiff = Math.abs(node.getPos().getY() - this.getPos().getY());
        int zDiff = Math.abs(node.getPos().getZ() - this.getPos().getZ());
        return xDiff + yDiff + zDiff;
    }

    public List<Node> asList() {
        List<Node> list = new ArrayList<>();
        Node current = this;
        while (current != null) {
            list.add(current);
            current = current.getParent();
        }
        Collections.reverse(list);
        return list;
    }

    public Stream<Node> asStream() {
        return asList().stream();
    }
    
    private static boolean canGo(BlockPos pos) {
        if (FunnyModClient.mc.level == null) return false;
        BlockState state = FunnyModClient.mc.level.getBlockState(pos);
        BlockState up = FunnyModClient.mc.level.getBlockState(pos.offset(0, 1, 0));
        return !isSolid(state) && !isSolid(up);
    }
    
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private static boolean isSolid(BlockState state) {
        return state.getMaterial().isSolid();
    }
}