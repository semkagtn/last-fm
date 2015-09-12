package com.semkagtn.lastfm;

import com.semkagtn.lastfm.database.Database;
import com.semkagtn.lastfm.httpclient.HttpClient;
import com.semkagtn.lastfm.httpclient.HttpClientConfig;
import com.semkagtn.lastfm.lastfmapi.LastFmApiKeys;
import com.semkagtn.lastfm.vkapi.VkAccessTokens;
import com.semkagtn.lastfm.vkapi.VkApi;
import com.semkagtn.lastfm.vkapi.response.AudioAttachment;
import com.semkagtn.lastfm.vkapi.response.AudioItem;
import com.semkagtn.lastfm.vkapi.response.ExecuteGetAttachmentsResponse;
import com.semkagtn.lastfm.vkapi.response.WallGetFilter;
import org.apache.commons.logging.LogFactory;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by semkagtn on 20.07.15.
 */
public class Main {

    private static final int THREAD_COUNT = 4;

    public static void main(String[] args) throws InterruptedException {
        Database.open();
//
        String lastFmApiKey = LastFmApiKeys.get(3);
        String vkAccessToken = VkAccessTokens.get(3);

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

//        VkApi vkApi = new VkApi(new HttpClient(HttpClientConfig.newInstance().build()), VkAccessTokens.get(3));
//        vkApi.wallGet(1602118, 0, 1, WallGetFilter.ALL);
//        int i = 0;
//        for (;; i++) {
//            try {
//                vkApi.usersGet(Arrays.asList("semkagtn"));
//                Thread.sleep(510);
//            } catch (VkApi.FloodControlError e) {
//                break;
//            }
//        }
//        System.out.println(i);
//        vkApi.executeTest();
//        ExecuteGetAttachmentsResponse response = vkApi.executeGetAttachments(32900511, 0, null);
//        List<AudioItem> audios = response.getResponse().stream()
//                .filter(x -> x != null)
//                .flatMap(Collection::stream)
//                .map(AudioAttachment::getAudio)
//                .filter(x -> x != null)
//                .collect(Collectors.toList());
//        System.out.println();
    }
}
