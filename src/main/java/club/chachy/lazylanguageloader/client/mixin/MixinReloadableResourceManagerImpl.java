package club.chachy.lazylanguageloader.client.mixin;

import club.chachy.lazylanguageloader.client.state.StateManager;
import net.minecraft.client.resource.language.LanguageManager;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.minecraft.resource.ResourceReloader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.List;

@Mixin(ReloadableResourceManagerImpl.class)
public class MixinReloadableResourceManagerImpl {
    @Shadow
    @Final
    private List<ResourceReloader> reloaders;

    @Redirect(method = "reload", at = @At(value = "FIELD", target = "Lnet/minecraft/resource/ReloadableResourceManagerImpl;reloaders:Ljava/util/List;"))
    private List<ResourceReloader> init(ReloadableResourceManagerImpl impl) {
        if (StateManager.isResourceLoadViaLanguage()) {
            return Collections.singletonList(StateManager.getLanguageManager());
        }

        return reloaders;
    }

    @Inject(method = "registerReloader", at = @At("HEAD"))
    private void registerReloader(ResourceReloader reloader, CallbackInfo ci) {
        if (reloader instanceof LanguageManager) {
            StateManager.setLanguageManager((LanguageManager) reloader);
        }
    }
}
