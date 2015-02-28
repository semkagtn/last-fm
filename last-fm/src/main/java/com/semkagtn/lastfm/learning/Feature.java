package com.semkagtn.lastfm.learning;

import com.semkagtn.lastfm.Users;
import weka.core.Attribute;

/**
 * Created by semkagtn on 2/15/15.
 */
public abstract class Feature<T> {

    protected Attribute attribute;
    private String name;

    protected Feature(String name) {
        this.name = name;
    }

    public final String getName() {
        return name;
    }

    public final Attribute getAttribute() {
        return attribute;
    }

    public abstract T calculate(Users user);
}
