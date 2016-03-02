package com.semkagtn.musicdatamining;

import com.semkagtn.musicdatamining.database.DatabaseReaderHelper;
import com.semkagtn.musicdatamining.utils.DateTimeUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by semkagtn on 25.02.16.
 */
public class SaveDistributionsToFile {

    public static void main(String[] args) throws IOException {
        try (DatabaseReaderHelper database = new DatabaseReaderHelper()) {
            List<DTracks> tracks = database.selectAll(DTracks.class)
                    .stream()
                    .filter(track -> track.getDTracksUserses().size() > 0)
                    .collect(Collectors.toList());
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("distributions.txt")));
            for (int i = 0; i < tracks.size(); i++) {
                System.out.println(String.format("%d/%d", (i+1), tracks.size()));
                DTracks track = tracks.get(i);
                writeToFile(writer, track);
            }
            writer.close();
        }
    }

    private static void writeToFile(BufferedWriter writer, DTracks track) throws IOException {
        List<Integer> ages = track.getDTracksUserses().stream()
                .map(DTracksUsers::getDUsers)
                .filter(user -> user.getBirthday() != null)
                .map(DUsers::getBirthday)
                .map(DateTimeUtils::unixTimeToAge)
                .collect(Collectors.toList());
        Map<Integer, Integer> ageCount = new HashMap<>();
        for (Integer age : ages) {
            if (!ageCount.containsKey(age)) {
                ageCount.put(age, 0);
            }
            ageCount.put(age, ageCount.get(age) + 1);
        }

        String result = ageCount.entrySet()
                .stream()
                .sorted((x, y) -> x.getKey().compareTo(y.getKey()))
                .map(entry -> String.format("%d,%d", entry.getKey(), entry.getValue()))
                .reduce((x, y) -> x + ";" + y).orElse("");

        writer.write(result);
        writer.newLine();
    }
}
