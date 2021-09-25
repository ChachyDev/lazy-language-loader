package club.chachy.lazylanguageloader.client.state;

import net.minecraft.resource.ResourceReloader;

import java.util.ArrayList;
import java.util.List;

public class StateManager {
    private static boolean resourceLoadViaLanguage = false;
    private static final List<ResourceReloader> reloaders = new ArrayList<>();

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
     * @param reloader Reloader to be used on language reloads
     */
    public static void addResourceReloader(ResourceReloader reloader) {
        reloaders.add(reloader);
    }
}
