package com.semkagtn.lastfm;


import com.semkagtn.lastfm.api.Api;
import com.semkagtn.lastfm.api.ApiConfig;
import com.semkagtn.lastfm.api.ApiKeys;
import com.semkagtn.lastfm.api.User;

/**
 * Created by semkagtn on 20.07.15.
 */
public class Test {

    public static void main(String[] args) {
        Api api = new Api(ApiConfig.newInstance()
                .withApiKey(ApiKeys.getRandom())
                .build());
//        Artist album = api.call(Artist.GetInfo.createRequest("Kasabian"));
//        System.out.println();

        User user = api.call(User.GetInfo.createRequest("semkagtn"));
        System.out.println();
    }
}
