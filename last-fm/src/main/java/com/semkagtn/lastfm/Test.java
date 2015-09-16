package com.semkagtn.lastfm;

import com.semkagtn.lastfm.httpclient.HttpClient;
import com.semkagtn.lastfm.httpclient.HttpClientConfig;
import com.semkagtn.lastfm.vkapi.VkApi;
import com.semkagtn.lastfm.vkapi.response.FriendsGetResponse;
import com.semkagtn.lastfm.vkapi.response.UserItem;
import com.semkagtn.lastfm.vkapi.response.UsersGetResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by semkagtn on 12.09.15.
 */
public class Test {

    public static void main(String[] args) {
        VkApi api = new VkApi(new HttpClient(HttpClientConfig.newInstance().build()), "a402e32e60afec1d02f4c247469c9a73e718f25d1833d0ba9690af883916f9038768af5898ac786115f9c");

        List<UserItem> users = new ArrayList<>();
        List<UserItem> kelleFriends = api.friendsGet(32900511, 0, 10).getResponse().getItems();
        for (UserItem user : kelleFriends) {
            FriendsGetResponse response = api.friendsGet(user.getId(), 0, 50);
            if (response.getResponse() != null) {
                List<String> userIds = response.getResponse().getItems()
                        .stream()
                        .map(x -> String.valueOf(x.getId()))
                        .collect(Collectors.toList());
                for (String userId : userIds) {
                    UsersGetResponse usersGetResponse = api.usersGet(Arrays.asList(userId));
                    if (usersGetResponse.getResponse() != null) {
                        users.addAll(usersGetResponse.getResponse());
                    }
                }
            }
        }
        users = users.stream()
                .filter(user -> user.getCounters() != null && user.getCounters().getAudios() != null)
                .collect(Collectors.toList());
        int less100 = 0;
        int less200 = 0;
        int less300 = 0;
        int less400 = 0;
        int less500 = 0;
        int less600 = 0;
        int less700 = 0;
        int greater700 = 0;
        for (UserItem user : users) {
            int audios = user.getCounters().getAudios();
            if (audios < 100) {
                less100++;
            } else if (audios < 200) {
                less200++;
            } else if (audios < 300) {
                less300++;
            } else if (audios < 400) {
                less400++;
            } else if (audios < 500) {
                less500++;
            } else if (audios < 600) {
                less600++;
            } else if (audios < 700) {
                less700++;
            } else {
                greater700++;
            }
        }
        System.out.println("TOTAL USERS: " + users.size());
        System.out.println("less100: " + less100);
        System.out.println("less200: " + less200);
        System.out.println("less300: " + less300);
        System.out.println("less400: " + less400);
        System.out.println("less500: " + less500);
        System.out.println("less600: " + less600);
        System.out.println("less700: " + less700);
        System.out.println("greater700: " + greater700);
    }
}
