package com.semkagtn.lastfm;

import com.semkagtn.lastfm.database.Database;

/**
 * Created by semkagtn on 3/6/15.
 */
public class Test {

    public static void main(String[] args) {
        Database.open();
        Users users = new Users(2, 1, "f", "RU", 0);
        Database.close();
    }
}
