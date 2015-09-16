package com.semkagtn.musicdatamining.database;

import com.semkagtn.musicdatamining.Artists;
import com.semkagtn.musicdatamining.Tags;
import com.semkagtn.musicdatamining.Tracks;
import com.semkagtn.musicdatamining.Users;
import com.semkagtn.musicdatamining.lastfmapi.response.ArtistItem;
import com.semkagtn.musicdatamining.lastfmapi.response.TagItem;
import com.semkagtn.musicdatamining.lastfmapi.response.TrackItem;
import com.semkagtn.musicdatamining.utils.DateTimeUtils;
import com.semkagtn.musicdatamining.utils.HashUtils;
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
        tracks.setGenre(genreId >= 1 && genreId <= 22 ? genreId : 18);
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

    private EntityConverter() {

    }
}
