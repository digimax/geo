package com.digimax.geo.pages;

import com.digimax.geo.services.app.GeoDatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityIspOrgResponse;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.model.OmniResponse;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRenderTemplate;
import org.apache.tapestry5.annotations.BeforeRenderBody;
import org.apache.tapestry5.annotations.BeforeRenderTemplate;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;


import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class About
{
    @Inject
    private Logger logger;

    @Inject
    private GeoDatabaseReader geoDatabaseReader;

    boolean brb(MarkupWriter writer) {
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getByName("192.31.187.75");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        try {
            CityResponse cityResponse = geoDatabaseReader.city(inetAddress);
            CityIspOrgResponse cityIspOrgResponse = geoDatabaseReader.cityIspOrg(inetAddress);
            CountryResponse countryResponse = geoDatabaseReader.country(inetAddress);
            OmniResponse omniResponse = geoDatabaseReader.omni(inetAddress);
            logger.debug("cityResponse::{}", cityResponse);
            logger.debug("cityIspOrgResponse::{}", cityIspOrgResponse);
            logger.debug("countryResponse::{}", countryResponse);
            logger.debug("omniResponse::{}", omniResponse);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeoIp2Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @BeginRender
    void beginRender(MarkupWriter writer) {
        logger.debug("Start Diagnostics");
        brb(writer);
        logger.debug("End Diagnostics");
    }

    @BeforeRenderTemplate
    boolean checkBeforeTemplate(MarkupWriter writer) {
        logger.debug("Start Diagnostics");
        logger.debug("End Diagnostics");
        return true;
    }

    @AfterRenderTemplate
    void checkAfterTemplate(MarkupWriter writer) {
        logger.debug("Start Diagnostics");
        logger.debug("End Diagnostics");
    }

}
