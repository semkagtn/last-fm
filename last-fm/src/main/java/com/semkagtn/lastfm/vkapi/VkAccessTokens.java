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
            "9e034ef437265d930fdec8a8014635d71e37527c199ff4dfd51f9c10d7c775f50c7bb6640c8b43368e701a32ce7a6",
            "0d640595ca3be978438c6008480c12b60e1232147bbae5351c5cfb0a13398225c16ccef5db29315ef4619",
            "89b406d736315c57754a65c5f3bdf44032d67ed0e1b4a6c4c2238f36a1586e458dd5a78fd162ca3d477bc",
            "6b1a00451eef00f3ee8803812ffb17550c3da7bdfe758c371c84a0531694a2c6d954c6f7f5da16655f423"
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
