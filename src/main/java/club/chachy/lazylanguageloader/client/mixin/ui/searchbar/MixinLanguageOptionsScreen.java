package club.chachy.lazylanguageloader.client.mixin.ui.searchbar;

import club.chachy.lazylanguageloader.client.api.scroll.Scrollable;
import club.chachy.lazylanguageloader.client.impl.state.StateManager;
import club.chachy.lazylanguageloader.client.impl.utils.Constants;
import club.chachy.lazylanguageloader.client.mixin.ui.searchbar.accessor.LanguageEntryAccessor;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.LanguageOptionsScreen;
import net.minecraft.client.gui.widget.ClickableWidget;
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

    @Unique
    private List<LanguageOptionsScreen.LanguageSelectionListWidget.LanguageEntry> initialComponents;

    @Unique
    private TextFieldWidget searchText;

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
    private void lazyLanguageLoader$$init(CallbackInfo ci) {
        initialComponents = new ArrayList<>(languageSelectionList.children());

        int w = width / 5;

        searchText = new TextFieldWidget(textRenderer, width - (w + 5), 11, w, 15, LiteralText.EMPTY);

        searchText.setSuggestion(lazyLanguageLoader$$truncateByWidth(Constants.SUGGESTION_TEXT, searchText, Constants.TRUNCATION_MARKER));
        searchText.setChangedListener(this::lazyLanguageLoader$$handleText);

        addDrawableChild(searchText);
    }

    @Unique
    private void lazyLanguageLoader$$handleText(String text) {
        List<LanguageOptionsScreen.LanguageSelectionListWidget.LanguageEntry> children = languageSelectionList.children();

        if (text.isBlank() || text.isBlank()) {
            int initialSize = initialComponents.size();
            int currentSize = children.size();

            if (initialSize != currentSize) {
                languageSelectionList.replaceEntries(initialComponents);
            }

            searchText.setSuggestion(lazyLanguageLoader$$truncateByWidth(Constants.SUGGESTION_TEXT, searchText, Constants.TRUNCATION_MARKER));
        } else {
            searchText.setSuggestion(LiteralText.EMPTY.asString());
            for (LanguageOptionsScreen.LanguageSelectionListWidget.LanguageEntry entry : initialComponents) {
                LanguageDefinition def = ((LanguageEntryAccessor) entry).getLanguageDefinition();

                if (StateManager.isMatchable(text, def)) {
                    lazyLanguageLoader$$safeAdd(entry);
                } else {
                    languageSelectionList.removeEntry(entry);
                }
            }
        }
        lazyLanguageLoader$$fixScroll();
    }

    @Unique
    private void lazyLanguageLoader$$fixScroll() {
        if (((Scrollable) languageSelectionList).hasScrolled()) {
            languageSelectionList.setScrollAmount(languageSelectionList.getScrollAmount());
        } else {
            languageSelectionList.centerScrollOn(languageSelectionList.getSelectedOrNull());
        }
    }

    @Unique
    private void lazyLanguageLoader$$safeAdd(LanguageOptionsScreen.LanguageSelectionListWidget.LanguageEntry entry) {
        if (!languageSelectionList.children().contains(entry)) {
            languageSelectionList.addEntry(entry);
        }
    }

    @Unique
    private String lazyLanguageLoader$$truncateByWidth(String text, ClickableWidget widget, String marker) {
        int textWidth = textRenderer.getWidth(text);
        int widgetWidth = widget.getWidth();

        if (textWidth > widgetWidth) {
            String truncatedText = text;
            int truncatedWidth = textWidth;

            while (truncatedWidth > widgetWidth) {
                truncatedText = truncatedText.substring(0, truncatedText.length() - 1);
                truncatedWidth = textRenderer.getWidth(truncatedText);
            }

            return lazyLanguageLoader$$addTruncationMarker(truncatedText, marker);
        } else {
            return text;
        }
    }

    private String lazyLanguageLoader$$addTruncationMarker(String text, String marker) {
        return text.length() > marker.length() ? text.substring(0, text.length() - marker.length()) : marker;
    }
}
