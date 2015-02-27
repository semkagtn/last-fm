package com.semkagtn.lastfm;

import com.semkagtn.lastfm.database.Database;
import com.semkagtn.lastfm.features.Feature;
import com.semkagtn.lastfm.features.NominalFeature;
import com.semkagtn.lastfm.features.NumericFeature;
import com.semkagtn.lastfm.utils.RequestWrapper;
import javafx.util.Pair;
import weka.classifiers.Classifier;
import weka.core.*;
import weka.core.converters.ArffSaver;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by semkagtn on 2/17/15.
 */
public class Test {

    private static final String API_KEY = "ca07f2773420c59d127dddd445db202e";

    public static void main(String[] args) throws RequestWrapper.RequestException, IOException {
        Database.open();

        List<Users> users = Database.select(Users.class);
        Pair<List<Users>, List<Users>> learningAndTestingSet = splitToLearnAndTesting(users);
        List<Users> learningSet = learningAndTestingSet.getKey();
        List<Users> testingSet = learningAndTestingSet.getValue();

        generateArffFile(learningSet, Arrays.asList(
                new NominalFeature("someFeature", Arrays.asList("someResult")) {
                    @Override
                    public String calculate(Users user) {
                        return "someResult";
                    }
                }
        ), Arrays.asList(
                new NumericFeature("someNumeric") {
                    @Override
                    public Double calculate(Users user) {
                        return 3.14;
                    }
                }
        ),
                new NominalFeature("gender", Arrays.asList("m", "f", "n")) {
            @Override
            public String calculate(Users user) {
                return user.getGender();
            }
        }, new File("result.arff"));

        Database.close();
    }

    private static void generateArffFile(Collection<Users> learningSet,
                                         Collection<NominalFeature> nominalFeatures,
                                         Collection<NumericFeature> numericFeatures,
                                         NominalFeature classFeature,
                                         File fileToSave) throws IOException {
        FastVector attributesVector = new FastVector(nominalFeatures.size() + numericFeatures.size() + 1);
        for (NominalFeature feature : nominalFeatures) {
            attributesVector.addElement(feature.getAttribute());
        }
        for (NumericFeature feature : numericFeatures) {
            attributesVector.addElement(feature.getAttribute());
        }
        attributesVector.addElement(classFeature.getAttribute());

        Instances learningInstances = new Instances("Rel", attributesVector, learningSet.size());
        learningInstances.setClassIndex(nominalFeatures.size() + numericFeatures.size());

        for (Users user : learningSet) {
            Instance instance = new Instance(nominalFeatures.size() + numericFeatures.size() + 1);
            for (NominalFeature feature : nominalFeatures) {
                instance.setValue(feature.getAttribute(), feature.calculate(user));
            }
            for (NumericFeature feature : numericFeatures) {
                instance.setValue(feature.getAttribute(), feature.calculate(user));
            }
            instance.setValue(classFeature.getAttribute(), classFeature.calculate(user));
            learningInstances.add(instance);
        }

        ArffSaver saver = new ArffSaver();
        saver.setInstances(learningInstances);
        saver.setFile(fileToSave);
        saver.writeBatch();
    }

    private static Pair<List<Users>, List<Users>> splitToLearnAndTesting(Collection<Users> users) {
        List<Users> usersList = new ArrayList<>(users);
        Collections.shuffle(usersList);
        List<Users> learningSet = usersList.subList(0, usersList.size() / 2);
        List<Users> testingSet = usersList.subList(usersList.size() / 2, usersList.size());
        return new Pair<>(learningSet, testingSet);
    }
}
