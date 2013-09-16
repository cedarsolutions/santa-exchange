/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 *              C E D A R
 *          S O L U T I O N S       "Software done right."
 *           S O F T W A R E
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2013 Kenneth J. Pronovici.
 * All rights reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the Apache License, Version 2.0.
 * See LICENSE for more information about the licensing terms.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Author   : Kenneth J. Pronovici <pronovic@ieee.org>
 * Language : Java 6
 * Project  : Secret Santa Exchange
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.cedarsolutions.santa.shared.domain.user;

import java.util.Date;

import javax.jdo.annotations.Index;
import javax.persistence.Id;

import com.cedarsolutions.shared.domain.OpenIdProvider;
import com.cedarsolutions.shared.domain.TranslatableDomainObject;
import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

/**
 * User that has registered with the application.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class RegisteredUser extends TranslatableDomainObject {

    /** Serialization version number, which can be important to the GAE back-end. */
    private static final long serialVersionUID = 2L;

    @Id @Index private String userId;
    @Index private String userName;
    @Index private Date registrationDate;
    private String authenticationDomain;
    @Index private OpenIdProvider openIdProvider;
    private String federatedIdentity;
    private String emailAddress;
    @Index private int logins;
    @Index private Date lastLogin;
    @Index private boolean admin;
    @Index private boolean locked;

    /** Compare this object to another object. */
    @Override
    public boolean equals(Object obj) {
        RegisteredUser other = (RegisteredUser) obj;
        return new EqualsBuilder()
                    .append(this.userId, other.userId)
                    .append(this.userName, other.userName)
                    .append(this.registrationDate, other.registrationDate)
                    .append(this.authenticationDomain, other.authenticationDomain)
                    .append(this.openIdProvider, other.openIdProvider)
                    .append(this.federatedIdentity, other.federatedIdentity)
                    .append(this.emailAddress, other.emailAddress)
                    .append(this.logins, other.logins)
                    .append(this.lastLogin, other.lastLogin)
                    .append(this.admin, other.admin)
                    .append(this.locked, other.locked)
                    .isEquals();
    }

    /** Generate a hash code for this object. */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                    .append(this.userId)
                    .append(this.userName)
                    .append(this.registrationDate)
                    .append(this.authenticationDomain)
                    .append(this.openIdProvider)
                    .append(this.federatedIdentity)
                    .append(this.emailAddress)
                    .append(this.logins)
                    .append(this.lastLogin)
                    .append(this.admin)
                    .append(this.locked)
                    .toHashCode();
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getRegistrationDate() {
        return this.registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getAuthenticationDomain() {
        return this.authenticationDomain;
    }

    public void setAuthenticationDomain(String authenticationDomain) {
        this.authenticationDomain = authenticationDomain;
    }

    public OpenIdProvider getOpenIdProvider() {
        return this.openIdProvider;
    }

    public void setOpenIdProvider(OpenIdProvider openIdProvider) {
        this.openIdProvider = openIdProvider;
    }

    public String getFederatedIdentity() {
        return this.federatedIdentity;
    }

    public void setFederatedIdentity(String federatedIdentity) {
        this.federatedIdentity = federatedIdentity;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public int getLogins() {
        return this.logins;
    }

    public void setLogins(int logins) {
        this.logins = logins;
    }

    public Date getLastLogin() {
        return this.lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public boolean isAdmin() {
        return this.admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isLocked() {
        return this.locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

}
