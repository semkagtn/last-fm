package com.semkagtn.musicdatamining;

import com.semkagtn.musicdatamining.utils.WekaUtils;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.attributeSelection.ReliefFAttributeEval;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;

import java.io.File;

/**
 * Created by semkagtn on 10.12.15.
 */
public class RankFeatures {

    private static final String INPUT_NAME = "gender-align";
    private static final String OUPUT_NAME = "gender-align-igain";

    public static void main(String[] args) throws Exception {
        System.out.println("Reading file...");
        Instances instances = WekaUtils.readFile(new File(INPUT_NAME + ".arff"), new ArffLoader());
        System.out.println("Reading file done.");

//        ReliefFAttributeEval relieff = new ReliefFAttributeEval();
//        relieff.setSampleSize(Math.round(instances.numAttributes()));
//        relieff.setNumNeighbours(10);
        InfoGainAttributeEval infoGain = new InfoGainAttributeEval();
        Ranker ranker = new Ranker();
        ranker.setNumToSelect(2411);
        AttributeSelection attributeSelection = new AttributeSelection();
        attributeSelection.setInputFormat(instances);
        attributeSelection.setSearch(ranker);
        attributeSelection.setEvaluator(infoGain);

        System.out.println("Ranking...");
        Instances newInstances = Filter.useFilter(instances, attributeSelection);
        System.out.println("Ranking done.");

        System.out.println("Writing new file...");
        newInstances.setRelationName(OUPUT_NAME);
        WekaUtils.writeFile(newInstances, new ArffSaver(), "arff");
        System.out.println("Writing new file done.");
    }
}
