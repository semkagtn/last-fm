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
public class Main1 {

    private static final int MINIMUM_TRACKS = 100;
    private static final int TOP_TAGS = 20;

    public static void main(String[] args) throws IOException {
        DatabaseReaderHelper database = new DatabaseReaderHelper();

        List<Users> users = database.selectAll(Users.class).stream()
                .filter(user -> user.getGender() != null)
                .filter(user -> user.getUsersTrackses().size() >= MINIMUM_TRACKS)
                .collect(Collectors.toList());

        List<GenresDict> genres = database.selectAll(GenresDict.class);
        List<Tags> tags = database.topArtistsTags(TOP_TAGS);

        List<MultiFeature<Double>> features = new ArrayList<>();
        features.add(Features.genreHistogram(genres));
        features.add(Features.artistTagsHistogram(tags));

        Feature<String> clazz = Features.gender();

        String trainingSetName = "trainingSetArtistTagsAndGenres";

        Instances instances = WekaUtils.generateTrainingSet(users, features, clazz, trainingSetName);
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
}
