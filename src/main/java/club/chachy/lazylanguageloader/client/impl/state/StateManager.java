package club.chachy.lazylanguageloader.client.impl.state;

import club.chachy.lazylanguageloader.client.api.language.LanguageMatcher;
import club.chachy.lazylanguageloader.client.impl.language.CodeLanguageMatcher;
import club.chachy.lazylanguageloader.client.impl.language.NameLanguageMatcher;
import club.chachy.lazylanguageloader.client.impl.language.RegionLanguageMatcher;
import net.minecraft.client.resource.language.LanguageDefinition;
import net.minecraft.client.resource.language.LanguageManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StateManager {
    private static boolean resourceLoadViaLanguage = false;
    private static LanguageManager languageManager = null;

    private static final List<LanguageMatcher> languageMatchers = new ArrayList<>();

    public static boolean isResourceLoadViaLanguage() {
        return resourceLoadViaLanguage;
    }

    public static void setResourceLoadViaLanguage(boolean resourceLoadViaLanguage) {
        StateManager.resourceLoadViaLanguage = resourceLoadViaLanguage;
    }

    public static LanguageManager getLanguageManager() {
        if (languageManager == null) {
            throw new RuntimeException("The Language manager was accessed too early or was never set... Please report this to the developer");
        }
        return languageManager;
    }

    public static void setLanguageManager(LanguageManager languageManager) {
        StateManager.languageManager = languageManager;
    }

    public static boolean isMatchable(String input, LanguageDefinition definition) {
        return languageMatchers.stream().anyMatch((m) -> m.matches(input, definition));
    }

    static {
        languageMatchers.addAll(Arrays.asList(new CodeLanguageMatcher(), new NameLanguageMatcher(), new RegionLanguageMatcher()));
    }
}
