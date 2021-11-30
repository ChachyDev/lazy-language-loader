package club.chachy.lazylanguageloader.client.mixin.searchbar;

import club.chachy.lazylanguageloader.client.api.scroll.Scrollable;
import club.chachy.lazylanguageloader.client.impl.state.StateManager;
import club.chachy.lazylanguageloader.client.mixin.searchbar.accessor.LanguageEntryAccessor;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.LanguageOptionsScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resource.language.LanguageDefinition;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Mixin(LanguageOptionsScreen.class)
public class MixinLanguageOptionsScreen extends Screen {
    @Shadow
    private LanguageOptionsScreen.LanguageSelectionListWidget languageSelectionList;

    private List<LanguageOptionsScreen.LanguageSelectionListWidget.LanguageEntry> initialComponents;

    protected MixinLanguageOptionsScreen(Text title) {
        super(title);
    }

    @Inject(
        method = "init",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/option/LanguageOptionsScreen;addSelectableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;",
            shift = At.Shift.AFTER
        )
    )
    private void init(CallbackInfo ci) {
        initialComponents = new ArrayList<>(languageSelectionList.children());

        int w = width / 5;
        TextFieldWidget searchText = new TextFieldWidget(textRenderer, width - (w + 5), 11, w, 15, LiteralText.EMPTY);
        searchText.setChangedListener(this::handleText);

        addDrawableChild(searchText);
    }

    private void handleText(String text) {
        if (text.isBlank() || text.isBlank()) {
            int initialSize = initialComponents.size();
            int currentSize = languageSelectionList.children().size();

            if (initialSize != currentSize) {
                languageSelectionList.replaceEntries(initialComponents);
            }
        } else {
            for (LanguageOptionsScreen.LanguageSelectionListWidget.LanguageEntry entry : initialComponents) {
                LanguageDefinition def = ((LanguageEntryAccessor) entry).getLanguageDefinition();

                if (!StateManager.isMatchable(text, def)) {
                    languageSelectionList.removeEntry(entry);
                } else {
                    if (!languageSelectionList.children().contains(entry)) {
                        languageSelectionList.addEntry(entry);
                    }
                }
            }
        }
        fixScroll();
    }

    private void fixScroll() {
        if (((Scrollable) languageSelectionList).hasScrolled()) {
            languageSelectionList.setScrollAmount(languageSelectionList.getScrollAmount());
        } else {
            languageSelectionList.centerScrollOn(languageSelectionList.getSelectedOrNull());
        }
    }
}