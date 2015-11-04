package com.semkagtn.musicdatamining.learning;

import com.semkagtn.musicdatamining.*;
import weka.core.Attribute;
import weka.core.FastVector;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by semkagtn on 03.11.15.
 */
public class Features {

    public static Feature<String> gender() {
        FastVector vector = new FastVector(2);
        vector.addElement("m");
        vector.addElement("f");
        Attribute attribute = new Attribute("gender", vector);
        return new Feature<>(attribute, Users::getGender);
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
                .map(tag -> new Attribute("tag:" + tag.getTagName()))
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

    private Features() {

    }
}
