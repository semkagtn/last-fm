package com.semkagtn.musicdatamining;

import com.semkagtn.musicdatamining.database.DatabaseReaderHelper;
import com.semkagtn.musicdatamining.learning.Feature;
import com.semkagtn.musicdatamining.learning.Features;
import com.semkagtn.musicdatamining.learning.MultiFeature;
import com.semkagtn.musicdatamining.utils.DateTimeUtils;
import com.semkagtn.musicdatamining.utils.WekaUtils;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVSaver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by semkagtn on 03.11.15.
 */
public class GenerateTrainingSet {

    private static final int MINIMUM_TRACKS = 50;
    private static final int MAXIMUM_TRACKS = 100;
    private static final int TOP_ARTIST_TAGS = 10_000;
    private static final int TOP_TRACK_TAGS = 10_000;
    private static final int TOP_ARTISTS = 50;
//    private static final int TOP_GENRES = ;

    public static void main(String[] args) throws IOException {
        DatabaseReaderHelper database = new DatabaseReaderHelper();
        List<Users> users = database.selectAll(Users.class).stream()
                .filter(user -> user.getGender() != null)
//                .filter(user -> user.getBirthday() != null)
                .filter(user -> user.getUsersTrackses().size() >= MINIMUM_TRACKS)
                .collect(Collectors.toList());
        users = alignGender(users);

//        int birthdayMedian = birthdayMedianAge(users);
//        users = removeAgeMedianUsers(users, birthdayMedian);
//        users = removeBadUsers(users);

        List<GenresDict> genres = database.selectAll(GenresDict.class);
        List<Tags> artistsTags = database.topArtistsTags(TOP_ARTIST_TAGS);
        List<Tags> tracksTags = database.topTracksTags(TOP_TRACK_TAGS);
        List<Artists> artists = database.topArtists(TOP_ARTISTS);

        List<MultiFeature<Users, Double>> multiFeatures = new ArrayList<>();
        multiFeatures.add(Features.artistHistogram(artists, MAXIMUM_TRACKS));
        multiFeatures.add(Features.artistTagsHistogram(artistsTags, MAXIMUM_TRACKS));
        multiFeatures.add(Features.trackTagsHistogram(tracksTags, MAXIMUM_TRACKS));
        multiFeatures.add(Features.genreHistogram(genres, MAXIMUM_TRACKS));

        List<Feature<Users, ?>> features = new ArrayList<>();
//        features.add(Features.age());

        Feature<Users, ?> output = Features.gender();

        String trainingSetName = "optimized-big";

        Instances instances = WekaUtils.generateTrainingSet(
                users,
                multiFeatures,
                features,
                output,
                trainingSetName);
        WekaUtils.writeFile(instances, new CSVSaver(), "csv");

        database.close();
    }

    private static List<Users> alignGender(List<Users> users) {
        List<Users> shuffledUsers = new ArrayList<>(users);
        Collections.shuffle(shuffledUsers);
        List<Users> male = shuffledUsers.stream()
                .filter(user -> user.getGender().equals("m"))
                .collect(Collectors.toList());
        List<Users> female = shuffledUsers.stream()
                .filter(user -> user.getGender().equals("f"))
                .collect(Collectors.toList());

        int minGender = Math.min(male.size(), female.size());
        male = male.stream().limit(minGender).collect(Collectors.toList());
        female = female.stream().limit(minGender).collect(Collectors.toList());

        List<Users> result = new ArrayList<>();
        result.addAll(male);
        result.addAll(female);
        return result;
    }

    private static Long birthdayMedian(List<Users> users) {
        List<Long> birthdays = users.stream()
                .map(Users::getBirthday)
                .filter(x -> x != null)
                .sorted()
                .collect(Collectors.toList());
        return birthdays.get(birthdays.size() / 2);
    }

    private static Integer birthdayMedianAge(List<Users> users) {
        long median = birthdayMedian(users);
        return DateTimeUtils.unixTimeToAge(median);
    }

    private static List<Users> removeAgeMedianUsers(List<Users> users, int ageMedian) {
        return users.stream()
                .filter(user -> DateTimeUtils.unixTimeToAge(user.getBirthday()) != ageMedian)
                .collect(Collectors.toList());
    }

    private static List<Users> removeBadUsers(List<Users> users) {
        final int minAge = 16;
        final int maxAge = 80;
        return users.stream()
                .filter(user -> {
                    int age = DateTimeUtils.unixTimeToAge(user.getBirthday());
                    return age >= minAge && age <= maxAge;
                }).collect(Collectors.toList());
    }
}
