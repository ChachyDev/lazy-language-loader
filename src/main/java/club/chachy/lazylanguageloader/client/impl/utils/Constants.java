package club.chachy.lazylanguageloader.client.impl.utils;

public class Constants {
    public static final String SUGGESTION_TEXT = "Search for a language";

    public static final String TRUNCATION_MARKER = "...";

    public static final String BENCHMARK_TEXT = "This is a benchmark for lazy-language-loader, it works by effectively " +
        "recreating the same experience as choosing another language in the language menu and testing it against when optimizations " +
        "have been applied and when they haven't. After the data from the benchmark is exported to your computer at %s and the results " +
        "and statistics such as percentage faster/slower, etc. will be displayed in the game chat. Beware, this may freeze your game!!!";

    public static final String BENCHMARK_FINISH_TEXT = "The benchmark has been completed! Check %s for your exported data and here are some stats " +
        "taken from your benchmark. " +
        "" +
        "With Optimizations enabled your game was: %s";
}
