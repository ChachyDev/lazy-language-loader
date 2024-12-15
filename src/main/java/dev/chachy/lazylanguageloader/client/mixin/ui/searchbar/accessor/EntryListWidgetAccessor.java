package dev.chachy.lazylanguageloader.client.mixin.ui.searchbar.accessor;

import net.minecraft.client.gui.widget.EntryListWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntryListWidget.class)
public interface EntryListWidgetAccessor {
    @Invoker
    void invokeScroll(int amount);
}
