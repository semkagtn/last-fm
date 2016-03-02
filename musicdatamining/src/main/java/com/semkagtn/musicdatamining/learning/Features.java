package com.semkagtn.musicdatamining.learning;

import com.semkagtn.musicdatamining.*;
import com.semkagtn.musicdatamining.utils.DateTimeUtils;
import javafx.util.Pair;
import weka.core.Attribute;
import weka.core.FastVector;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by semkagtn on 03.11.15.
 */
public class Features {

    public static NominalFeature<Users> gender() {
        final String male = "m";
        final String female = "f";

        FastVector vector = new FastVector(2);
        vector.addElement(male);
        vector.addElement(female);
        Attribute attribute = new Attribute("gender", vector);
        return new NominalFeature<>(attribute, Users::getGender);
    }

    public static NominalFeature<Users> youngOrOld(long birthdayMedian) {
        final String young = "young";
        final String old = "old";

        FastVector vector = new FastVector(2);
        vector.addElement(young);
        vector.addElement(old);
        Attribute attribute = new Attribute("youngOrOld", vector);
        Function<Users, String> function = user -> user.getBirthday() <= birthdayMedian ? young : old;
        return new NominalFeature<>(attribute, function);
    }

    public static NominalFeature<Users> youngOrOldAge(int ageMedian) {
        final String young = "less" + ageMedian;
        final String old = "greater" + ageMedian;
        FastVector vector = new FastVector(2);
        vector.addElement(young);
        vector.addElement(old);
        Attribute attribute = new Attribute("age2", vector);
        Function<Users, String> function = user -> {
            Integer age = DateTimeUtils.unixTimeToAge(user.getBirthday());
            if (age == null || age == ageMedian) {
                return null;
            } else if (age < ageMedian) {
                return young;
            } else {
                return old;
            }
        };
        return new NominalFeature<>(attribute, function);
    }

    public static NumericFeature<Users> birthday() {
        Attribute attribute = new Attribute("birthday");
        return new NumericFeature<>(attribute, user -> Double.valueOf(user.getBirthday()));
    }

    public static NumericFeature<Users> age() {
        Attribute attribute = new Attribute("age");
        return new NumericFeature<>(attribute,
                user -> {
                    Integer age = DateTimeUtils.unixTimeToAge(user.getBirthday());
                    return age == null ? null : age.doubleValue();
                }
        );
    }

    public static MultiFeature<Users, Double> genreHistogram(List<GenresDict> genresDict, int maximumTracks) {
        List<Attribute> attributes = genresDict.stream()
                .map(genre -> new Attribute("genre:" + genre.getGenreName()))
                .collect(Collectors.toList());
        Function<Users, List<Double>> function = user -> {
            Map<Integer, Long> genreCounts = user.getUsersTrackses().stream()
                    .map(UsersTracks::getTracks)
                    .limit(maximumTracks)
                    .map(Tracks::getGenre)
                    .collect(Collectors.groupingBy(genre -> genre, Collectors.counting()));
            return genresDict.stream()
                    .map(genre -> Double.valueOf(genreCounts.getOrDefault(genre.getGenreId(), 0L)))
                    .collect(Collectors.toList());
        };
        return new MultiFeature<>(attributes, function);
    }

    public static MultiFeature<Users, Double> artistTagsHistogram(List<Tags> tags, int maximumTracks) {
        Set<String> tagsSet = tags.stream()
                .map(Tags::getId)
                .collect(Collectors.toSet());
        List<Attribute> attributes = tags.stream()
                .map(tag -> new Attribute("artistTag:" + tag.getTagName()))
                .collect(Collectors.toList());
        Function<Users, List<Double>> function = user -> {
            Map<String, Long> tagsCount = user.getUsersTrackses().stream()
                    .map(UsersTracks::getTracks)
                    .limit(maximumTracks)
                    .map(track -> track.getArtists().getArtistsTagses())
                    .flatMap(Collection::stream)
                    .map(artistTag -> artistTag.getTags().getId())
                    .filter(tagsSet::contains)
                    .collect(Collectors.groupingBy(id -> id, Collectors.counting()));
            return tags.stream()
                    .map(tag -> Double.valueOf(tagsCount.getOrDefault(tag.getId(), 0L)))
                    .collect(Collectors.toList());
        };
        return new MultiFeature<>(attributes, function);
    }

    public static MultiFeature<Users, Double> trackTagsHistogram(List<Tags> tags, int maximumTracks) {
        Set<String> tagsSet = tags.stream()
                .map(Tags::getId)
                .collect(Collectors.toSet());
        List<Attribute> attributes = tags.stream()
                .map(tag -> new Attribute("trackTag:" + tag.getTagName()))
                .collect(Collectors.toList());
        Function<Users, List<Double>> function = user -> {
            Map<String, Long> tagsCount = user.getUsersTrackses().stream()
                    .map(UsersTracks::getTracks)
                    .limit(maximumTracks)
                    .map(Tracks::getTracksTagses)
                    .flatMap(Collection::stream)
                    .map(artistTag -> artistTag.getTags().getId())
                    .filter(tagsSet::contains)
                    .collect(Collectors.groupingBy(id -> id, Collectors.counting()));
            return tags.stream()
                    .map(tag -> Double.valueOf(tagsCount.getOrDefault(tag.getId(), 0L)))
                    .collect(Collectors.toList());
        };
        return new MultiFeature<>(attributes, function);
    }

    public static MultiFeature<Users, Double> artistHistogram(List<Artists> artists, int maximumTracks) {
        Set<String> artistsSet = artists.stream()
                .map(Artists::getId)
                .collect(Collectors.toSet());
        List<Attribute> attributes = artists.stream()
                .map(artist -> new Attribute("artist:" + artist.getArtistName()))
                .collect(Collectors.toList());
        Function<Users, List<Double>> function = user -> {
            Map<String, Long> artistsCount = user.getUsersTrackses().stream()
                    .map(UsersTracks::getTracks)
                    .limit(maximumTracks)
                    .map(track -> track.getArtists().getId())
                    .filter(artistsSet::contains)
                    .collect(Collectors.groupingBy(id -> id, Collectors.counting()));
            return artists.stream()
                    .map(artist -> Double.valueOf(artistsCount.getOrDefault(artist.getId(), 0L)))
                    .collect(Collectors.toList());
        };
        return new MultiFeature<>(attributes, function);
    }


    public static NominalFeature<LastfmUsers> lastFmGender() {
        final String male = "m";
        final String female = "f";

        FastVector vector = new FastVector(2);
        vector.addElement(male);
        vector.addElement(female);
        Attribute attribute = new Attribute("gender", vector);
        return new NominalFeature<>(attribute, u -> String.valueOf(u.getGender()));
    }


    private Features() {

    }
}
