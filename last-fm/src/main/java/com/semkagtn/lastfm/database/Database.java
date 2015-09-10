package com.semkagtn.lastfm.database;

import org.hibernate.NonUniqueObjectException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.ConstraintViolationException;

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

    public static <T> T select(Class<T> clazz, String id) {
        return session.get(clazz, id);
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> select(Class<T> clazz) {
        return (List<T>) session.createCriteria(clazz).list();
    }

    public static void close() {
        session.close();
        sessionFactory.close();
    }

    public static boolean insert(Object object) {
        session.beginTransaction();
        try {
            session.save(object);
            session.getTransaction().commit();
        } catch (ConstraintViolationException | NonUniqueObjectException e) {
            session.getTransaction().rollback();
            return false;
        }
        return true;
    }

    private Database() {

    }
}
