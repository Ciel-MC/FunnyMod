package com.lukflug.panelstudio.mc17;

import com.lukflug.panelstudio.base.IInterface;
import com.mojang.blaze3d.vertex.PoseStack;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.function.Supplier;

/**
 * Implementation of {@link GLInterface} to be used with {@link MinecraftGUI}
 * @author lukflug
 */
public abstract class GUIInterface extends GLInterface {
    private final Supplier<Integer> modifiersSupplier;
    private final Supplier<Long> lastTimeSupplier;
    private final Supplier<Point> mouseSupplier;
    private final Supplier<PoseStack> matrixStackSupplier;
    private final Supplier<Integer> callGetBlitOffset;
    private final Supplier<Boolean> lButtonSupplier;
    private final Supplier<Boolean> rButtonSupplier;

    /**
     * Constructor.
     * @param clipX whether to clip in the horizontal direction
     */
    public GUIInterface(boolean clipX, Supplier<Integer> modifiersSupplier, Supplier<Long> lastTimeSupplier, Supplier<Point> mouseSupplier, Supplier<PoseStack> matrixStackSupplier, Supplier<Integer> callGetBlitOffset, Supplier<Boolean> lButtonSupplier, Supplier<Boolean> rButtonSupplier) {
        super(clipX);
        this.modifiersSupplier = modifiersSupplier;
        this.lastTimeSupplier = lastTimeSupplier;
        this.mouseSupplier = mouseSupplier;
        this.matrixStackSupplier = matrixStackSupplier;
        this.callGetBlitOffset = callGetBlitOffset;
        this.lButtonSupplier = lButtonSupplier;
        this.rButtonSupplier = rButtonSupplier;
    }

    @Override
    public boolean getModifier (int modifier) {
        return switch (modifier) {
            case SHIFT -> (modifiersSupplier.get() & GLFW.GLFW_MOD_SHIFT) != 0;
            case CTRL -> (modifiersSupplier.get() & GLFW.GLFW_MOD_CONTROL) != 0;
            case ALT -> (modifiersSupplier.get() & GLFW.GLFW_MOD_ALT) != 0;
            case SUPER -> (modifiersSupplier.get() & GLFW.GLFW_MOD_SUPER) != 0;
            default -> false;
        };
    }

    @Override
    public long getTime() {
        return lastTimeSupplier.get();
    }

    @Override
    public boolean getButton (int button) {
        return switch (button) {
            case IInterface.LBUTTON -> lButtonSupplier.get();
            case IInterface.RBUTTON -> rButtonSupplier.get();
            default -> false;
        };
    }

    @Override
    public Point getMouse() {
        return new Point(mouseSupplier.get());
    }

    @Override
    protected float getZLevel() {
        return callGetBlitOffset.get();
    }

    @Override
    protected PoseStack getMatrixStack() {
        return matrixStackSupplier.get();
    }
}
