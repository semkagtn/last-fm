package com.semkagtn.musicdatamining;

import com.semkagtn.musicdatamining.utils.WekaUtils;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.CSVSaver;

import java.io.File;
import java.io.IOException;

/**
 * Created by semkagtn on 25.11.15.
 */
public class ConvertArffToCsv {

    public static void main(String[] args) throws IOException {
        Instances instances = WekaUtils.readFile(new File("gender-align-igain.arff"), new ArffLoader());
        WekaUtils.writeFile(instances, new CSVSaver(), "csv");
    }
}
