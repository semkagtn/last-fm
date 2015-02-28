package com.semkagtn.lastfm.learning;

import com.semkagtn.lastfm.Users;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by semkagtn on 2/28/15.
 */
public class WekaTools {

    public static Instances readArffFile(File file) throws IOException {
        ArffLoader loader = new ArffLoader();
        loader.setFile(file);
        Instances instances = loader.getDataSet();
        instances.setClassIndex(instances.numAttributes() - 1);
        return instances;
    }

    public static void writeArffFile(Instances instances, File file) throws IOException {
        ArffSaver saver = new ArffSaver();
        saver.setInstances(instances);
        saver.setFile(file);
        saver.writeBatch();
    }

    public static Instances createInstances(DataSet dataSet, Features features, NominalFeature clazz) {
        List<NominalFeature> nominalFeatures = features.getNominalFeatures();
        List<NumericFeature> numericFeatures = features.getNumericFeatures();
        List<Users> users = dataSet.getUsers();

        FastVector attributesVector = new FastVector(features.size() + 1);
        for (NominalFeature feature : nominalFeatures) {
            attributesVector.addElement(feature.getAttribute());
        }
        for (NumericFeature feature : numericFeatures) {
            attributesVector.addElement(feature.getAttribute());
        }
        attributesVector.addElement(clazz.getAttribute());

        Instances instances = new Instances(dataSet.getName(), attributesVector, users.size());

        for (Users user : dataSet.getUsers()) {
            Instance instance = new Instance(features.size() + 1);
            for (NominalFeature feature : nominalFeatures) {
                instance.setValue(feature.getAttribute(), feature.calculate(user));
            }
            for (NumericFeature feature : numericFeatures) {
                instance.setValue(feature.getAttribute(), feature.calculate(user));
            }
            instance.setValue(clazz.getAttribute(), clazz.calculate(user));
            instances.add(instance);
        }
        return instances;
    }

    private WekaTools() {

    }
}
