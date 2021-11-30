package club.chachy.lazylanguageloader.client.impl.language;

import club.chachy.lazylanguageloader.client.api.language.LanguageMatcher;
import net.minecraft.client.resource.language.LanguageDefinition;

import java.util.Locale;

public class CodeLanguageMatcher implements LanguageMatcher {
    @Override
    public boolean matches(String input, LanguageDefinition entry) {
        return entry.getCode().toLowerCase(Locale.ROOT).contains(input.toLowerCase(Locale.ROOT));
    }
}
