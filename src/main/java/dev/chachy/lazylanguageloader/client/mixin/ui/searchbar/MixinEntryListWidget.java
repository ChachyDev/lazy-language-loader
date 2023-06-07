package dev.chachy.lazylanguageloader.client.mixin.ui.searchbar;

import dev.chachy.lazylanguageloader.client.api.scroll.Scrollable;
import net.minecraft.client.gui.widget.EntryListWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntryListWidget.class)
public class MixinEntryListWidget implements Scrollable {
    private boolean scrolled;

    @Inject(method = "mouseScrolled", at = @At("HEAD"))
    private void mouseScrolled(double mouseX, double mouseY, double amount, CallbackInfoReturnable<Boolean> cir) {
        scrolled = true;
    }

    @Override
    public boolean hasScrolled() {
        return scrolled;
    }
}
