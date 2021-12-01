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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

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
        List<LanguageOptionsScreen.LanguageSelectionListWidget.LanguageEntry> children = languageSelectionList.children();

        if (text.isBlank() || text.isBlank()) {
            int initialSize = initialComponents.size();
            int currentSize = children.size();

            if (initialSize != currentSize) {
                languageSelectionList.replaceEntries(initialComponents);
            }
        } else {
            for (LanguageOptionsScreen.LanguageSelectionListWidget.LanguageEntry entry : initialComponents) {
                LanguageDefinition def = ((LanguageEntryAccessor) entry).getLanguageDefinition();

                if (StateManager.isMatchable(text, def)) {
                    safeAdd(entry);
                } else {
                    languageSelectionList.removeEntry(entry);
                }
            }
        }
        fixScroll();
    }

    @Unique
    private void fixScroll() {
        if (((Scrollable) languageSelectionList).hasScrolled()) {
            languageSelectionList.setScrollAmount(languageSelectionList.getScrollAmount());
        } else {
            languageSelectionList.centerScrollOn(languageSelectionList.getSelectedOrNull());
        }
    }

    @Unique
    private void safeAdd(LanguageOptionsScreen.LanguageSelectionListWidget.LanguageEntry entry) {
        if (!languageSelectionList.children().contains(entry)) {
            languageSelectionList.addEntry(entry);
        }
    }
}
