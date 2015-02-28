package com.semkagtn.lastfm.learning;

import com.semkagtn.lastfm.Users;

import java.util.List;

/**
 * Created by semkagtn on 2/28/15.
 */
public class DataSet {

    private String name;
    private List<Users> users;

    public DataSet(String name, List<Users> users) {
        this.name = name;
        this.users = users;
    }

    public String getName() {
        return name;
    }

    public List<Users> getUsers() {
        return users;
    }
}
