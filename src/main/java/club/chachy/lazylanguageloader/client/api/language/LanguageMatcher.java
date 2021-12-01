package club.chachy.lazylanguageloader.client.api.language;

import net.minecraft.client.resource.language.LanguageDefinition;

public interface LanguageMatcher {
    boolean matches(String input, LanguageDefinition entry);
}
