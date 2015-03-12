package com.semkagtn.lastfm;

import com.semkagtn.lastfm.api.Api;
import com.semkagtn.lastfm.api.Track;
import com.semkagtn.lastfm.api.User;
import com.semkagtn.lastfm.recenttrackscollector.LastRecentTracksCollector;
import com.semkagtn.lastfm.recenttrackscollector.RecentTracksCollector;

import java.util.List;

/**
 * Created by semkagtn on 3/6/15.
 */
public class Test {

    public static void main(String[] args) throws Api.NotJsonInResponseException {
        RecentTracksCollector collector = new LastRecentTracksCollector(1000);
//        System.out.println(Api.call(User.GetInfo.createRequest("11888602")).getPlaycount());
        collector.collect(11888602);
    }
}
