package com.semkagtn.lastfm;


import com.semkagtn.lastfm.vkapi.VkAccessTokens;
import com.semkagtn.lastfm.vkapi.VkApi;
import com.semkagtn.lastfm.vkapi.VkApiConfig;
import com.semkagtn.lastfm.vkapi.response.UserItem;
import com.semkagtn.lastfm.vkapi.response.WallGetFilter;
import com.semkagtn.lastfm.vkapi.response.WallGetResponse;
import com.semkagtn.lastfm.vkapi.response.WallItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by semkagtn on 20.07.15.
 */
public class Test {

    private static final int MAX_WALL_ITEMS = 100;

    public static void main(String[] args) throws IOException {
        VkApi api = new VkApi(VkApiConfig.newInstance()
                .withToken(VkAccessTokens.getRandom())
                .build());

        int userId = api.usersGet(Arrays.asList("id32900511")).getResponse().get(0).getId();
        List<UserItem> friends = api.friendsGet(userId, 0, 5000).getResponse().getItems();

        List<Integer> allResults = new ArrayList<>();
        for (UserItem friend : friends) {
            int friendId = friend.getId();
            List<WallItem> friendResult = new ArrayList<>();
            WallGetResponse response = api.wallGet(friendId, 0, 0, WallGetFilter.ALL);
            if (response.getError() != null) {
                continue;
            }
            int allPosts = response.getResponse().getCount();
            int ownerPosts = api.wallGet(friendId, 0, 0, WallGetFilter.OWNER).getResponse().getCount();
            for (int offset = 0; offset < ownerPosts; offset += MAX_WALL_ITEMS) {
                friendResult.addAll(api.wallGet(friendId, offset, offset + MAX_WALL_ITEMS, WallGetFilter.OWNER)
                        .getResponse().getItems().stream()
                        .filter(post -> post.getAttachments() != null && post.getAttachments().size() > 0)
                        .collect(Collectors.toList()));
            }
            int tracks = friendResult.stream()
                    .mapToInt(x -> x.getAttachments().size())
                    .sum();
            allResults.add(tracks);
        }
        allResults.sort(Integer::compareTo);
        System.out.println(allResults);
    }
}
