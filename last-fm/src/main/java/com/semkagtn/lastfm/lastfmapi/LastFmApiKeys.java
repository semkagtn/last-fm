package com.semkagtn.lastfm.lastfmapi;

import com.semkagtn.lastfm.utils.ResourceUtils;

import java.util.List;
import java.util.Random;

/**
 * Created by semkagtn on 3/7/15.
 */
public class LastFmApiKeys {

    private static final String FILE_NAME = "last-fm-api-keys.tokens";

    private static List<String> apiKeys = ResourceUtils.getFileLines(FILE_NAME);
    private static Random random = new Random();

    public static String get(int index) {
        return apiKeys.get(index);
    }

    public static String getRandom() {
        int index = random.nextInt(apiKeys.size());
        return get(index);
    }

    private LastFmApiKeys() {

    }
}
