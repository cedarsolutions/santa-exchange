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
package com.cedarsolutions.santa.client.common.widget;

import com.cedarsolutions.santa.shared.domain.audit.AuditEventType;

/**
 * List box containing available audit event types.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class AuditEventTypeList extends DropdownList<AuditEventType> {

    /** Create the list. */
    public AuditEventTypeList() {
        this(false);
    }

    /** Create the list, optionally adding the "any" choice. */
    public AuditEventTypeList(boolean addAny) {
        if (addAny) {
            this.addDropdownItemAny();
        }

        for (AuditEventType eventType : AuditEventType.values()) {
            this.addDropdownItem(eventType);
        }

        this.setVisibleItemCount(1);
    }

}
