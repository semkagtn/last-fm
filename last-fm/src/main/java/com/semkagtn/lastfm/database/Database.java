package com.semkagtn.lastfm.database;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

/**
 * Created by semkagtn on 11.03.15.
 */
public class Database {

    private static SessionFactory sessionFactory;
    private static Session session;

    public static void open() {
        Configuration configuration = new Configuration().configure();
        sessionFactory = configuration.buildSessionFactory();
        session = sessionFactory.openSession();
    }

    public static <T> List<T> select(Class<T> clazz, String condition) {
        Query query = session.createQuery("from " + clazz.getSimpleName() + " where " + condition);
        return (List<T>) query.list();
    }

    public static <T> List<T> select(Class<T> clazz) {
        return (List<T>) session.createCriteria(clazz).list();
    }

    public static void close() {
        session.close();
        sessionFactory.close();
    }

    private Database() {

    }
}
