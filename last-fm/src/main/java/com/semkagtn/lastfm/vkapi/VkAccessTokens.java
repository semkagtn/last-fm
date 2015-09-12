package com.semkagtn.lastfm.vkapi;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by semkagtn on 31.08.15.
 */
public class VkAccessTokens {

    private static Random random = new Random();

    private static List<String> tokens = Arrays.asList(
            "2177a64b447ae774b2f1a81cc5256ef853d9d981b5a700e104f3a68c2a2d9085091bebd8a7bf6ec4edf716f2b2f54",
            "69f3f20ee721b8ceb640cc17b0afe052354a823228f1a03882f32f33322c9eb7756a820040772cc71baa159ee28b7",
            "89b406d736315c57754a65c5f3bdf44032d67ed0e1b4a6c4c2238f36a1586e458dd5a78fd162ca3d477bc",
            "602ad06b0723e4e4063fc6f3ff27e9c04d8b4309d15cbbdb1d61c2c643afff3b5cc56892701c46c39839e33fb9d87"
    );

    private VkAccessTokens() {

    }

    public static String get(int index) {
        return tokens.get(index);
    }

    public static String getRandom() {
        int index = random.nextInt(tokens.size());
        return get(index);
    }
}
