package dev.chachy.lazylanguageloader.client.mixin.optimizations.loading;

import dev.chachy.lazylanguageloader.client.impl.state.StateManager;
import net.minecraft.client.gui.screen.option.LanguageOptionsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LanguageOptionsScreen.class)
public class MixinLanguageOptionsScreen {
    @Inject(
        method = "onDone",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/MinecraftClient;reloadResources()Ljava/util/concurrent/CompletableFuture;",
            ordinal = 0
        )
    )
    private void lazyLanguageLoader$$preResourceLoad(CallbackInfo info) {
        StateManager.setResourceLoadViaLanguage(true);
    }

    @Inject(
        method = "onDone",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/MinecraftClient;reloadResources()Ljava/util/concurrent/CompletableFuture;",
            ordinal = 0,
            shift = At.Shift.AFTER
        )
    )
    private void lazyLanguageLoader$$postResourceLoad(CallbackInfo info) {
        StateManager.setResourceLoadViaLanguage(false);
    }
}
