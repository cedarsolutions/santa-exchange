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
package com.cedarsolutions.santa.shared.domain.exchange;

import java.util.ArrayList;
import java.util.List;

import com.cedarsolutions.shared.domain.TranslatableDomainObject;
import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

/**
 * Exchange search criteria.
 *
 * <p>
 * You must provide a user id.  This is because we want all interactions
 * with user-owned data to explicitly reference a user.  That way, it's more
 * difficult to inadvertently return data to a user that does not own that
 * data.  You can optionally supply a set of exchange ids, and the returned
 * list will be limited to the ids in that set (like an IN clause in SQL).
 * </p>
 *
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ExchangeCriteria extends TranslatableDomainObject {

    /** Serialization version number, which can be important to the GAE back-end. */
    private static final long serialVersionUID = 1L;

    /** User id. */
    private String userId;

    /** List of exchange ids. */
    private List<Long> exchangeIds;

    /** Compare this object to another object. */
    @Override
    public boolean equals(Object obj) {
        ExchangeCriteria other = (ExchangeCriteria) obj;
        return new EqualsBuilder()
                    .append(this.userId, other.userId)
                    .append(this.exchangeIds, other.exchangeIds)
                    .isEquals();
    }

    /** Generate a hash code for this object. */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                    .append(this.userId)
                    .append(this.exchangeIds)
                    .toHashCode();
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Long> getExchangeIds() {
        return this.exchangeIds;
    }

    public void setExchangeIds(Long... exchangeIds) {
        if (exchangeIds == null) {
            this.exchangeIds = null;
        } else {
            this.exchangeIds = new ArrayList<Long>();
            for (Long exchangeId : exchangeIds) {
                this.exchangeIds.add(exchangeId);
            }
        }
    }

    public void setExchangeIds(List<Long> exchangeIds) {
        this.exchangeIds = exchangeIds;
    }

}
