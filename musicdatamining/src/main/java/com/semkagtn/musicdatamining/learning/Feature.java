package com.semkagtn.musicdatamining.learning;

import com.semkagtn.musicdatamining.Users;
import weka.core.Attribute;

import java.util.function.Function;

/**
 * Created by semkagtn on 03.11.15.
 */
public abstract class Feature<T> {

    private Attribute attribute;
    private Function<Users, T> function;

    public Feature(Attribute attribute, Function<Users, T> function) {
        this.attribute = attribute;
        this.function = function;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public T calculate(Users users) {
        return function.apply(users);
    }

    public boolean isNumeric() {
        return !isNominal();
    }

    public abstract boolean isNominal();
}
