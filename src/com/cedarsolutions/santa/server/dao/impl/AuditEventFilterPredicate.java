/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 *              C E D A R
 *          S O L U T I O N S       "Software done right."
 *           S O F T W A R E
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2011-2012 Kenneth J. Pronovici.
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
import com.cedarsolutions.santa.shared.domain.audit.AuditEvent;
import com.cedarsolutions.santa.shared.domain.audit.AuditEventCriteria;

/**
 * Filter predicate based on audit event search criteria.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class AuditEventFilterPredicate implements IFilterPredicate<AuditEvent> {

    /** Search criteria. */
    private AuditEventCriteria criteria;

    /** Create a filter predicate in terms of audit event search criteria. */
    public AuditEventFilterPredicate(AuditEventCriteria criteria) {
        this.criteria = criteria;
    }

    /** Evaluate the predicate for the passed-in value. */
    @Override
    public boolean evaluate(AuditEvent value) {
        return meetsEventTypeCriteria(criteria, value) &&
               meetsUserIdCriteria(criteria, value);
    }

    /** Indicates whether the passed-in row meets the event type criteria. */
    private static boolean meetsEventTypeCriteria(AuditEventCriteria criteria, AuditEvent auditEvent) {
        if (criteria.getEventTypes() == null || criteria.getEventTypes().isEmpty()) {
            return true;
        } else {
            return criteria.getEventTypes().contains(auditEvent.getEventType());
        }
    }

    /** Indicates whether the passed-in row meets the user id criteria. */
    private static boolean meetsUserIdCriteria(AuditEventCriteria criteria, AuditEvent auditEvent) {
        if (criteria.getUserIds() == null || criteria.getUserIds().isEmpty()) {
            return true;
        } else {
            return criteria.getUserIds().contains(auditEvent.getUserId());
        }
    }

}
