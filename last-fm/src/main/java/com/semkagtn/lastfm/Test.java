package com.semkagtn.lastfm;

import com.semkagtn.lastfm.database.Database;
import de.umass.lastfm.Caller;
import de.umass.lastfm.User;

import java.util.stream.Collectors;

/**
 * Created by semkagtn on 3/2/15.
 */
public class Test {

    public static void main(String[] args) throws Exception {
        Database.open();
        Users user = new Users();
        user.setUserId(1234);
        user.setCountry("RU");
        user.setGender("m");
        System.out.println(Database.insertIfNotExists(user, "user_id"));
        Database.close();
    }
}
