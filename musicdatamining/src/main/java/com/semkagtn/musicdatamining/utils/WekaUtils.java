package com.semkagtn.musicdatamining.utils;

import com.semkagtn.musicdatamining.Users;
import com.semkagtn.musicdatamining.learning.Feature;
import com.semkagtn.musicdatamining.learning.MultiFeature;
import com.semkagtn.musicdatamining.learning.NominalFeature;
import com.semkagtn.musicdatamining.learning.NumericFeature;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.core.converters.Loader;
import weka.core.converters.Saver;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by semkagtn on 03.11.15.
 */
public class WekaUtils {

    private static Logger logger = Logger.getLogger(WekaUtils.class.getName());

    public static <X, T> Instances generateTrainingSet(List<X> users,
                                                       List<MultiFeature<X, Double>> multiFeatures,
                                                       List<Feature<X, ?>> features,
                                                       Feature<X, ?> output,
                                                       String name) {
        List<Attribute> allAttributes = multiFeatures.stream()
                .flatMap(multiFeature -> multiFeature.getAttributes().stream())
                .collect(Collectors.toList());
        allAttributes.addAll(features.stream().map(Feature::getAttribute).collect(Collectors.toList()));

        FastVector attributesVector = new FastVector(allAttributes.size() + features.size() + 1);
        allAttributes.forEach(attributesVector::addElement);
        attributesVector.addElement(output.getAttribute());

        Instances instances = new Instances(name, attributesVector, users.size());
        instances.setClassIndex(attributesVector.size() - 1);

        for (int j = 0; j < users.size(); j++) {
            X user = users.get(j);
            Instance instance = new Instance(attributesVector.size());
            for (MultiFeature<X, Double> multiFeature : multiFeatures) {
                List<Attribute> attributes = multiFeature.getAttributes();
                List<Double> values = multiFeature.calculate(user);
                for (int i = 0; i < values.size(); i++) {
                    Attribute attribute = attributes.get(i);
                    Double value = values.get(i);
                    instance.setValue(attribute, value);
                }
            }
            for (Feature<X, ?> feature : features) {
                if (feature.isNominal()) {
                    NominalFeature<X> clazz = (NominalFeature<X>) feature;
                    String value = clazz.calculate(user);
                    if (value == null) {
                        instance.setMissing(clazz.getAttribute());
                    } else {
                        instance.setValue(clazz.getAttribute(), value);
                    }
                } else {
                    NumericFeature<X> numericOutput = (NumericFeature<X>) feature;
                    Double value = numericOutput.calculate(user);
                    if (value == null) {
                        instance.setMissing(numericOutput.getAttribute());
                    } else {
                        instance.setValue(numericOutput.getAttribute(), value);
                    }
                }
            }
            if (output.isNominal()) {
                NominalFeature<X> clazz = (NominalFeature<X>) output;
                instance.setValue(clazz.getAttribute(), clazz.calculate(user));
            } else {
                NumericFeature<X> numericOutput = (NumericFeature<X>) output;
                instance.setValue(numericOutput.getAttribute(), numericOutput.calculate(user));
            }
            instances.add(instance);
            logger.info(String.format("%d/%d users complete", j + 1, users.size()));
        }
        return instances;
    }

    public static void writeFile(Instances instances, Saver saver, String suffix) throws IOException {
        saver.setInstances(instances);
        saver.setFile(new File(instances.relationName() + "." + suffix));
        saver.writeBatch();
    }

    public static Instances readFile(File file, Loader loader) throws IOException {
        loader.setSource(file);
        Instances instances = loader.getDataSet();
        instances.setClassIndex(instances.numAttributes() - 1);
        return instances;
    }

    private WekaUtils() {

    }
}
