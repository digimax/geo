package com.digimax.geo.services.domain;

import com.digimax.geo.entities.Visit;
import com.digimax.geo.structural.DomainObject;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;

import java.util.Date;

/**
 * Created by jonwilliams on 2014-05-16.
 */
public class DomainObjectServiceImpl implements DomainObjectService {

    private static final String SYSTEM_USER = "SYSTEM";

    @Inject
    private Logger logger;

    @Override
    public void touch(DomainObject domainObject, Visit visit) {
        if (domainObject.createDate==null) {
            domainObject.createDate = new Date();
        }
        if (domainObject.createUserName==null) {
            domainObject.createUserName = (visit!=null && visit.user!=null)? visit.user.userName : SYSTEM_USER;
        }
        if (domainObject.createUserIpAddress==null) {
            domainObject.createUserIpAddress = visit!=null? visit.getIpAddress()
                    : null;
        }
        domainObject.modifiedDate = new Date();
        domainObject.modifiedUserName = (visit!=null && visit.user!=null)? visit.user.userName : SYSTEM_USER;
    }
}