package com.digimax.geo.entities;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by jonwilliams on 2014-05-16.
 */
public class Visit {

    private transient HttpServletRequest request;

    public Visit(@Inject HttpServletRequest request) {
        this.request = request;
    }

    @Property
    public User user;

    @Property
    public String prefix;

    public String getIpAddress() {
        String ipAddress = "127.0.0.1";
        try {
            ipAddress = request.getRemoteAddr();
        } catch (Exception e) {
            //do nothing
        }
        return ipAddress;
    }

}
