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
            "b41245361fb120b0540938c2567ac37b64e56d0c87afecf564bbc2b4f16274e32f2eb0b64e0bfaea238a6"
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
