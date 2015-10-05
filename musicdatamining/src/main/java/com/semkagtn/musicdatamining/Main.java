package com.semkagtn.musicdatamining;

import com.semkagtn.musicdatamining.lastfmapi.LastFmApiKeys;
import com.semkagtn.musicdatamining.vkapi.VkAccessTokens;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by semkagtn on 20.07.15.
 */
public class Main {

    private static final int THREAD_COUNT = 8;

    public static void main(String[] args) throws InterruptedException, IOException {
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < THREAD_COUNT; i++) {
            String lastFmApiKey = LastFmApiKeys.get(i);
            String vkAccessToken = VkAccessTokens.get(i);

            Thread thread = new Thread(() -> {
                DataCollector dataCollector = new DataCollector(lastFmApiKey, vkAccessToken);
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
