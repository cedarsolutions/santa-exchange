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
package com.cedarsolutions.santa.shared.domain;

import com.cedarsolutions.client.gwt.module.IClientSession;
import com.cedarsolutions.santa.shared.domain.user.RegisteredUser;
import com.cedarsolutions.shared.domain.FederatedUser;
import com.cedarsolutions.shared.domain.TranslatableDomainObject;
import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;


/**
 * Holds client session information.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ClientSession extends TranslatableDomainObject implements IClientSession {

    /** Serialization version number, which can be important to the GAE back-end. */
    private static final long serialVersionUID = 1L;

    /** Whether the session is initialized. */
    protected boolean initialized = false;

    /** The current user, if any. */
    protected FederatedUser currentUser;

    /** The registered user associated with the session, if any. */
    protected RegisteredUser registeredUser;

    /** The current session id, if any. */
    protected String sessionId;

    /** The logout URL to use, if any. */
    protected String logoutUrl;

    /** Clear the current session. */
    public void clear() {
        this.initialized = false;
        this.currentUser = null;
        this.registeredUser = null;
        this.logoutUrl = null;
        this.sessionId = null;
    }

    /** Refresh the session using the input session as the source. */
    public void refresh(ClientSession session) {
        this.initialized = true;
        this.currentUser = session.currentUser;
        this.registeredUser = session.registeredUser;
        this.logoutUrl = session.logoutUrl;
        this.sessionId = session.sessionId;
    }

    /** Initialize the session. */
    @Override
    public void initialize() {
        this.initialized = true;
    }

    /** Indicates whether the session has been initialized. */
    @Override
    public boolean isInitialized() {
        return this.initialized;
    }

    /** Indicates whether a user is currently logged in. */
    public boolean isLoggedIn() {
        return this.currentUser != null;
    }

    /** Indicates whether current user is an admin user. */
    public boolean isAdmin() {
        return this.currentUser == null ? false : this.currentUser.isAdmin();
    }

    /** Indicates whether current session is locked. */
    public boolean isLocked() {
        return this.registeredUser == null ? false : this.registeredUser.isLocked();
    }

    /** Returns the current user, possibly null. */
    public FederatedUser getCurrentUser() {
        return this.currentUser;
    }

    /** Returns the registered user associated with the session, possibly null. */
    public RegisteredUser getRegisteredUser() {
        return this.registeredUser;
    }

    /** Sets the current user. */
    public void setCurrentUser(FederatedUser currentUser) {
        this.currentUser = currentUser;
    }

    /** Sets the registered user. */
    public void setRegisteredUser(RegisteredUser registeredUser) {
        this.registeredUser = registeredUser;
    }

    /** Get the logout URL, possibly null. */
    public String getLogoutUrl() {
        return this.logoutUrl;
    }

    /** Set the logout URL. */
    public void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }

    /** Get the session id, possibly null. */
    public String getSessionId() {
        return this.sessionId;
    }

    /** Set the session id. */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /** Clear the firstLogin flag. */
    public void clearFirstLogin() {
        this.currentUser.setFirstLogin(false);
    }

    /** Compare this object to another object. */
    @Override
    public boolean equals(Object obj) {
        ClientSession other = (ClientSession) obj;
        return new EqualsBuilder()
                    .append(this.initialized, other.initialized)
                    .append(this.currentUser, other.currentUser)
                    .append(this.registeredUser, other.registeredUser)
                    .append(this.sessionId, other.sessionId)
                    .append(this.logoutUrl, other.logoutUrl)
                    .isEquals();
    }

    /** Generate a hash code for this object. */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                    .append(this.initialized)
                    .append(this.currentUser)
                    .append(this.registeredUser)
                    .append(this.sessionId)
                    .append(this.logoutUrl)
                    .toHashCode();
    }
}
