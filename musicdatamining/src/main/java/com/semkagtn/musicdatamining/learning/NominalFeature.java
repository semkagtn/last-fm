package com.semkagtn.musicdatamining.learning;

import weka.core.Attribute;
import weka.core.FastVector;

import java.util.List;

/**
 * Created by semkagtn on 2/15/15.
 */
public abstract class NominalFeature extends Feature<String> {

    public NominalFeature(String name, List<String> values) {
        super(name);
        FastVector valuesVector = new FastVector(values.size());
        for (String value : values) {
            valuesVector.addElement(value);
        }
        this.attribute = new Attribute(name, valuesVector);
    }
}
