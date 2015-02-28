package com.semkagtn.lastfm.learning.features;

import com.semkagtn.lastfm.Users;
import com.semkagtn.lastfm.learning.NominalFeature;

import java.util.Arrays;

/**
 * Created by semkagtn on 2/28/15.
 */
public class TestNominalFeature extends NominalFeature {

    public TestNominalFeature() {
        super("testNominal", Arrays.asList("someValue"));
    }

    @Override
    public String calculate(Users user) {
        return "someValue";
    }
}
