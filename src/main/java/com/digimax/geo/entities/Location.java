package com.digimax.geo.entities;

import com.digimax.geo.structural.DomainObject;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.NonVisual;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;

/**
 * Created by jon on 1/22/2014.
 */
//@Entity
@XmlRootElement(name = "location")
@Inheritance(strategy = InheritanceType.JOINED)
public class Location extends DomainObject {

    @Property
    public String ip;

    @Property
    public String city;

    @Property
    public String state;

    @Property
    public String stateIsoCode;

    @Property
    public String country;

    @Property
    public String isoCode;

    @Property
    public String continent;

    @Property
    public String continentCode;

    @Property
    @NonVisual
    public String postCode;

    @Property
    public String isp;

    @Property
    public String timeZone;

    @Property
    @NonVisual
    public Integer cityGeoNameId;

    @Property
    @NonVisual
    public Integer cityMetroCode;

    @Property
    public Double latitude;

    @Property
    public Double longitude;
}
