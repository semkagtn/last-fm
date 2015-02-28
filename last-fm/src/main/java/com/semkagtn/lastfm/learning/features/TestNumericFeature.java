package com.semkagtn.lastfm.learning.features;

import com.semkagtn.lastfm.Users;
import com.semkagtn.lastfm.learning.NumericFeature;

/**
 * Created by semkagtn on 2/28/15.
 */
public class TestNumericFeature extends NumericFeature {

    public TestNumericFeature() {
        super("testNumeric");
    }

    @Override
    public Double calculate(Users user) {
        return 3.14;
    }
}
