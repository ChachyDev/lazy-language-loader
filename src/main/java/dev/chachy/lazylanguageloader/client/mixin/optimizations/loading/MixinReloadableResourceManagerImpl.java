package dev.chachy.lazylanguageloader.client.mixin.optimizations.loading;

import dev.chachy.lazylanguageloader.client.impl.state.StateManager;
import net.minecraft.client.resource.language.LanguageManager;
import net.minecraft.client.search.SearchManager;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.minecraft.resource.ResourceReloader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ReloadableResourceManagerImpl.class)
public class MixinReloadableResourceManagerImpl {
    @ModifyArg(
        method = "reload",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/resource/SimpleResourceReload;start(Lnet/minecraft/resource/ResourceManager;Ljava/util/List;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Ljava/util/concurrent/CompletableFuture;Z)Lnet/minecraft/resource/ResourceReload;"
        )
    )
    private List<ResourceReloader> lazyLanguageLoader$$onReload(List<ResourceReloader> reloaders) {
        return StateManager.isResourceLoadViaLanguage() ? StateManager.getResourceReloaders() : reloaders;
    }

    @Inject(method = "registerReloader", at = @At("HEAD"))
    private void lazyLanguageLoader$$onRegisterReloader(ResourceReloader reloader, CallbackInfo ci) {
        if (reloader instanceof LanguageManager || reloader instanceof SearchManager) {
            StateManager.addResourceReloader(reloader);
        }
    }
}
