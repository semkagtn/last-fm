package com.semkagtn.lastfm.api;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by semkagtn on 3/7/15.
 */
public class ApiKeys {

    private static final List<String> API_KEYS = Arrays.asList(
            "b8b099d24df9c92133b729ac71ba0478",
            "ba83737f1d804bbb7499adaf6dfbf478",
            "db4d324783268f2d26a5593b292399cb",
            "c3f08d2af7231974467281edd2836cf3",
            "03269abd0820d42871cda4514e03325e",
            "ea1ea195273e2a918dbff8396bc7e2a4",
            "fc1e0c6d0eb6562b26e808fc92aba272",
            "9926b618f0ca51b2c85488c58e1afe4d",
            "d8f866dfa74f17b7ba9d9c6b418a0058",
            "6f81fde5cee816804e8e12c983dc7502",
            "fd06b14a43753befe6ec4c5a3edc7e8a",
            "3f600866a464d2b770eef80485cafab6",
            "ac8f614967ae1424a78d6d6e9d383b71",
            "7c62d6be8036d151bf3696da5e6645f7",
            "f436890e149816f13c07e8c235501651",
            "b941d8d79abd76fb2acea5ff9ed17b7f",
            "200b9e2a11ae6ebfcfbf5bcee2855a98",
            "39b18e8ff8ea40f2b3e99c0de4e1f5f9",
            "5669f663e5e96f9bd0e3309ff5c05cef",
            "64676c7280b3b8f7ed73cce4741baac7");

    private static Random random = new Random();

    public static String get(int index) {
        return API_KEYS.get(index);
    }

    public static String getRandom() {
        int index = random.nextInt(API_KEYS.size());
        return get(index);
    }

    private ApiKeys() {

    }
}
