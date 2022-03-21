package hk.eric.funnymod.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import hk.eric.ericLib.utils.classes.ThreeDimensionalLine;
import hk.eric.funnymod.FunnyModClient;
import hk.eric.funnymod.utils.accessor.IEntityRenderDispatcher;
import net.minecraft.core.Position;
import net.minecraft.world.phys.AABB;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public interface RenderUtil {

    Tesselator tesselator = Tesselator.getInstance();
    BufferBuilder buffer = tesselator.getBuilder();
    IEntityRenderDispatcher renderManager = (IEntityRenderDispatcher) FunnyModClient.mc.getEntityRenderDispatcher();

    float[] color = new float[] {1.0F, 1.0F, 1.0F, 255.0F};

    static void glColor(Color color, float alpha) {
        float[] colorComponents = color.getColorComponents(null);
        RenderUtil.color[0] = colorComponents[0];
        RenderUtil.color[1] = colorComponents[1];
        RenderUtil.color[2] = colorComponents[2];
        RenderUtil.color[3] = alpha;
    }

    static void setupLines(Color color, float alpha, float lineWidth, boolean ignoreDepth) {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);
        glColor(color, alpha);
        RenderSystem.lineWidth(lineWidth);
        RenderSystem.disableTexture();
        RenderSystem.depthMask(false);

        if (ignoreDepth) {
            RenderSystem.disableDepthTest();
        }
    }

    static void startLines(Color color, float lineWidth, boolean ignoreDepth) {
        setupLines(color, .4f, lineWidth, ignoreDepth);
    }

    static void endLines(boolean ignoredDepth) {
        if (ignoredDepth) {
            RenderSystem.enableDepthTest();
        }

        RenderSystem.depthMask(true);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    static void drawAABB(PoseStack stack, AABB aabb) {
        AABB toDraw = aabb.move(-renderManager.renderX(), -renderManager.renderY(), -renderManager.renderZ());

        Matrix4f matrix4f = stack.last().pose();
        //TODO: check
        buffer.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
        // bottom
        buffer.vertex(matrix4f, (float) toDraw.minX, (float) toDraw.minY, (float) toDraw.minZ).color(color[0], color[1], color[2], color[3]).endVertex();
        buffer.vertex(matrix4f, (float) toDraw.maxX, (float) toDraw.minY, (float) toDraw.minZ).color(color[0], color[1], color[2], color[3]).endVertex();
        buffer.vertex(matrix4f, (float) toDraw.maxX, (float) toDraw.minY, (float) toDraw.minZ).color(color[0], color[1], color[2], color[3]).endVertex();
        buffer.vertex(matrix4f, (float) toDraw.maxX, (float) toDraw.minY, (float) toDraw.maxZ).color(color[0], color[1], color[2], color[3]).endVertex();
        buffer.vertex(matrix4f, (float) toDraw.maxX, (float) toDraw.minY, (float) toDraw.maxZ).color(color[0], color[1], color[2], color[3]).endVertex();
        buffer.vertex(matrix4f, (float) toDraw.minX, (float) toDraw.minY, (float) toDraw.maxZ).color(color[0], color[1], color[2], color[3]).endVertex();
        buffer.vertex(matrix4f, (float) toDraw.minX, (float) toDraw.minY, (float) toDraw.maxZ).color(color[0], color[1], color[2], color[3]).endVertex();
        buffer.vertex(matrix4f, (float) toDraw.minX, (float) toDraw.minY, (float) toDraw.minZ).color(color[0], color[1], color[2], color[3]).endVertex();
        // top
        buffer.vertex(matrix4f, (float) toDraw.minX, (float) toDraw.maxY, (float) toDraw.minZ).color(color[0], color[1], color[2], color[3]).endVertex();
        buffer.vertex(matrix4f, (float) toDraw.maxX, (float) toDraw.maxY, (float) toDraw.minZ).color(color[0], color[1], color[2], color[3]).endVertex();
        buffer.vertex(matrix4f, (float) toDraw.maxX, (float) toDraw.maxY, (float) toDraw.minZ).color(color[0], color[1], color[2], color[3]).endVertex();
        buffer.vertex(matrix4f, (float) toDraw.maxX, (float) toDraw.maxY, (float) toDraw.maxZ).color(color[0], color[1], color[2], color[3]).endVertex();
        buffer.vertex(matrix4f, (float) toDraw.maxX, (float) toDraw.maxY, (float) toDraw.maxZ).color(color[0], color[1], color[2], color[3]).endVertex();
        buffer.vertex(matrix4f, (float) toDraw.minX, (float) toDraw.maxY, (float) toDraw.maxZ).color(color[0], color[1], color[2], color[3]).endVertex();
        buffer.vertex(matrix4f, (float) toDraw.minX, (float) toDraw.maxY, (float) toDraw.maxZ).color(color[0], color[1], color[2], color[3]).endVertex();
        buffer.vertex(matrix4f, (float) toDraw.minX, (float) toDraw.maxY, (float) toDraw.minZ).color(color[0], color[1], color[2], color[3]).endVertex();
        // corners
        buffer.vertex(matrix4f, (float) toDraw.minX, (float) toDraw.minY, (float) toDraw.minZ).color(color[0], color[1], color[2], color[3]).endVertex();
        buffer.vertex(matrix4f, (float) toDraw.minX, (float) toDraw.maxY, (float) toDraw.minZ).color(color[0], color[1], color[2], color[3]).endVertex();
        buffer.vertex(matrix4f, (float) toDraw.maxX, (float) toDraw.minY, (float) toDraw.minZ).color(color[0], color[1], color[2], color[3]).endVertex();
        buffer.vertex(matrix4f, (float) toDraw.maxX, (float) toDraw.maxY, (float) toDraw.minZ).color(color[0], color[1], color[2], color[3]).endVertex();
        buffer.vertex(matrix4f, (float) toDraw.maxX, (float) toDraw.minY, (float) toDraw.maxZ).color(color[0], color[1], color[2], color[3]).endVertex();
        buffer.vertex(matrix4f, (float) toDraw.maxX, (float) toDraw.maxY, (float) toDraw.maxZ).color(color[0], color[1], color[2], color[3]).endVertex();
        buffer.vertex(matrix4f, (float) toDraw.minX, (float) toDraw.minY, (float) toDraw.maxZ).color(color[0], color[1], color[2], color[3]).endVertex();
        buffer.vertex(matrix4f, (float) toDraw.minX, (float) toDraw.maxY, (float) toDraw.maxZ).color(color[0], color[1], color[2], color[3]).endVertex();
        tesselator.end();
    }

    static void drawLine(ThreeDimensionalLine line) {
        drawLine(FunnyModClient.getPoseStack(), line);
    }

    static void drawLine(PoseStack stack, ThreeDimensionalLine line) {
        drawLine(stack, line.getX1(), line.getY1(), line.getZ1(), line.getX2(), line.getY2(), line.getZ2());
    }

    static void drawLine(PoseStack stack, Position pos1, Position pos2) {
        drawLine(stack, pos1.x(), pos1.y(), pos1.z(), pos2.x(), pos2.y(), pos2.z());
    }

    static void drawLine(PoseStack stack, double x1, double y1, double z1, double x2, double y2, double z2) {
        Matrix4f matrix4f = stack.last().pose();

        double vpX = posX();
        double vpY = posY();
        double vpZ = posZ();
        buffer.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
        buffer.vertex(matrix4f, (float) (x1 + 0.5D - vpX), (float) (y1 + 0.5D - vpY), (float) (z1 + 0.5D - vpZ)).color(color[0], color[1], color[2], color[3]).endVertex();
        buffer.vertex(matrix4f, (float) (x2 + 0.5D - vpX), (float) (y2 + 0.5D - vpY), (float) (z2 + 0.5D - vpZ)).color(color[0], color[1], color[2], color[3]).endVertex();
        tesselator.end();
    }

    static void drawAABB(PoseStack stack, AABB aabb, double expand) {
        drawAABB(stack, aabb.inflate(expand, expand, expand));
    }

    static double posX() {
        return renderManager.renderX();
    }

    static double posY() {
        return renderManager.renderY();
    }

    static double posZ() {
        return renderManager.renderZ();
    }
}