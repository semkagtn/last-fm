package com.semkagtn.lastfm.database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Created by semkagtn on 2/14/15.
 */
public class Database {

    private static Session session;
    private static SessionFactory sessionFactory;

    static Session getSession() {
        return session;
    }

    public static void open() {
        Configuration configuration = new Configuration().configure();
//        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
//                .applySettings(configuration.getProperties())
//                .build();
        sessionFactory = configuration.buildSessionFactory(/*serviceRegistry*/);
        session = sessionFactory.openSession();
    }

    public static void close() {
        session.close();
        sessionFactory.close();
    }

    public static UsersDatabase users() {
        return UsersDatabase.getInstance();
    }

    public static RecentTracksDatabase recentTracks() {
        return RecentTracksDatabase.getInstance();
    }

    private Database() {

    }
}
