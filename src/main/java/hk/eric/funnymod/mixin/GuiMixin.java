package hk.eric.funnymod.mixin;

import net.minecraft.client.gui.Gui;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Gui.class)
public interface GuiMixin {
    @Accessor("title")
    public Component getTitle();

    @Accessor("title")
    public void setTitle(Component title);
}
