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
package com.cedarsolutions.santa.client.internal;

import java.util.HashSet;
import java.util.Set;

import com.cedarsolutions.client.gwt.module.IClientSession;
import com.cedarsolutions.client.gwt.module.ModuleSessionEventFilter;
import com.cedarsolutions.santa.client.SantaExchangeEventTypes;
import com.cedarsolutions.santa.client.common.presenter.SystemStateInjector;
import com.cedarsolutions.santa.client.common.widget.WidgetUtils;
import com.cedarsolutions.santa.shared.domain.ClientSession;
import com.google.inject.Inject;

/**
 * Event filter which ensures that a user is logged in.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class InternalEventFilter extends ModuleSessionEventFilter<InternalEventBus> {

    /** Injector for the shared client session singleton. */
    private SystemStateInjector systemStateInjector;

    /** Get the client session in use by this module. */
    @Override
    protected IClientSession getClientSession() {
        return this.getSession();
    }

    /** Get the event types that should be excluded from filtering. */
    @Override
    protected Set<String> getExcludedEventTypes() {
        Set<String> excluded = new HashSet<String>();
        excluded.add(SantaExchangeEventTypes.START);             // Start event doesn't require session
        excluded.add(SantaExchangeEventTypes.REPLACE_ROOT_BODY); // Don't bother to filter this event, since it's a rendering step
        excluded.add(SantaExchangeEventTypes.CLEAR_SESSION);     // We don't want to apply any rules if someone says to clear the session
        return excluded;
    }

    /** Filter the event once the session has been initialized. */
    @Override
    protected boolean filterEventOnceInitialized(String eventType, Object[] params, InternalEventBus eventBus) {
        if (this.getSession().isLocked()) {
            eventBus.setFilteringEnabled(false);
            eventBus.lockOutUser();
            eventBus.setFilteringEnabled(true);
            return false;
        } else {
            if (this.getSession().isLoggedIn()) {
                // If the user is logged in, then it's OK to handle the event
                return true;
            } else {
                // If the user is not logged in, then redirect to the login page
                eventBus.setFilteringEnabled(false);
                eventBus.showLoginRequiredPage(WidgetUtils.getInstance().getWndLocationHref());
                eventBus.setFilteringEnabled(true);
                return false;
            }
        }
    }

    /** Get the session from the injector. */
    public ClientSession getSession() {
        return this.systemStateInjector.getSession();
    }

    public SystemStateInjector getSystemStateInjector() {
        return this.systemStateInjector;
    }

    @Inject
    public void setSystemStateInjector(SystemStateInjector systemStateInjector) {
        this.systemStateInjector = systemStateInjector;
    }

}
