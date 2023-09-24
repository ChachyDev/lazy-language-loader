package dev.chachy.lazylanguageloader.client.mixin.ui.searchbar;

import dev.chachy.lazylanguageloader.client.api.scroll.Scrollable;
import net.minecraft.client.gui.widget.EntryListWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntryListWidget.class)
public class MixinEntryListWidget implements Scrollable {
    @Unique
    private boolean scrolled;


    @Inject(method = "mouseScrolled", at = @At("HEAD"))
    private void mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount, CallbackInfoReturnable<Boolean> cir) {
        this.scrolled = true;
    }


    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public boolean hasScrolled() {
        return scrolled;
    }
}
