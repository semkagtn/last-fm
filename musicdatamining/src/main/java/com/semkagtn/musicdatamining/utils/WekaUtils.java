package com.semkagtn.musicdatamining.utils;

import com.semkagtn.musicdatamining.Users;
import com.semkagtn.musicdatamining.learning.Feature;
import com.semkagtn.musicdatamining.learning.MultiFeature;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
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

    public static Instances generateTrainingSet(List<Users> users,
                                 List<MultiFeature<Double>> multiFeatures,
                                 Feature<String> clazz,
                                 String name) {
        List<Attribute> allAttributes = multiFeatures.stream()
                .flatMap(multiFeature -> multiFeature.getAttributes().stream())
                .collect(Collectors.toList());
        FastVector attributesVector = new FastVector(allAttributes.size() + 1);
        allAttributes.forEach(attributesVector::addElement);
        attributesVector.addElement(clazz.getAttribute());

        Instances instances = new Instances(name, attributesVector, users.size());
        instances.setClassIndex(attributesVector.size() - 1);

        for (Users user : users) {
            Instance instance = new Instance(attributesVector.size());
            for (MultiFeature<Double> multiFeature : multiFeatures) {
                List<Attribute> attributes = multiFeature.getAttributes();
                List<Double> values = multiFeature.calculate(user);
                for (int i = 0; i < values.size(); i++) {
                    Attribute attribute = attributes.get(i);
                    Double value = values.get(i);
                    instance.setValue(attribute, value);
                }
            }
            instance.setValue(clazz.getAttribute(), clazz.calculate(user));
            instances.add(instance);
        }

        return instances;
    }

    public static void writeArffFile(Instances instances) throws IOException {
        Saver saver = new ArffSaver();
        saver.setInstances(instances);
        saver.setFile(new File(instances.relationName() + ".arff"));
        saver.writeBatch();
    }

    public static Instances readArffFile(File file) throws IOException {
        ArffLoader loader = new ArffLoader();
        loader.setFile(file);
        Instances instances = loader.getDataSet();
        instances.setClassIndex(instances.numAttributes() - 1);
        return instances;
    }

    private WekaUtils() {

    }
}
