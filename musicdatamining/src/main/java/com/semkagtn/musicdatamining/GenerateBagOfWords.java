package com.semkagtn.musicdatamining;

import com.semkagtn.musicdatamining.utils.WekaUtils;
import javafx.util.Pair;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by semkagtn on 26.02.16.
 */
public class GenerateBagOfWords {

    private static final String FILE_NAME = "gender-align.arff";
    private static final String RESULT_NAME = "result";

    public static void main(String[] args) throws IOException {
        System.out.println("Reading dataset file...");
        Instances instances = WekaUtils.readFile(new File(FILE_NAME), new ArffLoader());

        Map<Pair<Integer, Integer>, Integer> triples = new LinkedHashMap<>();

        System.out.println("Calculating triples...");
        for (int i = 0; i < instances.numInstances(); i++) {
            Instance instance = instances.instance(i);
            for (int j = 0; j < instance.numAttributes() - 1; j++) {
                int value = (int) instance.value(j);
                if (value != 0) {
                    triples.put(new Pair<>(i + 1, j + 1), value);
                }
            }
        }
        System.out.println(String.format("%d triples was calculated", triples.size()));

        System.out.println("Writing docword...");
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(String.format("docword.%s.txt", RESULT_NAME)))) {
            writer.write(String.valueOf(instances.numInstances())); writer.newLine();
            writer.write(String.valueOf(instances.numAttributes())); writer.newLine();
            writer.write(String.valueOf(triples.size())); writer.newLine();
            int i = 0;
            for (Map.Entry<Pair<Integer, Integer>, Integer> triple : triples.entrySet()) {
                int docId = triple.getKey().getKey();
                int wordId = triple.getKey().getValue();
                int count = triple.getValue();
                writer.write(String.format("%d %d %d\n", docId, wordId, count));
//                System.out.println(String.format("Wrote %d triples", ++i));
            }
        }

        System.out.println("Writing vocab...");
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(String.format("vocab.%s.txt", RESULT_NAME)))) {
            for (int i = 0; i < instances.numAttributes() - 1; i++) {
                Attribute attribute = instances.attribute(i);
                String name = attribute.name().replaceAll("_", "__").replaceAll("\\s", "_");
                writer.write(name); writer.newLine();
//                System.out.println(String.format("Wrote %d words of %d", i + 1, instances.numAttributes() - 1));
            }
        }

        System.out.println("Writing classes...");
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(String.format("%s.classes", RESULT_NAME)))) {
            writer.write("class_column\n");
            for (int i = 0; i < instances.numInstances(); i++) {
                Instance instance = instances.instance(i);
                String clazz = instance.stringValue(instance.classIndex());
                writer.write(clazz); writer.newLine();
//                System.out.println(String.format("Wrote %d classes of %d", i + 1, instances.numInstances()));
            }
        }
    }
}
