package com.semkagtn.musicdatamining.database;

import com.semkagtn.musicdatamining.Artists;
import com.semkagtn.musicdatamining.GenresDict;
import com.semkagtn.musicdatamining.LastfmUsers;
import com.semkagtn.musicdatamining.Tags;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.type.IntegerType;

import java.util.List;

/**
 * Created by semkagtn on 03.11.15.
 */
public class DatabaseReaderHelper implements AutoCloseable {

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
        String query =
                "SELECT tag_id AS id, tag_name, COUNT(*) AS count " +
                "FROM artists_tags JOIN tags ON artists_tags.tag_id = tags.id " +
                "GROUP BY tags.id ORDER BY count DESC LIMIT " + top;
        SQLQuery sqlQuery = session.createSQLQuery(query)
                .addEntity(Tags.class);
        return sqlQuery.list();
    }

    @SuppressWarnings("unchecked")
    public List<Tags> topTracksTags(int top) {
        String query =
                "SELECT tag_id AS id, tag_name, COUNT(*) AS count " +
                "FROM tracks_tags JOIN tags ON tracks_tags.tag_id = tags.id " +
                "GROUP BY tags.id ORDER BY count DESC LIMIT " + top;
        SQLQuery sqlQuery = session.createSQLQuery(query)
                .addEntity(Tags.class);
        return sqlQuery.list();
    }

    @SuppressWarnings("unchecked")
    public List<Artists> topArtists(int top) {
        String query =
                "SELECT artists.id AS id, artist_name, COUNT(*) AS count " +
                "FROM artists JOIN tracks ON artists.id = tracks.artist_id " +
                "GROUP BY artists.id ORDER BY count DESC LIMIT " + top;
        SQLQuery sqlQuery = session.createSQLQuery(query)
                .addEntity(Artists.class);
        return sqlQuery.list();
    }

    @SuppressWarnings("unchecked")
    public List<GenresDict> topGenres(int top) {
        String query = "SELECT genre_id, genre_name, COUNT(*) AS count " +
                "FROM tracks JOIN genres_dict ON tracks.genre = genres_dict.genre_id " +
                "GROUP BY genre_id ORDER BY count DESC LIMIT " + top;
        SQLQuery sqlQuery = session.createSQLQuery(query)
                .addEntity(GenresDict.class);
        return sqlQuery.list();
    }

    @SuppressWarnings("unchecked")
    public List<Integer> lastFmTotalPlaycounts() {
        String query =
                "SELECT SUM(playcount) AS total_playcount " +
                "FROM lastfm_top_tracks " +
                "GROUP BY user_id";
        SQLQuery sqlQuery = session.createSQLQuery(query)
                .addScalar("total_playcount", IntegerType.INSTANCE);
        return sqlQuery.list();
    }

    public boolean dTracksUsersExists(String trackId) {
        String query =
                "SELECT COUNT(*) AS count " +
                "FROM d_tracks_users " +
                "WHERE track_id = '" + trackId + "'";
        SQLQuery sqlQuery = session.createSQLQuery(query)
                .addScalar("count", IntegerType.INSTANCE);
        Integer count = (Integer) sqlQuery.list().get(0);
        return count > 0;
    }

    @SuppressWarnings("unchecked")
    public List<LastfmUsers> getLastFmUsers(int limit) {
        String query =
                "SELECT u.id, u.gender, u.country, COUNT(*) AS c " +
                "FROM lastfm_top_tracks tt " +
                "JOIN lastfm_users u on tt.user_id = u.id " +
                "GROUP BY tt.user_id " +
                "HAVING c >= 40 " +
                "ORDER BY c ASC " +
                "LIMIT " + limit;
        SQLQuery sqlQuery = session.createSQLQuery(query)
                .addEntity(LastfmUsers.class);
        return (List<LastfmUsers>) sqlQuery.list();
    }
}
