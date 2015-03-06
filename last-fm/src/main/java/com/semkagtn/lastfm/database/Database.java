package com.semkagtn.lastfm.database;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.Column;
import javax.persistence.Id;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by semkagtn on 2/16/15.
 */
public class Database {

    private static SessionFactory sessionFactory;
    private static Session session;

    public static void open() {
        Configuration configuration = new Configuration().configure();
        sessionFactory = configuration.buildSessionFactory();
        session = sessionFactory.openSession();
    }

    public static void close() {
        session.close();
        sessionFactory.close();
    }

    public static <T> List<T> select(Class<T> clazz) {
        List<T> result = session.createCriteria(clazz).list();
        return result;
    }

    public static <T> List<T> select(Class<T> clazz, String condition) {
        Query query = session.createQuery("from " + clazz.getSimpleName() + " where " + condition);
        List<T> result = query.list();
        return result;
    }

    public static <T> void insert(T object) {
        session.beginTransaction();
        session.save(object);
        session.getTransaction().commit();
    }

    public static <T> boolean insertIfNotExists(T object, String... uniqueFields) {
        session.beginTransaction();
        boolean isExists = objectExists(object, uniqueFields);
        if (!isExists) {
            session.save(object);
        }
        session.getTransaction().commit();
        return !isExists;
    }

    public static <T> boolean objectExists(T object, String... uniqueFields) {
        session.clear();
        sessionFactory.getCache().evictAllRegions();
        Set<String> uniqueFieldsSet = new TreeSet<>(Arrays.asList(uniqueFields));
        Map<String, Object> columnValue = new TreeMap<>();
        Class<?> clazz = object.getClass();
        String idName = null;
        String getIdMethodName = null;
        String setIdMethodName = null;
        String queryString = "from " + clazz.getSimpleName() + " where ";
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Column.class)) {
                Column column = method.getAnnotation(Column.class);
                String columnName = column.name();
                if (uniqueFieldsSet.contains(columnName)) {
                    try {
                        Object value = method.invoke(object);
                        columnValue.put(columnName, value);
                    } catch (Exception e) {
                        terminateWithError(e);
                    }
                }
                if (method.isAnnotationPresent(Id.class)) {
                    idName = columnName;
                    getIdMethodName = method.getName();
                    setIdMethodName = method.getName().replaceFirst("get", "set");
                }
            }
        }
        queryString += columnValue.entrySet()
                .stream()
                .map(x -> x.getKey() + " = '" + escapeSingleQuotes(x.getValue().toString()) + "'")
                .reduce((x, y) -> x + " and " + y)
                .get();
        Query query = session.createQuery(queryString);
        List<T> objects = query.list();
        if (objects.size() > 0) {
            if (!uniqueFieldsSet.contains(idName)) {
                try {
                    Method getIdMethod = clazz.getMethod(getIdMethodName);
                    Method setIdMethod = clazz.getMethod(setIdMethodName, Integer.class);
                    T savedObject = objects.get(0);
                    Integer id = (Integer) getIdMethod.invoke(savedObject);
                    setIdMethod.invoke(object, id);
                } catch (Exception e) {
                    terminateWithError(e);
                }
            }
            return true;
        }
        return false;
    }

    private static String escapeSingleQuotes(String tagName) {
        return tagName.replaceAll("'", "''");
    }

    private static void terminateWithError(Exception e) {
        close();
        System.err.println("Something goes wrong :(");
        e.printStackTrace();
        System.exit(1);
    }

    private Database() {

    }
}
