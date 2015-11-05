package com.semkagtn.musicdatamining;

import com.semkagtn.musicdatamining.database.DatabaseReaderHelper;
import com.semkagtn.musicdatamining.learning.Feature;
import com.semkagtn.musicdatamining.learning.Features;
import com.semkagtn.musicdatamining.learning.MultiFeature;
import com.semkagtn.musicdatamining.utils.WekaUtils;
import weka.core.Instances;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by semkagtn on 03.11.15.
 */
public class GenerateTrainingSet {

    private static final int MINIMUM_TRACKS = 100;
    private static final int TOP_TAGS = 40;
    private static final int TOP_ARTISTS = 10;

    public static void main(String[] args) throws IOException {
        DatabaseReaderHelper database = new DatabaseReaderHelper();
        List<Users> users = database.selectAll(Users.class).stream()
                .filter(user -> user.getGender() != null)
                .filter(user -> user.getUsersTrackses().size() >= MINIMUM_TRACKS)
                .collect(Collectors.toList());
//        users = alignGender(users);

//        long birthdayMedian = birthdayMedian(users);
        List<GenresDict> genres = database.selectAll(GenresDict.class);
        List<Tags> artistsTags = database.topArtistsTags(TOP_TAGS);
        List<Tags> tracksTags = database.topTracksTags(TOP_TAGS);
        List<Artists> artists = database.topArtists(TOP_ARTISTS);

        List<MultiFeature<Double>> features = new ArrayList<>();
        features.add(Features.artistTagsHistogram(artistsTags));

        Feature<?> output = Features.gender();

        String trainingSetName = "trainingSetArtistsTags";

        Instances instances = WekaUtils.generateAgeTrainingSet(users, features, output, trainingSetName);
        WekaUtils.writeArffFile(instances);

        database.close();
    }

    private static List<Users> alignGender(List<Users> users) {
        List<Users> male = users.stream()
                .filter(user -> user.getGender().equals("m"))
                .collect(Collectors.toList());
        List<Users> female = users.stream()
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
