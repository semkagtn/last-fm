package com.semkagtn.musicdatamining;

import com.semkagtn.musicdatamining.learning.WekaTools;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;

import java.io.File;
import java.util.Random;

/**
 * Created by semkagtn on 2/28/15.
 */
public class LearnModel {

    private static final int T = 5;
    private static final int Q = 5;

    public static void main(String[] args) throws Exception {
        File file = new File("gender-same-top-3000-artists.arff");
        Instances instances = WekaTools.readArffFile(file);

        Filter filter = new Normalize();
        filter.setInputFormat(instances);
        Filter.useFilter(instances, filter);

        Classifier classifier = new NaiveBayes();
        classifier.buildClassifier(instances);

        Evaluation evaluation = new Evaluation(instances);
        for (int i = 0; i < T; i++) {
            evaluation.crossValidateModel(classifier, instances, Q, new Random());
        }
        System.out.println(evaluation.toSummaryString());
    }
}
