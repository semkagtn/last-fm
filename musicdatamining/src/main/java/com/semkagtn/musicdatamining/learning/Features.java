package com.semkagtn.musicdatamining.learning;

import com.semkagtn.musicdatamining.*;
import com.semkagtn.musicdatamining.utils.DateTimeUtils;
import weka.core.Attribute;
import weka.core.FastVector;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by semkagtn on 03.11.15.
 */
public class Features {

    public static NominalFeature gender() {
        final String male = "m";
        final String female = "f";

        FastVector vector = new FastVector(2);
        vector.addElement(male);
        vector.addElement(female);
        Attribute attribute = new Attribute("gender", vector);
        return new NominalFeature(attribute, Users::getGender);
    }

    public static NominalFeature youngOrOld(long birthdayMedian) {
        final String young = "young";
        final String old = "old";

        FastVector vector = new FastVector(2);
        vector.addElement(young);
        vector.addElement(old);
        Attribute attribute = new Attribute("youngOrOld", vector);
        Function<Users, String> function = user -> user.getBirthday() <= birthdayMedian ? young : old;
        return new NominalFeature(attribute, function);
    }

    public static NumericFeature birthday() {
        Attribute attribute = new Attribute("birthday");
        return new NumericFeature(attribute, user -> Double.valueOf(user.getBirthday()));
    }

    public static NumericFeature age() {
        Attribute attribute = new Attribute("age");
        return new NumericFeature(attribute,
                user -> Double.valueOf(DateTimeUtils.unixTimeToAge(user.getBirthday())));
    }

    public static MultiFeature<Double> genreHistogram(List<GenresDict> genresDict) {
        List<Attribute> attributes = genresDict.stream()
                .map(genre -> new Attribute("genre:" + genre.getGenreName()))
                .collect(Collectors.toList());
        Function<Users, List<Double>> function = user -> {
            Map<Integer, Long> genreCounts = user.getUsersTrackses().stream()
                    .map(userTrack -> userTrack.getTracks().getGenre())
                    .collect(Collectors.groupingBy(genre -> genre, Collectors.counting()));
            return genresDict.stream()
                    .map(genre -> Double.valueOf(genreCounts.getOrDefault(genre.getGenreId(), 0L)))
                    .collect(Collectors.toList());
        };
        return new MultiFeature<>(attributes, function);
    }

    public static MultiFeature<Double> artistTagsHistogram(List<Tags> tags) {
        Set<String> tagsSet = tags.stream()
                .map(Tags::getId)
                .collect(Collectors.toSet());
        List<Attribute> attributes = tags.stream()
                .map(tag -> new Attribute("artistTag:" + tag.getTagName()))
                .collect(Collectors.toList());
        Function<Users, List<Double>> function = user -> {
            Map<String, Long> tagsCount = user.getUsersTrackses().stream()
                    .map(userTrack -> userTrack.getTracks().getArtists().getArtistsTagses())
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

    public static MultiFeature<Double> trackTagsHistogram(List<Tags> tags) {
        Set<String> tagsSet = tags.stream()
                .map(Tags::getId)
                .collect(Collectors.toSet());
        List<Attribute> attributes = tags.stream()
                .map(tag -> new Attribute("trackTag:" + tag.getTagName()))
                .collect(Collectors.toList());
        Function<Users, List<Double>> function = user -> {
            Map<String, Long> tagsCount = user.getUsersTrackses().stream()
                    .map(userTrack -> userTrack.getTracks().getTracksTagses())
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

    public static MultiFeature<Double> artistHistogram(List<Artists> artists) {
        Set<String> artistsSet = artists.stream()
                .map(Artists::getId)
                .collect(Collectors.toSet());
        List<Attribute> attributes = artists.stream()
                .map(artist -> new Attribute("artist:" + artist.getArtistName()))
                .collect(Collectors.toList());
        Function<Users, List<Double>> function = user -> {
            Map<String, Long> artistsCount = user.getUsersTrackses().stream()
                    .map(userTrack -> userTrack.getTracks().getArtists().getId())
                    .filter(artistsSet::contains)
                    .collect(Collectors.groupingBy(id -> id, Collectors.counting()));
            return artists.stream()
                    .map(artist -> Double.valueOf(artistsCount.getOrDefault(artist.getId(), 0L)))
                    .collect(Collectors.toList());
        };
        return new MultiFeature<>(attributes, function);
    }

    private Features() {

    }
}
