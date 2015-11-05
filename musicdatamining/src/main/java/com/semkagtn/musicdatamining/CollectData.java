package com.semkagtn.musicdatamining;

import com.semkagtn.musicdatamining.lastfmapi.LastFmApiKeys;
import com.semkagtn.musicdatamining.utils.LoggerUtils;
import com.semkagtn.musicdatamining.vkapi.VkAccessTokens;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by semkagtn on 20.07.15.
 */
public class CollectData {

    private static final int THREAD_COUNT = 4;

    public static void main(String[] args) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < THREAD_COUNT; i++) {
            String lastFmApiKey = LastFmApiKeys.get(i);
            String vkAccessToken = VkAccessTokens.get(i);

            Thread thread = new Thread(() -> {
                Logger logger = LoggerUtils.getUniqueFileLogger();
                DataCollector dataCollector = new DataCollector(lastFmApiKey, vkAccessToken, logger);
                dataCollector.collect();
            });
            threads.add(thread);
        }
        threads.forEach(Thread::start);
        for (Thread thread : threads) {
            thread.join();
        }
    }
}
