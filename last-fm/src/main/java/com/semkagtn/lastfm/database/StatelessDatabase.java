package com.semkagtn.lastfm.database;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;

/**
 * Created by semkagtn on 2/16/15.
 */
public class StatelessDatabase {

    private static SessionFactory sessionFactory;
    private static StatelessSession session;

    public static void open() {
        Configuration configuration = new Configuration().configure();
        sessionFactory = configuration.buildSessionFactory();
        session = sessionFactory.openStatelessSession();
    }

    public static <T> T select(Class<T> clazz, String id) {
        Query query = session.createQuery("from " + clazz.getSimpleName() + " where id = :id");
        query.setString("id", id);
        List<T> list = query.list();
        return (list.size() > 0) ? list.get(0) : null;
    }

    public static <T> T select(Class<T> clazz, int id) {
        return select(clazz, String.valueOf(id));
    }

    public static <T> boolean insert(T object) {
        session.beginTransaction();
        try {
            session.insert(object);
        } catch (ConstraintViolationException e) {
            session.getTransaction().rollback();
            return false;
        }
        session.getTransaction().commit();
        return true;
    }

    public static void close() {
        session.close();
        sessionFactory.close();
    }

    private StatelessDatabase() {

    }
}
