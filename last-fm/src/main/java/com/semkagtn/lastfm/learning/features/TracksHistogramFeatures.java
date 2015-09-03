package com.semkagtn.lastfm.learning.features;

import com.semkagtn.lastfm.Tracks;
import com.semkagtn.lastfm.Users;
import com.semkagtn.lastfm.database.Database;
import com.semkagtn.lastfm.learning.NumericFeature;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by semkagtn on 11.03.15.
 */
public class TracksHistogramFeatures {

//    public static List<NumericFeature> getFeatures(int topTracksLimit) {
//        List<NumericFeature> features = Database.select(Tracks.class)
//                .stream()
//                .sorted((x, y) -> Integer.valueOf(y.getListeners()).compareTo(x.getListeners()))
//                .limit(topTracksLimit)
//                .map(track -> new NumericFeature("TRACK: " + track.getTrackName() + " " + track.getId()) {
//                    @Override
//                    public Double calculate(Users user) {
//                        return (double) user.getRecentTrackses()
//                                .stream()
//                                .filter(x -> x.getTracks().getId().equals(track.getId()))
//                                .count();
//                    }
//                }).collect(Collectors.toList());
//        return features;
//    }
//
//    private TracksHistogramFeatures() {
//
//    }
}
