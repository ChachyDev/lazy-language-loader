package club.chachy.lazylanguageloader.client.impl.state;

import net.minecraft.resource.ResourceReloader;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StateManager {
    private static final List<ResourceReloader> reloaders = new ArrayList<>();
    private static boolean resourceLoadViaLanguage = false;

    public static boolean isResourceLoadViaLanguage() {
        return resourceLoadViaLanguage;
    }

    public static void setResourceLoadViaLanguage(boolean resourceLoadViaLanguage) {
        StateManager.resourceLoadViaLanguage = resourceLoadViaLanguage;
    }

    public static List<ResourceReloader> getResourceReloaders() {
        return reloaders;
    }

    /**
     * If any developer wants to workaround lazy-language-loader you could depend on it via Jitpack and add your resource reloader here
     * if not it will not be reloaded. Sadly if your resource reloader doesn't derive from SearchManager or LanguageManager there isn't much
     * more I can do to determine that you do stuff with languages...
     *
     * @param reloader Reloader to be used on language reloads
     */
    public static void addResourceReloader(ResourceReloader reloader) {
        reloaders.add(reloader);
    }

    public static boolean isMatchable(String input, Text definition) {
        return isMatchable(input, definition.getString());
    }

    public static boolean isMatchable(String input, String definition) {
        return definition.toLowerCase(Locale.ROOT).contains(input.toLowerCase(Locale.ROOT));
    }
}
