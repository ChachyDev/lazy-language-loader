package club.chachy.lazylanguageloader.client.mixin.optimizations.splash;

import club.chachy.lazylanguageloader.client.impl.state.StateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.client.gui.screen.option.LanguageOptionsScreen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {
    @Inject(method = "setOverlay", at = @At("HEAD"), cancellable = true)
    private void lazyLanguageLoader$$setOverlay(Overlay overlay, CallbackInfo ci) {
        if (overlay instanceof SplashOverlay && StateManager.isResourceLoadViaLanguage()) {
            ci.cancel();
        }
    }
}
