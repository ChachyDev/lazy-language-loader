package club.chachy.lazylanguageloader.client.state;

import net.minecraft.client.resource.language.LanguageManager;

public class StateManager {
    private static boolean resourceLoadViaLanguage = false;
    private static LanguageManager languageManager = null;

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
}
