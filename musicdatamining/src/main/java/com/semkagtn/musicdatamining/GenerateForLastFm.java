package com.semkagtn.musicdatamining;

import com.semkagtn.musicdatamining.database.DatabaseReaderHelper;
import javafx.util.Pair;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by semkagtn on 26.02.16.
 */
public class GenerateForLastFm {

    public static void main(String[] args) throws IOException {
        final String resultFileName = "ress";
        try (DatabaseReaderHelper databaseReaderHelper = new DatabaseReaderHelper()) {

            System.out.println("Reading database...");
            List<LastfmArtists> artists = databaseReaderHelper.selectAll(LastfmArtists.class);
            Map<String, Integer> artistNo = new HashMap<>();
            for (int i = 0; i < artists.size(); i++) {
                artistNo.put(artists.get(i).getId(), i + 1);
            }
            List<LastfmUsers> users = databaseReaderHelper.getLastFmUsers(96807);

            System.out.println("Calculating triples...");
            Map<Pair<Integer, Integer>, Integer> triples = new LinkedHashMap<>();
            for (int i = 0; i < users.size(); i++) {
                Map<String, Integer> topArtists = new HashMap<>();
                LastfmUsers user = users.get(i);
                for (LastfmTopTracks topTrack : user.getLastfmTopTrackses()) {
                    String key = topTrack.getLastfmTracks().getLastfmArtists().getId();
                    Integer value = topTrack.getPlaycount();
                    Integer oldValue = topArtists.containsKey(key) ? topArtists.get(key) : 0;
                    topArtists.put(key, oldValue + value);
                }
                for (Map.Entry<String, Integer> entry : topArtists.entrySet()) {
                    triples.put(new Pair<>(i + 1, artistNo.get(entry.getKey())), entry.getValue());
                }
            }
            System.out.println(String.format("%d triples were calculated", triples.size()));

            System.out.println("Writing docword...");
            try (BufferedWriter writer = new BufferedWriter(
                    new FileWriter(String.format("docword.%s.txt", resultFileName)))) {
                writer.write(String.valueOf(users.size())); writer.newLine();
                writer.write(String.valueOf(artists.size())); writer.newLine();
                writer.write(String.valueOf(triples.size())); writer.newLine();

                for (Map.Entry<Pair<Integer, Integer>, Integer> triple : triples.entrySet()) {
                    int d = triple.getKey().getKey();
                    int w = triple.getKey().getValue();
                    int c = triple.getValue();
                    writer.write(String.format("%d %d %d\n", d, w, c));
                }
            }

            System.out.println("Writing vocab...");
            try (BufferedWriter writer = new BufferedWriter(
                    new FileWriter(String.format("vocab.%s.txt", resultFileName)))) {
                for (LastfmArtists artist : artists) {
                    String name = artist.getArtistName().replaceAll("_", "__").replaceAll("\\s", "_");
                    writer.write(name); writer.newLine();
                }
            }

            System.out.println("Writing classes...");
            try (BufferedWriter writer = new BufferedWriter(
                    new FileWriter(String.format("%s.classes", resultFileName)))) {
                writer.write("class_column"); writer.newLine();
                for (LastfmUsers user : users) {
                    writer.write(user.getGender()); writer.newLine();
                }
            }
        }
    }
}
