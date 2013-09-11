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
package com.cedarsolutions.santa.client.admin;

import java.util.HashSet;
import java.util.Set;

import com.cedarsolutions.client.gwt.module.IClientSession;
import com.cedarsolutions.client.gwt.module.ModuleSessionEventFilter;
import com.cedarsolutions.santa.client.SantaExchangeEventTypes;
import com.cedarsolutions.santa.client.SantaExchangeMessages;
import com.cedarsolutions.santa.client.common.presenter.SystemStateInjector;
import com.cedarsolutions.santa.client.common.widget.WidgetUtils;
import com.cedarsolutions.santa.shared.domain.ClientSession;
import com.cedarsolutions.shared.domain.ErrorDescription;
import com.google.inject.Inject;

/**
 * Event filter which ensures that a user is a logged-in admin user.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class AdminEventFilter extends ModuleSessionEventFilter<AdminEventBus> {

    /** Injector for the shared client session singleton. */
    private SystemStateInjector systemStateInjector;

    /** Santa exchange messages. */
    private SantaExchangeMessages messages;

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
    protected boolean filterEventOnceInitialized(String eventType, Object[] params, AdminEventBus eventBus) {
        if (this.getSession().isLocked()) {
            eventBus.setFilteringEnabled(false);
            eventBus.lockOutUser();
            eventBus.setFilteringEnabled(true);
            return false;
        } else {
            if (this.getSession().isAdmin()) {
                // If the user is logged in as an admin, then it's OK to handle the event
                return true;
            } else {
                if (!this.getSession().isLoggedIn()) {
                    // If the user is not logged in, then redirect to the login page
                    eventBus.setFilteringEnabled(false);
                    eventBus.showLoginRequiredPage(WidgetUtils.getInstance().getWndLocationHref());
                    eventBus.setFilteringEnabled(true);
                    return false;
                } else {
                    // Otherwise, they're logged in but not an admin.
                    // Just pop an error message and redirect to the landing page
                    // Note: the events we forward to must not require admin access, or we'll loop!

                    String message = messages.filter_adminAccessRequired();
                    String supporting = messages.filter_youWillBeRedirected();
                    ErrorDescription error = new ErrorDescription(message, supporting);

                    eventBus.setFilteringEnabled(false);
                    eventBus.showLandingPage();
                    eventBus.showErrorPopup(error);
                    eventBus.setFilteringEnabled(true);

                    return false;
                }
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

    public SantaExchangeMessages getMessages() {
        return this.messages;
    }

    @Inject
    public void setMessages(SantaExchangeMessages messages) {
        this.messages = messages;
    }
}
