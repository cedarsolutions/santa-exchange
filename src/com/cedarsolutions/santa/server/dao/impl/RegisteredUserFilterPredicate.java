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
package com.cedarsolutions.santa.server.dao.impl;

import com.cedarsolutions.dao.gae.IFilterPredicate;
import com.cedarsolutions.santa.shared.domain.user.RegisteredUser;
import com.cedarsolutions.santa.shared.domain.user.RegisteredUserCriteria;
import com.cedarsolutions.util.DateUtils;

/**
 * Filter predicate based on registered user search criteria.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class RegisteredUserFilterPredicate implements IFilterPredicate<RegisteredUser> {

    /** Search criteria. */
    private RegisteredUserCriteria criteria;

    /** Create a filter predicate in terms of audit event search criteria. */
    public RegisteredUserFilterPredicate(RegisteredUserCriteria criteria) {
        this.criteria = criteria;
    }

    /** Evaluate the predicate for the passed-in value. */
    @Override
    public boolean evaluate(RegisteredUser value) {
        return meetsUserIdCriteria(criteria, value) &&
               meetsUserNameCriteria(criteria, value) &&
               meetsOpenIdProviderCriteria(criteria, value) &&
               meetsAdminCriteria(criteria, value) &&
               meetsLockedCriteria(criteria, value) &&
               meetsDateCriteria(criteria, value);
    }

    /** Indicates whether the passed-in row meets the user id criteria. */
    private static boolean meetsUserIdCriteria(RegisteredUserCriteria criteria, RegisteredUser registeredUser) {
        if (criteria.getUserIds() == null || criteria.getUserIds().isEmpty()) {
            return true;
        } else {
            return criteria.getUserIds().contains(registeredUser.getUserId());
        }
    }

    /** Indicates whether the passed-in row meets the user name criteria. */
    private static boolean meetsUserNameCriteria(RegisteredUserCriteria criteria, RegisteredUser registeredUser) {
        if (criteria.getUserNames() == null || criteria.getUserNames().isEmpty()) {
            return true;
        } else {
            return criteria.getUserNames().contains(registeredUser.getUserName());
        }
    }

    /** Indicates whether the passed-in row meets the OpenId provider criteria. */
    private static boolean meetsOpenIdProviderCriteria(RegisteredUserCriteria criteria, RegisteredUser registeredUser) {
        if (criteria.getOpenIdProviders() == null || criteria.getOpenIdProviders().isEmpty()) {
            return true;
        } else {
            return criteria.getOpenIdProviders().contains(registeredUser.getOpenIdProvider());
        }
    }

    /** Indicates whether the passed-in row meets the admin flag criteria. */
    private static boolean meetsAdminCriteria(RegisteredUserCriteria criteria, RegisteredUser registeredUser) {
        if (criteria.getAdmin() == null) {
            return true;
        } else {
            if (criteria.getAdmin()) {
                return registeredUser.isAdmin();
            } else {
                return !registeredUser.isAdmin();
            }
        }
    }

    /** Indicates whether the passed-in row meets the locked flag criteria. */
    private static boolean meetsLockedCriteria(RegisteredUserCriteria criteria, RegisteredUser registeredUser) {
        if (criteria.getLocked() == null) {
            return true;
        } else {
            if (criteria.getLocked()) {
                return registeredUser.isLocked();
            } else {
                return !registeredUser.isLocked();
            }
        }
    }

    /** Indicates whether the passed-in row meets the date criteria. */
    private static boolean meetsDateCriteria(RegisteredUserCriteria criteria, RegisteredUser registeredUser) {
        if (criteria.getStartDate() != null && criteria.getEndDate() != null) {
            return DateUtils.isBetween(registeredUser.getRegistrationDate(), criteria.getStartDate(), criteria.getEndDate());
        } else if (criteria.getStartDate() != null) {
            return DateUtils.isAfter(registeredUser.getRegistrationDate(), criteria.getStartDate());
        } else if (criteria.getEndDate() != null) {
            return DateUtils.isBefore(registeredUser.getRegistrationDate(), criteria.getEndDate());
        } else {
            return true;
        }
    }
}
