package com.digimax.geo.services.domain;

import com.digimax.geo.entities.Visit;
import com.digimax.geo.structural.DomainObject;

/**
 * Created by jonwilliams on 2014-05-16.
 */
public interface DomainObjectService {
    void touch(DomainObject domainObject, Visit visit);
}
