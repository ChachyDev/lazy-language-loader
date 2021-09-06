package club.chachy.lazylanguageloader.client.mixin;

import club.chachy.lazylanguageloader.client.state.StateManager;
import net.minecraft.client.resource.language.LanguageManager;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.minecraft.resource.ResourceReloadListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;

@Mixin(ReloadableResourceManagerImpl.class)
public class MixinReloadableResourceManagerImpl {
    @ModifyArg(
        method = "beginReloadInner",
        at = @At(
            value = "INVOKE",
            target = "Lcom/google/common/collect/Lists;newArrayList(Ljava/lang/Iterable;)Ljava/util/ArrayList;",
            remap = false
        )
    )
    private Iterable<ResourceReloadListener> reload(Iterable<ResourceReloadListener> reloaders) {
        if (StateManager.isResourceLoadViaLanguage()) {
            return Collections.singletonList(StateManager.getLanguageManager());
        }

        return reloaders;
    }

    @Inject(method = "registerListener", at = @At("HEAD"))
    private void registerListener(ResourceReloadListener listener, CallbackInfo ci) {
        if (listener instanceof LanguageManager) {
            StateManager.setLanguageManager((LanguageManager) listener);
        }
    }
}
