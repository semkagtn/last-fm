package com.semkagtn.musicdatamining;

import com.semkagtn.musicdatamining.utils.WekaUtils;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.functions.Logistic;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.meta.Bagging;
import weka.classifiers.meta.RandomCommittee;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.trees.BFTree;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.M5P;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.converters.ArffLoader;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;

import java.io.File;
import java.util.Random;

/**
 * Created by semkagtn on 04.11.15.
 */
public class LearnModel {

    private static final String TRAINING_SET_FILE_NAME = "optimized-big-igain-l1-pca.csv";
    private static final int Q = 1;
    private static final int K = 10;

    public static void main(String[] args) throws Exception {
        Instances trainingSet = WekaUtils.readFile(new File(TRAINING_SET_FILE_NAME), new CSVLoader());
        System.out.println(trainingSet.numInstances());
//        Filter normalize = new Normalize();
//        normalize.setInputFormat(trainingSet);
//        trainingSet = Filter.useFilter(trainingSet, normalize);

        LibSVM classifier = new LibSVM();
        classifier.setCacheSize(1024);
        classifier.setNormalize(true);
//        classifier.setShrinking(false);

        Evaluation evaluation = new Evaluation(trainingSet);
        for (int i = 0; i < Q; i++) {
            evaluation.crossValidateModel(classifier, trainingSet, K, new Random());
        }
        System.out.println(evaluation.toSummaryString());
    }
}
