/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 *              C E D A R
 *          S O L U T I O N S       "Software done right."
 *           S O F T W A R E
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2013,2017 Kenneth J. Pronovici.
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
package com.cedarsolutions.santa.shared.domain.audit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cedarsolutions.dao.domain.AbstractSearchCriteria;
import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

/**
 * Audit event search criteria.
 *
 * <p>
 * You must provide either a start date, or an end date, or both.  The date
 * criteria are applied inclusively.  If you do not provide a one of the
 * values, then the query will be open-ended in that direction.
 * </p>
 *
 * <p>
 * The other values are implemented like an IN clause in SQL.  A row meets the
 * event type criteria if the row's event type is in the provided list.  If the
 * provided list is empty, then that value will be ignored.  So, if you provide
 * an empty event type list, you'll get back rows with any event type.
 * </p>
 *
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class AuditEventCriteria extends AbstractSearchCriteria<AuditEvent> {

    /** Serialization version number, which can be important to the GAE back-end. */
    private static final long serialVersionUID = 1L;

    private List<AuditEventType> eventTypes;
    private List<String> userIds;
    private Date startDate;
    private Date endDate;

    /** Compare this object to another object. */
    @Override
    public boolean equals(Object obj) {
        AuditEventCriteria other = (AuditEventCriteria) obj;
        return new EqualsBuilder()
                    .append(this.eventTypes, other.eventTypes)
                    .append(this.userIds, other.userIds)
                    .append(this.startDate, other.startDate)
                    .append(this.endDate, other.endDate)
                    .isEquals();
    }

    /** Generate a hash code for this object. */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                    .append(this.eventTypes)
                    .append(this.userIds)
                    .append(this.startDate)
                    .append(this.endDate)
                    .toHashCode();
    }

    public List<AuditEventType> getEventTypes() {
        return this.eventTypes;
    }

    public void setEventTypes(AuditEventType... eventTypes) {
        if (eventTypes == null) {
            this.eventTypes = null;
        } else {
            this.eventTypes = new ArrayList<AuditEventType>();
            for (AuditEventType eventType : eventTypes) {
                this.eventTypes.add(eventType);
            }
        }
    }

    public void setEventTypes(List<AuditEventType> eventTypes) {
        this.eventTypes = eventTypes;
    }

    public List<String> getUserIds() {
        return this.userIds;
    }

    public void setUserIds(String... userIds) {
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
