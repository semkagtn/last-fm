package com.semkagtn.musicdatamining.learning.classes;

import com.semkagtn.musicdatamining.Users;
import com.semkagtn.musicdatamining.learning.NominalFeature;

import java.util.Arrays;

/**
 * Created by semkagtn on 2/28/15.
 */
public class GenderClass extends NominalFeature {

    public GenderClass() {
        super("gender", Arrays.asList("m", "f"));
    }

    @Override
    public String calculate(Users user) {
        return user.getGender();
    }
}
