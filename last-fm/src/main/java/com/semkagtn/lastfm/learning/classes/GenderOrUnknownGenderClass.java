package com.semkagtn.lastfm.learning.classes;

import com.semkagtn.lastfm.Users;
import com.semkagtn.lastfm.learning.NominalFeature;

import java.util.Arrays;

/**
 * Created by semkagtn on 2/28/15.
 */
public class GenderOrUnknownGenderClass extends NominalFeature {

    public GenderOrUnknownGenderClass() {
        super("gender", Arrays.asList("m", "f", "n"));
    }

    @Override
    public String calculate(Users user) {
        return user.getGender();
    }
}
