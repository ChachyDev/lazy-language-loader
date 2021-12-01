package club.chachy.lazylanguageloader.client.mixin.optimizations;

import club.chachy.lazylanguageloader.client.impl.state.StateManager;
import net.minecraft.client.gui.screen.option.LanguageOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LanguageOptionsScreen.class)
public class MixinLanguageOptionsScreen {
    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(
        method = "method_19820",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/MinecraftClient;reloadResources()Ljava/util/concurrent/CompletableFuture;",
            ordinal = 0
        )
    )
    private void lazyLanguageLoader$$preResourceLoad(ButtonWidget widget, CallbackInfo info) {
        StateManager.setResourceLoadViaLanguage(true);
    }

    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(
        method = "method_19820",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/MinecraftClient;reloadResources()Ljava/util/concurrent/CompletableFuture;",
            ordinal = 0,
            shift = At.Shift.AFTER
        )
    )
    private void lazyLanguageLoader$$postResourceLoad(ButtonWidget widget, CallbackInfo info) {
        StateManager.setResourceLoadViaLanguage(false);
    }
}
