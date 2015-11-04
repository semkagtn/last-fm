package com.semkagtn.musicdatamining.learning;

import com.semkagtn.musicdatamining.Users;
import weka.core.Attribute;

import java.util.List;
import java.util.function.Function;

/**
 * Created by semkagtn on 04.11.15.
 */
public class MultiFeature<T> {

    private List<Attribute> attributes;
    private Function<Users, List<T>> function;

    public MultiFeature(List<Attribute> attributes, Function<Users, List<T>> function) {
        this.attributes = attributes;
        this.function = function;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public List<T> calculate(Users user) {
        return function.apply(user);
    }
}
