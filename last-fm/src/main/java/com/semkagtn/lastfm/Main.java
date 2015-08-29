package com.semkagtn.lastfm;

import com.semkagtn.lastfm.api.ApiConfig;
import com.semkagtn.lastfm.api.ApiKeys;
import com.semkagtn.lastfm.database.Database;
import org.apache.http.protocol.RequestUserAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by semkagtn on 20.07.15.
 */
public class Main {

    private static final int THREADS_COUNT = 1;
    private static final int USER_COUNT = 1;

    public static void main(String[] args) throws InterruptedException {
        ApiConfig apiConfig = ApiConfig.newInstance()
                .withApiKey(ApiKeys.getRandom())
                .build();

        Database.open();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < THREADS_COUNT; i++) {
            Thread thread = new Thread(new DataCollector(apiConfig, USER_COUNT));
            threads.add(thread);
            thread.run();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        Database.close();
    }
}