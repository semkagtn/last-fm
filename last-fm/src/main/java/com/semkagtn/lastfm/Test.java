package com.semkagtn.lastfm;

import com.semkagtn.lastfm.api.*;

import java.util.List;

/**
 * Created by semkagtn on 3/6/15.
 */
public class Test {

    public static void main(String[] args) {
        User user = Api.call(User.GetInfo.createRequest("floop1k"));
        List<User> friends = Api.call(User.GetFriends.createRequest(String.valueOf(user.getId()), 0, 10));
        List<Track> recentTracks = Api.call(User.GetRecentTracks.createRequest(
                String.valueOf(user.getId()), 0, 200));
        for (Track recentTrack : recentTracks) {
            Artist artist = null;
            try {
                artist = Api.call(Artist.GetInfo.createRequest(recentTrack.getArtist()));
            } catch (Api.CallError e) {

            }
            if (artist != null) {
                for (String tagName : artist.getTags()) {
                    Api.call(Tag.GetInfo.createRequest(tagName));
                }
            }

            Track track = null;
            try {
                track = Api.call(Track.GetInfo.createRequest(recentTrack.getArtist(), recentTrack.getName()));
            } catch (Api.CallError e) {

            }
            if (track != null) {
                for (String tagName : artist.getTags()) {
                    Api.call(Tag.GetInfo.createRequest(tagName));
                }
            }
        }
    }
}
