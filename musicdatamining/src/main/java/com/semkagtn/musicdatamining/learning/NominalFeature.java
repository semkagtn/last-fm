package com.semkagtn.musicdatamining.learning;

import com.semkagtn.musicdatamining.Users;
import weka.core.Attribute;

import java.util.function.Function;

/**
 * Created by semkagtn on 05.11.15.
 */
public class NominalFeature extends Feature<String> {

    public NominalFeature(Attribute attribute, Function<Users, String> function) {
        super(attribute, function);
    }

    @Override
    public boolean isNominal() {
        return true;
    }
}
