package com.semkagtn.musicdatamining;

import com.semkagtn.musicdatamining.utils.WekaUtils;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;

import java.io.File;
import java.util.*;

/**
 * Created by semkagtn on 19.11.15.
 */
public class CompressFeatures {

    private static int newIndex(long k) {
        final int threshold = 10;
        if (k < threshold) {
            return (int) k;
        }
        return (int) Math.log(Math.exp(threshold) + k * k * k * k * 5);
    }

    public static void main(String[] args) throws Exception {
        Instances instances = WekaUtils.readFile(new File("huge-igain.arff"), new ArffLoader());
        InfoGainAttributeEval eval = new InfoGainAttributeEval();
        eval.buildEvaluator(instances);

        List<Set<Integer>> sets = new ArrayList<>();
        for (int i = 0; i < instances.numAttributes() - 1; i++) {
            int k = newIndex(i);
            if (sets.size() - 1 < k) {
                sets.add(new LinkedHashSet<>());
            }
            sets.get(sets.size() - 1).add(i);
        }
        sets.get(sets.size() - 2).addAll(sets.get(sets.size() - 1));
        sets = sets.subList(0, sets.size() - 1);
        System.out.println("Total size: " + sets.size());
        System.out.println(sets.stream()
                .map(Set::size)
                .map(Object::toString)
                .reduce((x, y) -> x + "\n" + y).orElse(""));
        System.exit(0);

        FastVector attributes = new FastVector(sets.size() + 1);
        for (Set<Integer> set : sets) {
            String name = set.stream()
                    .map(index -> instances.attribute(index).name())
                    .reduce((x, y) -> x + ";" + y).orElse("");
            Attribute attribute = new Attribute(name);
            attributes.addElement(attribute);
        }
        attributes.addElement(instances.classAttribute());
        Instances newInstances = new Instances("new", attributes, instances.numInstances());
        newInstances.setClassIndex(newInstances.numAttributes() - 1);
        for (int i = 0; i < instances.numInstances(); i++) {
            Instance instance = instances.instance(i);
            Instance newInstance = new Instance(newInstances.numAttributes());
            for (int j = 0; j < newInstance.numAttributes() - 1; j++) {
                double value = sets.get(j).stream()
                        .map(instance::value)
                        .reduce(0.0, (x, y) -> x + y);
                newInstance.setValue(j, value);
            }
            newInstance.setValue(newInstances.classAttribute(), instance.classValue());
            newInstances.add(newInstance);
        }
        WekaUtils.writeFile(newInstances, new ArffSaver(), "arff");
    }
}
