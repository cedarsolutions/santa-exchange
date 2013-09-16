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
package com.cedarsolutions.santa.shared.domain.audit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Id;

import com.cedarsolutions.shared.domain.TranslatableDomainObject;
import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;
import com.googlecode.objectify.annotation.Indexed;
import com.googlecode.objectify.annotation.Unindexed;

/**
 * Audit event to be logged on the back-end.
 *
 * <p>
 * It originally seemed more natural to implement the extra data as a map.
 * However, that turns out to be impractical.  It's hard to serialize a
 * generic map into the GAE datastore.  So, I went with explicit objects,
 * which seems to work reasonably well.
 * </p>
 *
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@Unindexed
public class AuditEvent extends TranslatableDomainObject {

    /** Serialization version number, which can be important to the GAE back-end. */
    private static final long serialVersionUID = 1L;

    /** Event id. */
    @Id private Long eventId;  // because this is a Long, the id will be auto-generated

    /** Event type. */
    @Indexed private AuditEventType eventType;

    /** Event timestamp. */
    @Indexed private Date eventTimestamp;

    /** User id of current user, if any. */
    @Indexed private String userId;

    /** Session id of the current session, if any. */
    private String sessionId;

    /** Extra event data. */
    @Embedded private List<ExtraData> extraData;

    /** Default constructor. */
    public AuditEvent() {
    }

    /** Compare this object to another object. */
    @Override
    public boolean equals(Object obj) {
        AuditEvent other = (AuditEvent) obj;
        return new EqualsBuilder()
                    .append(this.eventId, other.eventId)
                    .append(this.eventType, other.eventType)
                    .append(this.eventTimestamp, other.eventTimestamp)
                    .append(this.userId, other.userId)
                    .append(this.sessionId, other.sessionId)
                    .append(this.extraData, other.extraData)
                    .isEquals();
    }

    /** Generate a hash code for this object. */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                    .append(this.eventId)
                    .append(this.eventType)
                    .append(this.eventTimestamp)
                    .append(this.userId)
                    .append(this.sessionId)
                    .append(this.extraData)
                    .toHashCode();
    }

    public Long getEventId() {
        return this.eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public AuditEventType getEventType() {
        return this.eventType;
    }

    public void setEventType(AuditEventType eventType) {
        this.eventType = eventType;
    }

    public Date getEventTimestamp() {
        return this.eventTimestamp;
    }

    public void setEventTimestamp(Date eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public List<ExtraData> getExtraData() {
        return this.extraData;
    }

    public void setExtraData(List<ExtraData> extraData) {
        this.extraData = extraData;
    }

    public void setExtraData(ExtraData... extraData) {
        if (extraData == null) {
            this.extraData = null;
        } else {
            this.extraData = new ArrayList<ExtraData>();
            if (extraData.length > 0) {
                for (ExtraData element : extraData) {
                    this.extraData.add(element);
                }
            }
        }
    }
}
