package com.semkagtn.musicdatamining;

import com.semkagtn.musicdatamining.utils.WekaUtils;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

import java.io.File;

/**
 * Created by semkagtn on 11.12.15.
 */
public class ReplaceOutput {

    public static void main(String[] args) throws Exception {
        Instances instances = WekaUtils.readFile(new File("huge-igain-relieff.arff"), new ArffLoader());
        Instances newOutputs = WekaUtils.readFile(new File("age.arff"), new ArffLoader());

        Remove remove = new Remove();
        remove.setAttributeIndicesArray(new int[]{instances.numAttributes() - 1});
        remove.setInvertSelection(false);
        remove.setInputFormat(instances);
        instances = Filter.useFilter(instances, remove);

        instances = Instances.mergeInstances(instances, newOutputs);
        instances.setRelationName("huge-igain-relieff-age.arff");
        WekaUtils.writeFile(instances, new ArffSaver(), "arff");
        WekaUtils.writeFile(instances, new CSVSaver(), "csv");
    }
}
