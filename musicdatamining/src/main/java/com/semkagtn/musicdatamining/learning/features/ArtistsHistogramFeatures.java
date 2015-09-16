package com.semkagtn.musicdatamining.learning.features;

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
