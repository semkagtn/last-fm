package com.semkagtn.lastfm;

import com.semkagtn.lastfm.httpclient.HttpClient;
import com.semkagtn.lastfm.httpclient.HttpClientConfig;
import com.semkagtn.lastfm.vkapi.VkApi;
import com.semkagtn.lastfm.vkapi.userwalker.PredicateVkUserWalker;
import com.semkagtn.lastfm.vkapi.userwalker.RandomRecursiveVkUserWalker;
import com.semkagtn.lastfm.vkapi.userwalker.UserPredicates;
import com.semkagtn.lastfm.vkapi.userwalker.VkUserWalker;

import static com.semkagtn.lastfm.vkapi.userwalker.UserPredicates.*;
import static com.semkagtn.lastfm.vkapi.userwalker.UserPredicates.hasGender;

/**
 * Created by semkagtn on 16.09.15.
 */
public class Test3 {

    public static void main(String[] args) {
        VkApi api = new VkApi(new HttpClient(HttpClientConfig.newInstance().build()),
                "a402e32e60afec1d02f4c247469c9a73e718f25d1833d0ba9690af883916f9038768af5898ac786115f9c");
        VkUserWalker walker = new PredicateVkUserWalker(
                new RandomRecursiveVkUserWalker(0, 0, api),
                        minimumAudios(50).and(
                                hasAvatar().and(
                                        hasAge().or(hasGender()))));
        walker.nextUser();
    }
}
