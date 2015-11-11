package com.semkagtn.musicdatamining;

import com.semkagtn.musicdatamining.database.DatabaseReaderHelper;
import com.semkagtn.musicdatamining.learning.Feature;
import com.semkagtn.musicdatamining.learning.Features;
import com.semkagtn.musicdatamining.learning.MultiFeature;
import com.semkagtn.musicdatamining.utils.WekaUtils;
import weka.core.Instances;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by semkagtn on 03.11.15.
 */
public class GenerateTrainingSet {

    private static final int MINIMUM_TRACKS = 100;
    private static final int TOP_ARTIST_TAGS = 12;
    private static final int TOP_TRACK_TAGS = 12;
    private static final int TOP_ARTISTS = 3;
    private static final int TOP_GENRES = 5;

    public static void main(String[] args) throws IOException {
        DatabaseReaderHelper database = new DatabaseReaderHelper();
        List<Users> users = database.selectAll(Users.class).stream()
                .filter(user -> user.getGender() != null)
                .filter(user -> user.getUsersTrackses().size() >= MINIMUM_TRACKS)
                .collect(Collectors.toList());
//        users = alignGender(users);

//        long birthdayMedian = birthdayMedian(users);
        List<GenresDict> genres = database.topGenres(TOP_GENRES);
        genres = genres.subList(1, genres.size()); // Without "Other"
        List<Tags> artistsTags = database.topArtistsTags(TOP_ARTIST_TAGS);
        List<Tags> tracksTags = database.topTracksTags(TOP_TRACK_TAGS);
        List<Artists> artists = database.topArtists(TOP_ARTISTS);

        List<MultiFeature<Double>> features = new ArrayList<>();
//        features.add(Features.artistHistogram(artists));
        features.add(Features.artistTagsHistogram(artistsTags));
        features.add(Features.trackTagsHistogram(tracksTags));
        features.add(Features.genreHistogram(genres));

        Feature<?> output = Features.gender();

        String trainingSetName = "all-small";

        Instances instances = WekaUtils.generateAgeTrainingSet(users, features, output, trainingSetName);
        WekaUtils.writeArffFile(instances);

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

    private static long birthdayMedian(List<Users> users) {
        List<Long> birthdays = users.stream()
                .map(Users::getBirthday)
                .sorted()
                .collect(Collectors.toList());
        return birthdays.get(birthdays.size() / 2);
    }
}
