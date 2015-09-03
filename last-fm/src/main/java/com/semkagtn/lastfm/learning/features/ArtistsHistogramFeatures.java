package com.semkagtn.lastfm.learning.features;

import com.semkagtn.lastfm.Artists;
import com.semkagtn.lastfm.Users;
import com.semkagtn.lastfm.database.Database;
import com.semkagtn.lastfm.learning.NumericFeature;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by semkagtn on 11.03.15.
 */
public class ArtistsHistogramFeatures {
//
//    public static List<NumericFeature> getFeatures(int topArtistsLimit) {
//        List<NumericFeature> features = Database.select(Artists.class)
//                .stream()
//                .sorted((x, y) -> Integer.valueOf(y.getListeners()).compareTo(x.getListeners()))
//                .limit(topArtistsLimit)
//                .map(artist -> new NumericFeature("ARTIST: " + artist.getArtistName()) {
//                    @Override
//                    public Double calculate(Users user) {
//                        return (double) user.getRecentTrackses()
//                                .stream()
//                                .filter(x -> {
//                                    Artists otherArtist = x.getTracks().getArtists();
//                                    return otherArtist != null &&
//                                            otherArtist.getArtistName().equals(artist.getArtistName());
//                                }).count();
//                    }
//                }).collect(Collectors.toList());
//        return features;
//    }
//
//    private ArtistsHistogramFeatures() {
//
//    }
}
