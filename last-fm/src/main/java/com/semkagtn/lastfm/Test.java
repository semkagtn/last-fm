package com.semkagtn.lastfm;

import de.umass.lastfm.Artist;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by semkagtn on 2/16/15.
 */
public class Test {

    private static final String API_KEY = "ca07f2773420c59d127dddd445db202e";

    public static void main(String[] args) {
        Artist artist = Artist.getInfo("Arctic monkeys", API_KEY);
        List<String> tags = artist.getTags().stream().collect(Collectors.toList());
        System.out.println(tags);
    }
}
