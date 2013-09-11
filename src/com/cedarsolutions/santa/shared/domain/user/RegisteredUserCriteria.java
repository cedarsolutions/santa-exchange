/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 *              C E D A R
 *          S O L U T I O N S       "Software done right."
 *           S O F T W A R E
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2011-2013 Kenneth J. Pronovici.
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cedarsolutions.dao.domain.AbstractSearchCriteriaWithSort;
import com.cedarsolutions.shared.domain.OpenIdProvider;
import com.cedarsolutions.util.gwt.GwtStringUtils;
import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

/**
 * Registered user search criteria.
 *
 * <p>
 * The date criteria are applied inclusively.  If you do not provide one of
 * the values (or both), then the query will be open-ended in that direction.
 * </p>
 *
 * <p>
 * The other values are implemented like an IN clause in SQL.  A row meets the
 * user id criteria if the row's user id is in the provided list.  If the
 * provided list is empty, then that value will be ignored.  So, if you provide
 * an empty user id list, you'll get back rows with any user id.
 * </p>
 *
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class RegisteredUserCriteria extends AbstractSearchCriteriaWithSort<RegisteredUser, RegisteredUserSortColumn> {

    /** Serialization version number, which can be important to the GAE back-end. */
    private static final long serialVersionUID = 1L;

    private List<String> userIds;
    private List<String> userNames;
    private List<OpenIdProvider> openIdProviders;
    private Boolean admin;
    private Boolean locked;
    private Date startDate;
    private Date endDate;

    /** Default constructor. */
    public RegisteredUserCriteria() {
        super();
    }

    /** Get default sort column name. */
    @Override
    public RegisteredUserSortColumn getDefaultSortColumn() {
        return RegisteredUserSortColumn.USER_NAME;
    }

    /** Translate a column name to an enumeration, or null if the name is unknown. */
    @Override
    public RegisteredUserSortColumn getSortColumn(String columnName) {
        return GwtStringUtils.isEmpty(columnName) ? null : RegisteredUserSortColumn.valueOf(columnName);
    }

    /** Compare this object to another object. */
    @Override
    public boolean equals(Object obj) {
        RegisteredUserCriteria other = (RegisteredUserCriteria) obj;
        return new EqualsBuilder()
                    .appendSuper(super.equals(obj))
                    .append(this.userIds, other.userIds)
                    .append(this.userNames, other.userNames)
                    .append(this.openIdProviders, other.openIdProviders)
                    .append(this.admin, other.admin)
                    .append(this.locked, other.locked)
                    .append(this.startDate, other.startDate)
                    .append(this.endDate, other.endDate)
                    .isEquals();
    }

    /** Generate a hash code for this object. */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                    .appendSuper(super.hashCode())
                    .append(this.userIds)
                    .append(this.userNames)
                    .append(this.openIdProviders)
                    .append(this.admin)
                    .append(this.locked)
                    .append(this.startDate)
                    .append(this.endDate)
                    .toHashCode();
    }

    public List<String> getUserIds() {
        return this.userIds;
    }

    public void setUserIds(String ... userIds) {
        if (userIds == null) {
            this.userIds = null;
        } else {
            this.userIds = new ArrayList<String>();
            for (String userId : userIds) {
                this.userIds.add(userId);
            }
        }
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public List<String> getUserNames() {
        return this.userNames;
    }

    public void setUserNames(String ... userNames) {
        if (userNames == null) {
            this.userNames = null;
        } else {
            this.userNames = new ArrayList<String>();
            for (String userName : userNames) {
                this.userNames.add(userName);
            }
        }
    }

    public void setUserNames(List<String> userNames) {
        this.userNames = userNames;
    }

    public List<OpenIdProvider> getOpenIdProviders() {
        return this.openIdProviders;
    }

    public void setOpenIdProviders(OpenIdProvider ... openIdProviders) {
        if (openIdProviders == null) {
            this.openIdProviders = null;
        } else {
            this.openIdProviders = new ArrayList<OpenIdProvider>();
            for (OpenIdProvider openIdProvider : openIdProviders) {
                this.openIdProviders.add(openIdProvider);
            }
        }
    }

    public void setOpenIdProviders(List<OpenIdProvider> openIdProviders) {
        this.openIdProviders = openIdProviders;
    }

    public Boolean getAdmin() {
        return this.admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public Boolean getLocked() {
        return this.locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

}
