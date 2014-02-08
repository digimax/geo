package com.digimax.geo.services.app;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.GeoIp2Provider;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by jon on 1/22/2014.
 */
public interface GeoDatabaseReader extends GeoIp2Provider, Closeable {

}
