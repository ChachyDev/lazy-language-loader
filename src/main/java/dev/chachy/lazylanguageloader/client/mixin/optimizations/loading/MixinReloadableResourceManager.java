package dev.chachy.lazylanguageloader.client.mixin.optimizations.loading;

import dev.chachy.lazylanguageloader.client.impl.state.StateManager;
import net.minecraft.client.multiplayer.SessionSearchTrees;
import net.minecraft.client.resources.language.LanguageManager;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ReloadableResourceManager.class)
public class MixinReloadableResourceManager {
    @ModifyArg(
        method = "createReload",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/packs/resources/SimpleReloadInstance;create(Lnet/minecraft/server/packs/resources/ResourceManager;Ljava/util/List;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Ljava/util/concurrent/CompletableFuture;Z)Lnet/minecraft/server/packs/resources/ReloadInstance;"
        )
    )
    private List<PreparableReloadListener> lazyLanguageLoader$$onReload(List<PreparableReloadListener> reloaders) {
        return StateManager.isResourceLoadViaLanguage() ? StateManager.getResourceReloaders() : reloaders;
    }

    @Inject(method = "registerReloadListener", at = @At("HEAD"))
    private void lazyLanguageLoader$$onRegisterReloader(PreparableReloadListener listener, CallbackInfo ci) {
        if (listener instanceof LanguageManager || listener instanceof SessionSearchTrees) {
            StateManager.addResourceReloader(listener);
        }
    }
}
