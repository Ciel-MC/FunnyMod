package hk.eric.funnymod.mixin;

import hk.eric.funnymod.event.events.TitleEvents;
import net.minecraft.client.gui.Gui;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Gui.class)
public class GuiMixin {
    @Shadow private @Nullable Component title;

    @Shadow private int titleTime;

    @Shadow private int titleFadeInTime;

    @Shadow private int titleStayTime;

    @Shadow private int titleFadeOutTime;

    @Shadow private @Nullable Component subtitle;

    /**
     * @reason Implement title event
     * @author Eric Ou
     */
    @Overwrite
    public void setTitle(Component component) {
        TitleEvents.TitleEvent event = new TitleEvents.TitleEvent(component);
        if (!event.call().isCancelled()) {
            this.title = event.getComponent();
            this.titleTime = this.titleFadeInTime + this.titleStayTime + this.titleFadeOutTime;
        }
    }

    /**
     * @reason Implement subtitle event
     * @author Eric Ou
     */
    @Overwrite
    public void setSubtitle(Component component) {
        TitleEvents.SubtitleEvent event = new TitleEvents.SubtitleEvent(component);
        if (!event.call().isCancelled()) {
            subtitle = event.getComponent();
        }
    }
}
