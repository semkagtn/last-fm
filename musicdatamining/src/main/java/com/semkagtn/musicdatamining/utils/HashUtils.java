package com.semkagtn.musicdatamining.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by semkagtn on 09.09.15.
 */
public class HashUtils {

    public static String md5(String string) {
        return DigestUtils.md5Hex(string);
    }

    private HashUtils() {

    }
}
