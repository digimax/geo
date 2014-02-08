package com.digimax.geo.services.domain;

import com.digimax.geo.entities.Location;

import java.net.UnknownHostException;

/**
 * Created by jon on 1/22/2014.
 */
public interface LocationService {

    Location getLocation(String ipAddress);
}
