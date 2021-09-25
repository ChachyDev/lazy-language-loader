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

    public static void addResourceReloader(ResourceReloader manager) {
        reloaders.add(manager);
    }
}
