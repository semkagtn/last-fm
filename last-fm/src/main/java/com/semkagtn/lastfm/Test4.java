package com.semkagtn.lastfm;

import java.io.*;
import java.util.stream.Collectors;

/**
 * Created by semkagtn on 01.09.15.
 */
public class Test4 {

    public static void main(String[] args) throws IOException {
        int lessThan20 = 0;
        int between20and50 = 0;
        int between50and200 = 0;
        int between200and500 = 0;
        int greaterThan500 = 0;

        BufferedReader reader = new BufferedReader(new FileReader("wall-stat-report-2.txt"));
        for (String line : reader.lines().collect(Collectors.toList())) {
            int value = Integer.valueOf(line);
            if (value < 20) {
                lessThan20++;
            } else if (value <= 50) {
                between20and50++;
            } else if (value <= 200) {
                between50and200++;
            } else if (value <= 500) {
                between200and500++;
            } else {
                greaterThan500++;
            }
        }
        reader.close();

        System.out.println("[0, 20): " + lessThan20);
        System.out.println("[20, 50]: " + between20and50);
        System.out.println("(50, 200]: " + between50and200);
        System.out.println("(200, 500]: " + between200and500);
        System.out.println("(500, +inf): " + greaterThan500);
    }
}
