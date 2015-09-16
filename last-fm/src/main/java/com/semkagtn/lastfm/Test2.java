package com.semkagtn.lastfm;

import com.semkagtn.lastfm.httpclient.HttpClient;
import com.semkagtn.lastfm.httpclient.HttpClientConfig;
import com.semkagtn.lastfm.vkapi.VkApi;
import com.semkagtn.lastfm.vkapi.response.FriendsGetResponse;
import com.semkagtn.lastfm.vkapi.response.UserItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by semkagtn on 14.09.15.
 */
public class Test2 {

    public static void main(String[] args) {
        VkApi api = new VkApi(new HttpClient(HttpClientConfig.newInstance().build()),
                "a402e32e60afec1d02f4c247469c9a73e718f25d1833d0ba9690af883916f9038768af5898ac786115f9c");
        FriendsGetResponse response = api.friendsGet(7111901, 0, 500);
        List<UserItem> friends = response.getResponse().getItems();
        List<String> years = friends.stream()
                .map(UserItem::getBdate)
                .filter(bdate -> bdate != null && bdate.split("\\.").length == 3)
                .map(bdate -> bdate.split("\\.")[2]).collect(Collectors.toList());
        Map<String, Integer> yearCount = new HashMap<>();
        for (String year : years) {
            if (!yearCount.containsKey(year)) {
                yearCount.put(year, 0);
            }
            yearCount.put(year, yearCount.get(year) + 1);
        }
        String result = yearCount.entrySet()
                .stream()
                .sorted((x, y) -> -x.getValue().compareTo(y.getValue()))
                .map(x -> x.getKey() + " " + x.getValue())
                .reduce("", (x, y) -> x + "\n" + y);
        System.out.println(result);
    }
}
