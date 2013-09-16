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
package com.cedarsolutions.santa.client.admin.view;

import com.cedarsolutions.client.gwt.datasource.IBackendDataRenderer;
import com.cedarsolutions.client.gwt.event.ViewEventHandlerWithContext;
import com.cedarsolutions.client.gwt.module.view.IModuleTabView;
import com.cedarsolutions.client.gwt.validation.IViewWithValidation;
import com.cedarsolutions.santa.shared.domain.audit.AuditEvent;
import com.cedarsolutions.santa.shared.domain.audit.AuditEventCriteria;

/**
 * Tab that contains audit functionality.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public interface IAuditTabView extends IModuleTabView, IViewWithValidation, IBackendDataRenderer<AuditEvent, AuditEventCriteria> {

    /** Set the history token. */
    void setHistoryToken(String historyToken);

    /** Get the user id selected handler. */
    ViewEventHandlerWithContext<String> getUserIdSelectedHandler();

    /** Set the user id selected handler. */
    void setUserIdSelectedHandler(ViewEventHandlerWithContext<String> userIdSelectedHandler);

}
