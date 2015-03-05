package com.semkagtn.lastfm;

import com.semkagtn.lastfm.api.Api;
import com.semkagtn.lastfm.api.Artist;
import com.semkagtn.lastfm.api.cache.ExpirationPolicy;
import com.semkagtn.lastfm.api.cache.FileSystemCache;

/**
 * Created by semkagtn on 3/2/15.
 */
public class Test {

    private static final String API_KEY = "c3f08d2af7231974467281edd2836cf3";

    public static void main(String[] args) throws Exception {
        Api.setApiKey(API_KEY);
        Api.setCache(new FileSystemCache());
        Api.setCacheExpirationPolicy(ExpirationPolicy.TWO_MONTHS);
        Api.enableLogger(true);

        Artist artist = Artist.getInfo("Green Day");
        System.out.println(artist.getName() + " " + artist.getTags());
    }
}
