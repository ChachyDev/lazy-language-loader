package club.chachy.lazylanguageloader.client.mixin.ui.searchbar.accessor;

import net.minecraft.client.gui.screen.option.LanguageOptionsScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LanguageOptionsScreen.LanguageSelectionListWidget.LanguageEntry.class)
public interface LanguageEntryAccessor {
    @Accessor
    Text getLanguageDefinition();
}
