package hk.eric.funnymod.event.events;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import hk.eric.funnymod.event.Event;
import hk.eric.funnymod.event.EventState;
import hk.eric.funnymod.event.HasState;

public class Render3DEvent extends Event implements HasState {

    private final PoseStack stack;
    private final float partialTicks;
    private final Matrix4f projection;
    private final EventState state;

    private Render3DEvent(PoseStack stack, float partialTicks, Matrix4f projection, EventState state) {
        this.stack = stack;
        this.partialTicks = partialTicks;
        this.projection = projection;
        this.state = state;
    }

    public PoseStack getStack() {
        return stack;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public Matrix4f getProjection() {
        return projection;
    }

    @Override
    public EventState getState() {
        return state;
    }

    public static class Pre extends Render3DEvent {
        public Pre(PoseStack stack, float partialTicks, Matrix4f projection) {
            super(stack, partialTicks, projection, EventState.PRE);
        }
    }

    public static class Post extends Render3DEvent {
        public Post(PoseStack stack, float partialTicks, Matrix4f projection) {
            super(stack, partialTicks, projection, EventState.POST);
        }
    }
}
