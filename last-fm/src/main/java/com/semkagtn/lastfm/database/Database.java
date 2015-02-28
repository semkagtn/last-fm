package com.semkagtn.lastfm.database;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.Column;
import javax.persistence.Id;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by semkagtn on 2/16/15.
 */
public class Database {

    private static Session session;
    private static SessionFactory sessionFactory;

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

    public static <T> List<T> select(Class<T> clazz) {
        return session.createCriteria(clazz).list();
    }

    public static <T> void insert(T object) {
        session.beginTransaction();
        session.save(object);
        session.getTransaction().commit();
    }

    public static <T> boolean objectExists(T object) {
        Class<?> clazz = object.getClass();
        String queryBuilder = "from " + clazz.getSimpleName() + " where ";
        String getIdMethodName = null;
        String setIdMethodName = null;
        boolean uniqueValueExists = false;
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Column.class)) {
                if (method.isAnnotationPresent(Id.class)) {
                    getIdMethodName = method.getName();
                    setIdMethodName = method.getName().replaceFirst("get", "set");
                } else {
                    Column column = method.getAnnotation(Column.class);
                    if (column.unique()) {
                        uniqueValueExists = true;
                        String name = column.name();
                        Object value = null;
                        try {
                            value = method.invoke(object);
                        } catch (Exception e) {
                            terminateWithError(e);
                        }
                        queryBuilder += name + " = '" + escapeSingleQuotes(value.toString()) + "'";
                    }
                }
            }
        }
        if (!uniqueValueExists) {
            terminateWithError(new RuntimeException("Entity " + clazz + " doesn't have unique fields"));
        }
        Query query = session.createQuery(queryBuilder);
        List<T> savedObjects = query.list();
        if (savedObjects.size() > 0) {
            T savedObject = savedObjects.get(0);
            try {
                Method setIdMethod = clazz.getMethod(setIdMethodName, Integer.class);
                Method getIdMethod = clazz.getMethod(getIdMethodName);
                setIdMethod.invoke(object, getIdMethod.invoke(savedObject));
            } catch (Exception e) {
                terminateWithError(e);
            }
            return true;
        }
        return false;
    }

    private static void terminateWithError(Exception e) {
        close();
        System.err.println("Something goes wrong :(");
        e.printStackTrace();
        System.exit(1);
    }

    private static String escapeSingleQuotes(String tagName) {
        return tagName.replaceAll("'", "''");
    }

    private Database() {

    }
}
