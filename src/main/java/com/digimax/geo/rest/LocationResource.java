package com.digimax.geo.rest;

import com.digimax.geo.entities.Location;
import com.digimax.geo.services.app.GeoDatabaseReader;
import com.digimax.geo.services.domain.LocationService;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * Created by jon on 1/22/2014.
 */
@Path("/location")
public class LocationResource {

    private LocationService locationService;

    public LocationResource(LocationService locationService) {
        this.locationService = locationService;
    }

    @GET
    @Path("{ip}")
    @Produces({"application/json"})
    public Location getLocation(@PathParam("ip") String ipAddress)
    {
        Location location = locationService.getLocation(ipAddress);
        if (location == null)
        {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return location;
    }
}
