package com.digimax.geo.services.domain;

import com.digimax.geo.entities.Location;
import com.digimax.geo.services.app.GeoDatabaseReader;
import com.digimax.geo.structural.IpAddressValidator;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;
import org.xbill.DNS.*;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by jon on 1/22/2014.
 */
public class LocationServiceImpl implements LocationService {

    @Inject
    private Logger logger;

    @Inject
    private GeoDatabaseReader geoDatabaseReader;

    //System property com.digimax.geo.db.folder
    private static String DB_ROOT = System.getProperty("com.digimax.geo.db.folder")!=null?
            System.getProperty("com.digimax.geo.db.folder"): "/dig/wrk/digimax/geoip/";
    public static final String DB_FILE = DB_ROOT+"GeoLite2-City.mmdb";


    public Location getLocation(String ipAddress) {
        try {
            if (!new IpAddressValidator().validate(ipAddress)) {
                return null;
            }
            Location location = new Location();
            InetAddress inetAddress = InetAddress.getByName(ipAddress);
            CityResponse cityResponse = geoDatabaseReader.city(inetAddress);
            location.ip = ipAddress;
            
            location.isoCode = cityResponse.getCountry()==null? null: cityResponse.getCountry().getIsoCode();
            location.country = cityResponse.getCountry()==null? null: cityResponse.getCountry().getName();
            location.state = cityResponse.getCountry()==null || cityResponse.getMostSpecificSubdivision()==null
                    ? null: cityResponse.getMostSpecificSubdivision().getName();
            location.stateIsoCode = cityResponse.getCountry()==null || cityResponse.getMostSpecificSubdivision()==null
                    ? null: cityResponse.getMostSpecificSubdivision().getIsoCode();
            location.city = cityResponse.getCity()==null? null: cityResponse.getCity().getName();
            location.postCode = cityResponse.getPostal()==null? null: cityResponse.getPostal().getCode();
            location.continent = cityResponse.getContinent()==null? null: cityResponse.getContinent().getName();
            location.continentCode = cityResponse.getContinent()==null? null: cityResponse.getContinent().getCode();
            location.cityGeoNameId = cityResponse==null? null: cityResponse.getCity().getGeoNameId();
            location.cityMetroCode = cityResponse==null || cityResponse.getCity()==null? null: cityResponse.getLocation().getMetroCode();
            location.latitude = cityResponse==null || cityResponse.getLocation()==null? null: cityResponse.getLocation().getLatitude();
            location.longitude = cityResponse==null || cityResponse.getLocation()==null? null: cityResponse.getLocation().getLongitude();
            location.timeZone = cityResponse==null || cityResponse.getLocation()==null? null: cityResponse.getLocation().getTimeZone();
            logger.debug("MaxMind queries remaining::{}", cityResponse.getMaxMind().getQueriesRemaining());
            String isp = null;
            try {
                isp = getIsp(ipAddress);
            } catch (IOException e) {
                e.printStackTrace();
            }
            location.isp = isp;
            return location;
        } catch (UnknownHostException e) {
            logger.debug("ERROR", e);
        } catch (GeoIp2Exception e) {
            logger.debug("ERROR", e);
        } catch (IOException e) {
            logger.debug("ERROR", e);
        }
        return null;
    }

    private String getIsp(String ipAddress) throws IOException {
        String location = reverseDnsLookup(ipAddress);
        location = (location!=null && location.contains("."))
                ? location.substring(location.indexOf('.'))
                : "";
        return location;
    }

    private String reverseDnsLookup(String hostIpAddress) throws IOException {
        if (hostIpAddress==null || hostIpAddress.trim().length()<1) {
            return null;
        }
        logger.debug("Starting Reverse DNS Lookup for: {}", hostIpAddress);
        Resolver res = new ExtendedResolver();

        Name name = ReverseMap.fromAddress(hostIpAddress);
        int type = Type.PTR;
        int dclass = DClass.IN;
        Record rec = Record.newRecord(name, type, dclass);
        Message query = Message.newQuery(rec);
        Message response = res.send(query);

        Record[] answers = response.getSectionArray(Section.ANSWER);
        if (answers.length == 0)
            return null;
        else
            return answers[0].rdataToString();
    }

}
