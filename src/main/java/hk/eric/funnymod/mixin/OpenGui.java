package hk.eric.funnymod.mixin;

import net.minecraft.client.gui.Gui;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Gui.class)
public interface OpenGui {
    @Accessor("title")
    Component getTitleComponent();

    @Accessor("title")
    void setTitleComponent(Component title);
}
