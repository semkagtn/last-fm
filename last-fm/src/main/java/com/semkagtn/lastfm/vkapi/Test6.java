package com.semkagtn.lastfm.vkapi;

import com.semkagtn.lastfm.api.*;

/**
 * Created by semkagtn on 02.09.15.
 */
public class Test6 {

    public static void main(String[] args) {
        Api lastFmApi = new Api(ApiConfig.newInstance()
                .withApiKey(ApiKeys.getRandom())
                .build());

        lastFmApi.call(Artist.GetInfo.createRequest("receptor"));
    }
}
