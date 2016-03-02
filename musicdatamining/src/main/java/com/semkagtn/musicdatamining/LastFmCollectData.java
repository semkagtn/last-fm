package com.semkagtn.musicdatamining;

import com.semkagtn.musicdatamining.lastfmapi.LastFmApiKeys;
import com.semkagtn.musicdatamining.lastfmapi.LastFmDataCollector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by semkagtn on 02.02.16.
 */
public class LastFmCollectData {

    private static final int THREADS = 20;

    private static List<String> initialUsers = Arrays.asList(
//            "floop1k",
//            "LuizFalk",
//            "stoicmage0",
//            "samojovana18",
//            "ShelbyPWND",
//            "gabiuspears",
//            "Lorelei_94",
//            "currison",
//            "sammy1971",
//            "kida48",
//            "Night-Mares",
//            "FCA19795",
//            "Jonderenn",
//            "LikeDuff",
//            "RussellChap",
//            "AllanDemigod",
//            "Red_October2",
//            "asumje",
//            "Bratizer",
//            "maaaree"

//            "Secretmode",
//            "Alv4r",
//            "SrtoLuan",
//            "kinguii",
//            "jlawfucked",
//            "unfack",
//            "Rodhina",
//            "illchillpill",
//            "MauroHenriq",
//            "liafrr",
//            "GRANRAlNHA",
//            "paolaaguilera",
//            "fantastickkay",
//            "Looppez",
//            "currison",
//            "sinetutku",
//            "InSearchOfTruth",
//            "Angel_Blue-",
//            "iEsenbach",
//            "GianLngyyy"

//            "vitriaz",
//            "neodude991",
//            "mr_gagakhan",
//            "InSearchOfTruth",
//            "WitchfynderBlue",
//            "camilalessa",
//            "LadyMary",
//            "Unwated",
//            "J-I-N-X",
//            "duda_a",
//            "RayTitoneli",
//            "JustinSpearsMe",
//            "Ruseis",
//            "robertoferrer",
//            "Luiz_feijo",
//            "EduardoJunior14",
//            "Irwinx",
//            "MayumiMay_",
//            "eliarlan-d",
//            "junioredighieri"

            "andressonz",
            "luandat91",
            "Szalwia246",
            "yasminkalliane",
            "MarcosHoltlin4",
            "AlekSaKauliTz",
            "currison",
            "Kolera98",
            "nage",
            "Francisco__",
            "LEOsigningOnn",
            "mayarahmanoel",
            "towardsecstasy",
            "IcePrinceXVI",
            "R_Victor",
            "Manic-Insanity",
            "ReedToGo",
            "Dnlphnv",
            "izabellealmeida",
            "jessyrocker"
    );

    public static void main(String[] args) throws InterruptedException {
        long startTimeMillis = System.currentTimeMillis();
        assert initialUsers.size() >= THREADS;

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < THREADS; i++) {
            String key = LastFmApiKeys.get(i);
            String initialUser = initialUsers.get(i);

            Thread thread = new Thread(() -> {
                LastFmDataCollector dataCollector = new LastFmDataCollector(key, initialUser);
                dataCollector.collect();
            });
            threads.add(thread);
        }
        threads.forEach(Thread::start);
        for (Thread thread : threads) {
            thread.join();
        }

        long executionSeconds = (System.currentTimeMillis() - startTimeMillis) / 1000;
        System.out.println(String.format("Execution time %d seconds", executionSeconds));
    }
}
