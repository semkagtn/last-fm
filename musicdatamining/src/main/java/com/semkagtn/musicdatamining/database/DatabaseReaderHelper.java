package com.semkagtn.musicdatamining.database;

import com.semkagtn.musicdatamining.Artists;
import com.semkagtn.musicdatamining.Tags;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

/**
 * Created by semkagtn on 03.11.15.
 */
public class DatabaseReaderHelper {

    private SessionFactory sessionFactory;
    private Session session;

    public DatabaseReaderHelper() {
        Configuration configuration = new Configuration().configure();
        sessionFactory = configuration.buildSessionFactory();
        session = sessionFactory.openSession();
    }

    public void close() {
        session.close();
        sessionFactory.close();
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> selectAll(Class<T> clazz) {
        return session.createCriteria(clazz).list();
    }

    @SuppressWarnings("unchecked")
    public List<Tags> topArtistsTags(int top) {
        String query = "SELECT tag_id AS id, tag_name, COUNT(*) AS count " +
                "FROM artists_tags JOIN tags ON artists_tags.tag_id = tags.id " +
                "GROUP BY tags.id ORDER BY count DESC LIMIT " + top;
        SQLQuery sqlQuery = session.createSQLQuery(query)
                .addEntity(Tags.class);
        return sqlQuery.list();
    }

    @SuppressWarnings("unchecked")
    public List<Tags> topTracksTags(int top) {
        String query = "SELECT tag_id AS id, tag_name, COUNT(*) AS count " +
                "FROM tracks_tags JOIN tags ON tracks_tags.tag_id = tags.id " +
                "GROUP BY tags.id ORDER BY count DESC LIMIT " + top;
        SQLQuery sqlQuery = session.createSQLQuery(query)
                .addEntity(Tags.class);
        return sqlQuery.list();
    }

    @SuppressWarnings("unchecked")
    public List<Artists> topArtists(int top) {
        String query = "SELECT artists.id AS id, artist_name, COUNT(*) AS count " +
                "FROM artists JOIN tracks ON artists.id = tracks.artist_id " +
                "GROUP BY artists.id ORDER BY count DESC LIMIT " + top;
        SQLQuery sqlQuery = session.createSQLQuery(query)
                .addEntity(Artists.class);
        return sqlQuery.list();
    }
}
