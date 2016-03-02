package com.semkagtn.musicdatamining.database;

import com.semkagtn.musicdatamining.*;
import com.semkagtn.musicdatamining.lastfmapi.model.item.ArtistItem;
import com.semkagtn.musicdatamining.lastfmapi.model.item.LastFmUserItem;
import com.semkagtn.musicdatamining.lastfmapi.model.item.TagItem;
import com.semkagtn.musicdatamining.lastfmapi.model.item.TrackItem;
import com.semkagtn.musicdatamining.utils.DateTimeUtils;
import com.semkagtn.musicdatamining.utils.HashUtils;
import com.semkagtn.musicdatamining.vkapi.response.AudioItem;
import com.semkagtn.musicdatamining.vkapi.response.UserItem;

/**
 * Created by semkagtn on 08.09.15.
 */
public class EntityConverter {

    public static Users convertUser(UserItem userItem) {
        Users users = new Users();
        users.setId(userItem.getId());
        users.setGender(userItem.getSex() == 1 ? "f" : userItem.getSex() == 2 ? "m" : null);
        users.setBirthday(DateTimeUtils.birthdayToUnixTime(userItem.getBdate()));
        return users;
    }

    public static Artists convertArtist(ArtistItem artistItem) {
        Artists artists = new Artists();
        artists.setArtistName(artistItem.getName());
        artists.setId(HashUtils.md5(artistItem.getName()));
        return artists;
    }

    public static Tracks convertTrack(TrackItem trackItem, int genreId) {
        Tracks tracks = new Tracks();
        tracks.setTrackName(trackItem.getName());
        tracks.setGenre(genreId >= 1 && genreId <= 22 && genreId != 20 ? genreId : 18);
        String artistName = trackItem.getArtist() != null ? trackItem.getArtist().getName() : "";
        tracks.setId(HashUtils.md5(trackItem.getName() + artistName));
        return tracks;
    }

    public static Tags convertTag(TagItem tagItem) {
        Tags tags = new Tags();
        tags.setTagName(tagItem.getName());
        tags.setId(HashUtils.md5(tagItem.getName()));
        return tags;
    }

    public static LastfmUsers convertLastFmUser(LastFmUserItem userItem) {
        LastfmUsers users = new LastfmUsers();
        users.setCountry(userItem.getCountry());
        users.setGender(userItem.getGender().charAt(0));
        users.setId(HashUtils.md5(userItem.getName()));
        return users;
    }

    public static LastfmArtists convertLastFmArtist(ArtistItem artistItem) {
        LastfmArtists artists = new LastfmArtists();
        artists.setArtistName(artistItem.getName());
        artists.setId(HashUtils.md5(artistItem.getName()));
        return artists;
    }

    public static LastfmTracks convertLastFmTrack(TrackItem trackItem) {
        LastfmTracks tracks = new LastfmTracks();
        tracks.setTrackName(trackItem.getName());
        tracks.setId(HashUtils.md5(trackItem.getName()));
        return tracks;
    }

    public static DUsers convertUserToDUser(UserItem userItem) {
        DUsers users = new DUsers();
        users.setId(userItem.getId());
        users.setGender(userItem.getSex() == 1 ? "f" : userItem.getSex() == 2 ? "m" : null);
        users.setBirthday(DateTimeUtils.birthdayToUnixTime(userItem.getBdate()));
        return users;
    }

    public static DTracks convertTrackToDTrack(AudioItem audioItem) {
        DTracks tracks = new DTracks();
        tracks.setArtistName(audioItem.getArtist());
        tracks.setTrackName(audioItem.getTitle());
        tracks.setId(HashUtils.md5(audioItem.getArtist() + audioItem.getTitle()));
        return tracks;
    }

    private EntityConverter() {

    }
}
