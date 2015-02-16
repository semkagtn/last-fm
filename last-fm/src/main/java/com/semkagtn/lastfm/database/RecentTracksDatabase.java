package com.semkagtn.lastfm.database;

import com.semkagtn.lastfm.RecentTracks;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;

/**
 * Created by semkagtn on 2/14/15.
 */
public class RecentTracksDatabase {

    private static RecentTracksDatabase instance = new RecentTracksDatabase();

    static RecentTracksDatabase getInstance() {
        return instance;
    }

    public void insert(List<RecentTracks> recentTracks) {
        Session session = Database.getSession();
        session.beginTransaction();
        recentTracks.stream().forEach(session::save);
        session.getTransaction().commit();
    }

    public List<RecentTracks> select(int userId) {
        Session session = Database.getSession();
        Query query = session.createQuery("from recent_tracks where user_id = :user_id");
        query.setParameter("user_id", userId);
        List<RecentTracks> recentTracks = query.list();
        return recentTracks;
    }

    private RecentTracksDatabase() {

    }
}
