package com.digimax.geo.entities;

import com.digimax.geo.structural.DomainObject;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.Validate;

import javax.persistence.*;

/**
 * Created by jonwilliams on 2014-05-16.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends DomainObject {

    @Property
    @Validate("required, minlength=2")
    public String firstName;

    @Property
    @Validate("required, minlength=2")
    public String lastName;

    @Property
    @Validate("required, minlength=6, regexp")
    public String userName;

    public User() {
        super();
    }

    public String getFullName() {
        String fullName = null;
        if (firstName!=null) {
            fullName = String.format("%s %s", firstName, lastName);
        } else if (lastName!=null) {
            fullName = lastName;
        }
        return fullName;
    }

    public String toString() {
        return String.format("(User %s)\n\r %s %s", userName, firstName, lastName);
    }
}
