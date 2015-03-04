package com.semkagtn.lastfm;

import com.semkagtn.lastfm.database.Database;
import com.semkagtn.lastfm.learning.DataSet;
import com.semkagtn.lastfm.learning.Features;
import com.semkagtn.lastfm.learning.NominalFeature;
import com.semkagtn.lastfm.learning.WekaTools;
import com.semkagtn.lastfm.learning.classes.GenderClass;

import java.io.IOException;
import java.util.List;

/**
 * Created by semkagtn on 2/17/15.
 */
public class CreateArff {

    public static void main(String[] args) throws IOException {
        Database.open();

        List<Users> users = Database.select(Users.class, "gender <> 'n' and playcount > 0");

        DataSet dataSet = new DataSet("dataset-gender", users);

        Features features = new Features();

        NominalFeature clazz = new GenderClass();

        WekaTools.writeArffFile(dataSet, features, clazz);

        Database.close();
    }

}
