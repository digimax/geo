package com.digimax.geo.structural;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by jon on 1/22/2014.
 */
public class RandomIdGenerator implements IdentifierGenerator {
    public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
        long sessionHash = Arrays.hashCode(session.toString().getBytes());
        Random randoms = new Random(System.currentTimeMillis()+sessionHash);
        return Math.abs(randoms.nextLong());
    }
}
