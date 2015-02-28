package com.semkagtn.lastfm;

import com.semkagtn.lastfm.database.Database;
import com.semkagtn.lastfm.learning.DataSet;
import com.semkagtn.lastfm.learning.Features;
import com.semkagtn.lastfm.learning.WekaTools;
import com.semkagtn.lastfm.learning.NominalFeature;
import com.semkagtn.lastfm.learning.classes.GenderClass;
import com.semkagtn.lastfm.learning.features.TestNominalFeature;
import com.semkagtn.lastfm.learning.features.TestNumericFeature;
import weka.core.Instances;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by semkagtn on 2/17/15.
 */
public class CreateArff {

    public static void main(String[] args) throws IOException {
        Database.open();
        List<Users> users = Database.select(Users.class);
        Database.close();

        DataSet dataSet = new DataSet("simple data set", users);

        Features features = new Features();
        features.addFeature(new TestNumericFeature());
        features.addFeature(new TestNominalFeature());

        NominalFeature clazz = new GenderClass();

        Instances instances = WekaTools.createInstances(dataSet, features, clazz);
        File file = new File("dataset.arff");
        WekaTools.writeArffFile(instances, file);
    }
}
