package com.semkagtn.lastfm.learning;

import weka.core.Attribute;

/**
 * Created by semkagtn on 2/15/15.
 */
public abstract class NumericFeature extends Feature<Double> {

    public NumericFeature(String name) {
        super(name);
        this.attribute = new Attribute(name);
    }
}
