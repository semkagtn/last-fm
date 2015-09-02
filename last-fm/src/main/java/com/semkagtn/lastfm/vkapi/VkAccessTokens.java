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
            "9e034ef437265d930fdec8a8014635d71e37527c199ff4dfd51f9c10d7c775f50c7bb6640c8b43368e701a32ce7a6"
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
