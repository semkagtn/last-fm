package com.semkagtn.musicdatamining.learning;

import com.semkagtn.musicdatamining.Users;
import weka.core.Attribute;

import java.util.function.Function;

/**
 * Created by semkagtn on 05.11.15.
 */
public class NumericFeature<X> extends Feature<X, Double> {

    public NumericFeature(Attribute attribute, Function<X, Double> function) {
        super(attribute, function);
    }

    @Override
    public boolean isNominal() {
        return false;
    }
}
