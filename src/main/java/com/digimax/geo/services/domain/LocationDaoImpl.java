package com.digimax.geo.services.domain;

import com.digimax.geo.entities.Location;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.*;

/**
 * Created by jonwilliams on 2014-05-16.
 */
@SuppressWarnings("unchecked")
public class LocationDaoImpl implements LocationDao {

    @Inject
    private Session session;

    @Override
    public List<Location> getAll() {
        List<Location> identities = session.createCriteria(Location.class).list();
        return identities;
    }

    @Override
    public Location get(Long id) {
        return (Location) session.get(Location.class, id);
    }

    @Override
    public boolean exists(Long id) {
        Location location = (Location) session.load(Location.class, id);
        return (location!=null);
    }

    @Override
    public Location save(Location location) {
        session.save(location);
        return location;
    }

    @Override
    public void remove(Long id) {
        Location location = (Location) session.load(Location.class, id);
        session.delete(location);
    }

    @Override
    public void remove(Location location) {
        session.delete(location);
    }

    @Override
    public List<Location> getAllDistinct() {
        List<Location> all = getAll();
        Set<Location> uniqueSet = new HashSet<>(all);
        return new ArrayList<>(uniqueSet);
    }

    @Override
    public List<Location> findByNamedQuery(String queryName, Map<String, Object> queryParams) {
        Query q = session.getNamedQuery(queryName);

        String []params = new String[queryParams.size()];
        Object []values = new Object[queryParams.size()];
        int index = 0;
        for (String key : queryParams.keySet()) {
            params[index] = key;
            values[index++] = queryParams.get(key);
        }
        if (queryParams!=null && queryParams.size()>0) {
            Iterator<String> i = queryParams.keySet().iterator();
            for (int j=0; j<params.length; j++) {
                q.setParameter(params[j], values[j]);
            }
        }
        return (List<Location>)q.list();
    }
}
