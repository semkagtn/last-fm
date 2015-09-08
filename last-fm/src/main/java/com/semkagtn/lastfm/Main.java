package com.semkagtn.lastfm;

import com.semkagtn.lastfm.database.Database;
import com.semkagtn.lastfm.lastfmapi.LastFmApiKeys;
import com.semkagtn.lastfm.vkapi.VkAccessTokens;

/**
 * Created by semkagtn on 20.07.15.
 */
public class Main {

    public static void main(String[] args) {
        Database.open();

        DataCollector dataCollector = new DataCollector(
                LastFmApiKeys.getRandom(), VkAccessTokens.getRandom());
        dataCollector.collect();

        Database.close();
    }
}
