package com.semkagtn.musicdatamining;

import com.semkagtn.musicdatamining.utils.WekaUtils;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.core.converters.CSVSaver;

import java.io.File;
import java.io.IOException;

/**
 * Created by semkagtn on 01.02.16.
 */
public class ConvertCountToTf {

    public static void main(String[] args) throws IOException {
        Instances instances = WekaUtils.readFile(new File("gender-align-igain-l1.csv"), new CSVLoader());
        for (int i = 0; i < instances.numInstances(); i++) {
            Instance instance = instances.instance(i);
            convertToTf(instance);
        }
        instances.setRelationName("gender-align-igain-l1-tf");
        WekaUtils.writeFile(instances, new CSVSaver(), "csv");
    }

    private static void convertToTf(Instance instance) {
        double totalCount = 0.0;
        for (int i = 0; i < instance.numAttributes() - 1; i++) {
            double count = instance.value(i);
            totalCount += count;
        }
        for (int i = 0; i < instance.numAttributes() - 1; i++) {
            instance.setValue(i, instance.value(i) / totalCount);
        }
    }
}
