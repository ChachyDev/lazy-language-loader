package club.chachy.lazylanguageloader.client;

import club.chachy.lazylanguageloader.client.impl.benchmarking.BenchmarkCommand;
import net.fabricmc.api.ClientModInitializer;

public class LazyLanguageLoaderMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BenchmarkCommand.register();
    }
}
