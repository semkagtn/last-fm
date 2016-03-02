package com.semkagtn.musicdatamining;

import com.semkagtn.musicdatamining.utils.WekaUtils;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.core.converters.CSVSaver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by semkagtn on 01.02.16.
 */
public class ConvertTfToTfIdf {

    public static void main(String[] args) throws IOException {
        Instances instances = WekaUtils.readFile(new File("gender-align-igain-l1-tf.csv"), new CSVLoader());

        List<Integer> documentsContainsTerm = new ArrayList<>();
        for (int termIndex = 0; termIndex < instances.numAttributes() - 1; termIndex++) {
            documentsContainsTerm.add(numDocumentsContainsTerm(instances, termIndex));
        }

        for (int i = 0; i < instances.numInstances(); i++) {
            Instance instance = instances.instance(i);
            convertToTfIdf(instance, documentsContainsTerm, instances.numInstances());
        }

        instances.setRelationName("gender-align-igain-l1-tf-idf");
        WekaUtils.writeFile(instances, new CSVSaver(), "csv");
    }

    private static int numDocumentsContainsTerm(Instances instances, int termIndex) {
        int result = 0;
        for (int i = 0; i < instances.numInstances(); i++) {
            Instance instance = instances.instance(i);
            double termTf = instance.value(termIndex);
            if (termTf > 0.0) {
                result++;
            }
        }
        return result;
    }

    private static void convertToTfIdf(Instance instance,
                                       List<Integer> documentsContainsTerm,
                                       int numDocuments) {
        for (int i = 0; i < instance.numAttributes() - 1; i++) {
            double tf = instance.value(i);
            double idf = Math.log(numDocuments / documentsContainsTerm.get(i));
            instance.setValue(i, tf * idf);
        }
    }
}
