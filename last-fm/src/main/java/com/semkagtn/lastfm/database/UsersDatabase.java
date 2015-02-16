package com.semkagtn.lastfm.database;

import com.semkagtn.lastfm.Users;
import org.hibernate.Session;

import java.util.List;

/**
 * Created by semkagtn on 2/14/15.
 */
public class UsersDatabase {

    private static UsersDatabase instance = new UsersDatabase();

    static UsersDatabase getInstance() {
        return instance;
    }

    public void insert(Users user) {
        Session session = Database.getSession();
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
    }

    public List<Users> select() {
        Session session = Database.getSession();
        List<Users> users = session.createCriteria(Users.class).list();
        return users;
    }

    private UsersDatabase() {

    }
}
