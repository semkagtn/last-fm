package com.semkagtn.lastfm;

import com.semkagtn.lastfm.learning.WekaTools;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.rules.DecisionTable;
import weka.core.Instances;

import java.io.File;
import java.util.Random;

/**
 * Created by semkagtn on 2/28/15.
 */
public class LearnModel {

    public static void main(String[] args) throws Exception {
        File file = new File("dataset-gender.arff");
        Instances instances = WekaTools.readArffFile(file);

        Classifier classifier = new DecisionTable();
        classifier.buildClassifier(instances);

        Evaluation evaluation = new Evaluation(instances);
        evaluation.crossValidateModel(classifier, instances, 10, new Random());

        System.out.println(evaluation.toSummaryString());
    }
}
