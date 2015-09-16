package com.semkagtn.musicdatamining.utils;

import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by semkagtn on 17.09.15.
 */
public class ResourceUtils {

    private static ClassLoader classLoader = ResourceUtils.class.getClassLoader();

    public static List<String> getFileLines(String fileName) {
        try {
            String tokensString = IOUtils.toString(classLoader.getResourceAsStream(fileName), Consts.UTF_8);
            return Stream.of(tokensString.split("\n"))
                    .map(String::trim)
                    .filter(line -> !line.equals(""))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private ResourceUtils() {

    }
}
