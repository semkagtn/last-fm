package com.semkagtn.lastfm.learning;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by semkagtn on 2/28/15.
 */
public class Features {

    private List<NominalFeature> nominalFeatures;
    private List<NumericFeature> numericFeatures;

    public Features() {
        this.nominalFeatures = new ArrayList<>();
        this.numericFeatures = new ArrayList<>();
    }

    public void addNominalFeature(NominalFeature nominalFeature) {
        nominalFeatures.add(nominalFeature);
    }

    public void addNumericFeature(NumericFeature numericFeature) {
        numericFeatures.add(numericFeature);
    }

    public void addNominalFeatures(List<NominalFeature> nominalFeatures) {
        this.nominalFeatures.addAll(nominalFeatures);
    }

    public void addNumericFeatures(List<NumericFeature> numericFeatures) {
        this.numericFeatures.addAll(numericFeatures);
    }

    public List<NominalFeature> getNominalFeatures() {
        return nominalFeatures;
    }

    public List<NumericFeature> getNumericFeatures() {
        return numericFeatures;
    }

    public int size() {
        return nominalFeatures.size() + numericFeatures.size();
    }
}
