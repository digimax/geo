package com.digimax.geo.services.app;

import com.digimax.geo.services.app.GeoDatabaseReader;
import com.digimax.geo.services.domain.LocationServiceImpl;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityIspOrgResponse;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.model.OmniResponse;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by jon on 1/22/2014.
 */
public class GeoDatabaseReaderImpl implements GeoDatabaseReader {
    private DatabaseReader geoDatabaseReader;

    public GeoDatabaseReaderImpl() {
        if (geoDatabaseReader==null) {
            getGeoDatabaseReader();
        }
    }
    private synchronized static DatabaseReader buildDatabaseReader() {
        DatabaseReader databaseReader = null;
        File database = new File(LocationServiceImpl.DB_FILE);
        try {
            databaseReader = new DatabaseReader.Builder(database).build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return databaseReader;
    }

    public DatabaseReader getGeoDatabaseReader() {
        if (geoDatabaseReader==null) {
            geoDatabaseReader = buildDatabaseReader();
        }
        return geoDatabaseReader;
    }



    public void close() throws IOException {
        getGeoDatabaseReader().close();
    }

    public CountryResponse country(InetAddress ipAddress) throws IOException, GeoIp2Exception {
        return getGeoDatabaseReader().country(ipAddress);
    }

    public CityResponse city(InetAddress ipAddress) throws IOException, GeoIp2Exception {
        return getGeoDatabaseReader().city(ipAddress);
    }

    public CityIspOrgResponse cityIspOrg(InetAddress ipAddress) throws IOException, GeoIp2Exception {
        return getGeoDatabaseReader().cityIspOrg(ipAddress);
    }

    public OmniResponse omni(InetAddress ipAddress) throws IOException, GeoIp2Exception {
        return getGeoDatabaseReader().omni(ipAddress);
    }
}
