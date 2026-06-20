package dev.chachy.lazylanguageloader.client.mixin.optimizations.splash;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import net.minecraft.client.gui.screens.options.LanguageSelectScreen;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class MixinGui {
    @Shadow
    @Nullable
    private Screen screen;

    @Inject(method = "setOverlay", at = @At("HEAD"), cancellable = true)
    private void lazyLanguageLoader$$setOverlay(Overlay overlay, CallbackInfo ci) {
        if (overlay instanceof LoadingOverlay && lazyLanguageLoader$$verifyScreen(this.screen)) {
            ci.cancel();
        }
    }

    @Unique
    private boolean lazyLanguageLoader$$verifyScreen(Screen screen) {
        return screen instanceof LanguageSelectScreen || screen instanceof CraftingScreen;
    }
}
