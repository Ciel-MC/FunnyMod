package hk.eric.funnymod.utils.classes.minecraftPlus;

import hk.eric.funnymod.utils.classes.pathFind.Node;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

public class BetterBlockPos extends BlockPos {

    public static final BetterBlockPos ZERO = new BetterBlockPos(0, 0, 0);

    public static BetterBlockPos fromEntity(Entity entity) {
        return new BetterBlockPos(entity.getX(), entity.getY(), entity.getZ());
    }

    public static BetterBlockPos fromBlockPos(BlockPos position) {
        return new BetterBlockPos(position.getX(), position.getY(), position.getZ());
    }

    public BetterBlockPos(int i, int j, int k) {
        super(i, j, k);
    }

    public BetterBlockPos(double d, double e, double f) {
        super(d, e, f);
    }

    public BetterBlockPos(Vec3 vec3) {
        super(vec3);
    }

    public BetterBlockPos(Position position) {
        super(position);
    }

    public BetterBlockPos(Vec3i vec3i) {
        super(vec3i);
    }

    public Node asNode() {
        return new Node(this);
    }

    @Override
    public BetterBlockPos offset(double x, double y, double z) {
        if (x == 0.0 && y == 0.0 && z == 0.0) {
            return this;
        }
        return new BetterBlockPos((double)this.getX() + x, (double)this.getY() + y, (double)this.getZ() + z);
    }

    @Override
    public BetterBlockPos offset(int x, int y, int z) {
        return new BetterBlockPos(this.getX() + x, this.getY() + y, this.getZ() + z);
    }

    @Override
    public BetterBlockPos offset(Vec3i offset) {
        return this.offset(offset.getX(), offset.getY(), offset.getZ());
    }

    @Override
    public BetterBlockPos subtract(Vec3i offset) {
        return this.offset(-offset.getX(), -offset.getY(), -offset.getZ());
    }

    @Override
    public BetterBlockPos multiply(int multiplier) {
        if (multiplier == 1) {
            return this;
        }
        if (multiplier == 0) {
            return ZERO;
        }
        return new BetterBlockPos(this.getX() * multiplier, this.getY() * multiplier, this.getZ() * multiplier);
    }

    @Override
    public BetterBlockPos above() {
        return this.relative(Direction.UP);
    }

    @Override
    public BetterBlockPos above(int amount) {
        return this.relative(Direction.UP, amount);
    }

    @Override
    public BetterBlockPos below() {
        return this.relative(Direction.DOWN);
    }

    @Override
    public BetterBlockPos below(int amount) {
        return this.relative(Direction.DOWN, amount);
    }

    @Override
    public BetterBlockPos north() {
        return this.relative(Direction.NORTH);
    }

    @Override
    public BetterBlockPos north(int amount) {
        return this.relative(Direction.NORTH, amount);
    }

    @Override
    public BetterBlockPos south() {
        return this.relative(Direction.SOUTH);
    }

    @Override
    public BetterBlockPos south(int amount) {
        return this.relative(Direction.SOUTH, amount);
    }

    @Override
    public BetterBlockPos west() {
        return this.relative(Direction.WEST);
    }

    @Override
    public BetterBlockPos west(int amount) {
        return this.relative(Direction.WEST, amount);
    }

    @Override
    public BetterBlockPos east() {
        return this.relative(Direction.EAST);
    }

    @Override
    public BetterBlockPos east(int amount) {
        return this.relative(Direction.EAST, amount);
    }

    @Override
    public BetterBlockPos relative(Direction direction) {
        return new BetterBlockPos(this.getX() + direction.getStepX(), this.getY() + direction.getStepY(), this.getZ() + direction.getStepZ());
    }

    @Override
    public BetterBlockPos relative(Direction direction, int amount) {
        if (amount == 0) {
            return this;
        }
        return new BetterBlockPos(this.getX() + direction.getStepX() * amount, this.getY() + direction.getStepY() * amount, this.getZ() + direction.getStepZ() * amount);
    }

    @Override
    public BetterBlockPos relative(Direction.Axis axis, int amount) {
        if (amount == 0) {
            return this;
        }
        int j = axis == Direction.Axis.X ? amount : 0;
        int k = axis == Direction.Axis.Y ? amount : 0;
        int l = axis == Direction.Axis.Z ? amount : 0;
        return new BetterBlockPos(this.getX() + j, this.getY() + k, this.getZ() + l);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof BetterBlockPos pos) {
            return this.getX() == pos.getX() && this.getY() == pos.getY() && this.getZ() == pos.getZ();
        }
        return false;
    }

    @Override
    public String toString() {
        return "BlockPos(" + this.getX() + ", " + this.getY() + ", " + this.getZ() + ")";
    }

    public BetterBlockPos rotate(Rotation rotation) {
        switch (rotation) {
            default: {
                return this;
            }
            case CLOCKWISE_90: {
                return new BetterBlockPos(-this.getZ(), this.getY(), this.getX());
            }
            case CLOCKWISE_180: {
                return new BetterBlockPos(-this.getX(), this.getY(), -this.getZ());
            }
            case COUNTERCLOCKWISE_90:
        }
        return new BetterBlockPos(this.getZ(), this.getY(), -this.getX());
    }

    @Override
    public BetterBlockPos cross(Vec3i cross) {
        return new BetterBlockPos(this.getY() * cross.getZ() - this.getZ() * cross.getY(), this.getZ() * cross.getX() - this.getX() * cross.getZ(), this.getX() * cross.getY() - this.getY() * cross.getX());
    }

    @Override
    public BetterBlockPos atY(int y) {
        return new BetterBlockPos(this.getX(), y, this.getZ());
    }

    public Vec3 toCenteredVec3() {
        return new Vec3(this.getX() + 0.5, this.getY(), this.getZ() + 0.5);
    }

    public Vec3 toVec3() {
        return new Vec3(this.getX(), this.getY() - .5, this.getZ());
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public BetterBlockPos clone() {
        return new BetterBlockPos(this.getX(), this.getY(), this.getZ());
    }

    @Override
    public long asLong() {
        return Objects.hash(this.getX(), this.getY(), this.getZ());
    }

    @Override
    public BetterBlockPos immutable() {
        throw new UnsupportedOperationException("Cannot mutate a BetterBlockPos");
    }

    @Override
    public MutableBlockPos mutable() {
        throw new UnsupportedOperationException("Cannot mutate a BetterBlockPos");
    }
}
