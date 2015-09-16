package com.semkagtn.musicdatamining;

import com.semkagtn.musicdatamining.database.Database;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by semkagtn on 2/17/15.
 */
public class CreateArff {

    private static final int TOP_ARTISTS = 3000;
    private static final int TOP_TRACKS = 5000;

    public static void main(String[] args) throws Exception {
        Database.open();
//        try {
//            List<Users> users = Database.select(Users.class, "gender <> 'n'");
//            users = sameCountOfMalesAndFemales(users);
//            DataSet dataSet = new DataSet("gender-same-top-3000-artists", users);
//            Features features = new Features();
//            features.addNumericFeatures(ArtistsHistogramFeatures.getFeatures(TOP_ARTISTS));
//            features.addNumericFeatures(TracksHistogramFeatures.getFeatures(TOP_TRACKS));
//            NominalFeature clazz = new GenderClass();
//            WekaTools.writeArffFile(dataSet, features, clazz);
//        } finally {
//            Database.close();
//        }
    }

    private static List<Users> sameCountOfMalesAndFemales(List<Users> users) {
        List<Users> males = users.stream()
                .filter(x -> x.getGender().equals("m"))
                .collect(Collectors.toList());
        List<Users> females = users.stream()
                .filter(x -> x.getGender().equals("f"))
                .collect(Collectors.toList());
        int minimumLength = Math.min(males.size(), females.size());
        males = males.stream().limit(minimumLength).collect(Collectors.toList());
        females = females.stream().limit(minimumLength).collect(Collectors.toList());
        List<Users> result = new ArrayList<>(males);
        result.addAll(females);
        return result;
    }
}
