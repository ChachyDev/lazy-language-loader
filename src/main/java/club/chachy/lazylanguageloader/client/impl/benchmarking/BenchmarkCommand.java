package club.chachy.lazylanguageloader.client.impl.benchmarking;

import club.chachy.lazylanguageloader.client.impl.state.StateManager;
import club.chachy.lazylanguageloader.client.impl.utils.Constants;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.LanguageDefinition;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BenchmarkCommand {
    private static final SecureRandom RANDOM = new SecureRandom();

    public static void register() {
        ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("language_loader_benchmark").executes(context -> {
            String timestamp = generateTimestamp();
            Path snapshotFile = FabricLoader.getInstance().getGameDir()
                .resolve(".snapshots")
                .resolve("lazy-language-loader")
                .resolve("benchmark-" + timestamp + "-snapshot.json");

            context.getSource().sendFeedback(new LiteralText(Constants.BENCHMARK_TEXT.formatted(snapshotFile.toString())).formatted(Formatting.BOLD, Formatting.RED));

            LanguageDefinition defaultLanguage = getDefaultLanguage();

            LanguageDefinition randomLanguage = chooseLanguage();

            long noOptTime = testNoOpt(randomLanguage);
            cleanup(defaultLanguage);

            long optTime = testOpt(randomLanguage);
            cleanup(defaultLanguage);

            boolean decrease = optTime < noOptTime;
            String text = "%s%s".formatted(Math.toIntExact(((decrease ? noOptTime - optTime : optTime - noOptTime) / optTime) * 100), "% " + (decrease ? "faster" : " slower"));

            context.getSource().sendFeedback(new LiteralText(Constants.BENCHMARK_FINISH_TEXT.formatted(snapshotFile.toString(), text)).formatted(Formatting.BOLD, Formatting.GREEN));

            try {
                FileUtils.write(snapshotFile.toFile(), createData(noOptTime, optTime, randomLanguage), Charset.defaultCharset());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return 0;
        }));
    }

    private static long testNoOpt(LanguageDefinition languageDefinition) {
        return changeLanguage(languageDefinition);
    }

    private static long testOpt(LanguageDefinition languageDefinition) {
        StateManager.setResourceLoadViaLanguage(true);
        long time = changeLanguage(languageDefinition);
        StateManager.setResourceLoadViaLanguage(false);
        return time;
    }

    private static void cleanup(LanguageDefinition oldLang) {
        changeLanguage(oldLang);
    }

    private static long changeLanguage(LanguageDefinition definition) {
        long startTime = System.nanoTime();

        MinecraftClient.getInstance().getLanguageManager().setLanguage(definition);
        MinecraftClient.getInstance().reloadResources();

        return System.nanoTime() - startTime;
    }

    private static String generateTimestamp() {
        return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
    }

    private static String createData(long timeTakenNoOpt, long timeTakenOpt, LanguageDefinition def) {
        JsonObject obj = new JsonObject();

        JsonObject noOpt = new JsonObject();
        noOpt.addProperty("time_taken", timeTakenNoOpt);
        noOpt.addProperty("language", def.getName());
        obj.add("no_opt", noOpt);

        JsonObject opt = new JsonObject();
        opt.addProperty("time_taken", timeTakenOpt);
        opt.addProperty("language", def.getName());
        obj.add("opt", opt);

        return obj.toString();
    }

    private static LanguageDefinition chooseLanguage() {
        List<LanguageDefinition> languageDefinitionList = new ArrayList<>(MinecraftClient.getInstance().getLanguageManager().getAllLanguages());

        return languageDefinitionList.get(RANDOM.nextInt(0, languageDefinitionList.size() - 1));
    }

    private static LanguageDefinition getDefaultLanguage() {
        return MinecraftClient.getInstance().getLanguageManager().getLanguage();
    }
}
