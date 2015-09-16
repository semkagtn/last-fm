package com.semkagtn.musicdatamining;

import com.semkagtn.musicdatamining.database.Database;
import com.semkagtn.musicdatamining.lastfmapi.LastFmApiKeys;

/**
 * Created by semkagtn on 20.07.15.
 */
public class Main {

    private static final int THREAD_COUNT = 4;

    public static void main(String[] args) throws InterruptedException {
        Database.open();
//
        String lastFmApiKey = LastFmApiKeys.get(3);
        String vkAccessToken = "a402e32e60afec1d02f4c247469c9a73e718f25d1833d0ba9690af883916f9038768af5898ac786115f9c";

//        List<Thread> threads = new ArrayList<>();
//        for (int i = 0; i < THREAD_COUNT; i++) {
//            String lastFmApiKey = LastFmApiKeys.get(i);
//            String vkAccessToken = VkAccessTokens.get(i);
//            Thread thread = new Thread(() -> {
                DataCollector dataCollector = new DataCollector(lastFmApiKey, vkAccessToken);
                dataCollector.collect();
//            });
//            threads.add(thread);
//        }
//        threads.forEach(Thread::start);
//        for (Thread thread : threads) {
//            thread.join();
//        }

        Database.close();
    }
}
