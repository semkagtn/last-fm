package com.semkagtn.lastfm;

import java.io.*;

/**
 * Created by semkagtn on 01.09.15.
 */
public class Test2 {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("wall-stat.txt"));
        String report = reader.lines().sorted((x, y) -> {
            String[] splittled1 = x.split("/");
            int musicOwnerPosts1 = Integer.valueOf(splittled1[0]);
            int allPosts1 = Integer.valueOf(splittled1[1]);
            double percentage1 = 1.0 * musicOwnerPosts1 / allPosts1;

            String[] splittled2 = y.split("/");
            int musicOwnerPosts2 = Integer.valueOf(splittled2[0]);
            int allPosts2 = Integer.valueOf(splittled2[1]);
            double percentage2 = 1.0 * musicOwnerPosts2 / allPosts2;

            return -Double.compare(musicOwnerPosts1, musicOwnerPosts2);
        }).map(line -> {
            String[] splittled = line.split("/");
            int musicOwnerPosts = Integer.valueOf(splittled[0]);
            int allPosts = Integer.valueOf(splittled[1]);
            double percentage = 1.0 * musicOwnerPosts / allPosts * 100;
            return musicOwnerPosts + "/" + allPosts + " (" + percentage + "%)";
        }).reduce((x, y) -> x + "\n" + y).orElse("");
        reader.close();

        BufferedWriter writer = new BufferedWriter(new FileWriter("wall-stat-report.txt"));
        writer.write(report);
        writer.close();
    }
}
