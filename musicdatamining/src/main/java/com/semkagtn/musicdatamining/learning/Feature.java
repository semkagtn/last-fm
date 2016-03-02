package com.semkagtn.musicdatamining.learning;

import weka.core.Attribute;

import java.util.function.Function;

/**
 * Created by semkagtn on 03.11.15.
 */
public abstract class Feature<X, T> {

    private Attribute attribute;
    private Function<X, T> function;

    public Feature(Attribute attribute, Function<X, T> function) {
        this.attribute = attribute;
        this.function = function;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public T calculate(X users) {
        return function.apply(users);
    }

    public boolean isNumeric() {
        return !isNominal();
    }

    public abstract boolean isNominal();
}
