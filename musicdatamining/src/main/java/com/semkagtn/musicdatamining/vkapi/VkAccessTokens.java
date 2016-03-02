package com.semkagtn.musicdatamining.vkapi;

import com.semkagtn.musicdatamining.utils.ResourceUtils;

import java.util.List;
import java.util.Random;

/**
 * Created by semkagtn on 31.08.15.
 */
public class VkAccessTokens {

    private static final String FILE_NAME = "vk-tokens.tokens";
    private static final String UNOFFICIAL_FILE_NAME = "vk-unofficial-tokens.tokens";

    private static List<String> tokens = ResourceUtils.getFileLines(FILE_NAME);
    private static String unofficialToken = ResourceUtils.getFileLines(UNOFFICIAL_FILE_NAME).get(0);
    private static Random random = new Random();

    private VkAccessTokens() {

    }

    public static String get(int index) {
        return tokens.get(index);
    }

    public static String getRandom() {
        int index = random.nextInt(tokens.size());
        return get(index);
    }

    public static String getUnofficialToken() {
        return unofficialToken;
    }
}
