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
package com.cedarsolutions.santa.client.root;

import java.util.HashSet;
import java.util.Set;

import com.cedarsolutions.client.gwt.module.IClientSession;
import com.cedarsolutions.client.gwt.module.ModuleSessionEventFilter;
import com.cedarsolutions.santa.client.SantaExchangeEventTypes;
import com.cedarsolutions.santa.client.common.presenter.SystemStateInjector;
import com.cedarsolutions.santa.shared.domain.ClientSession;
import com.google.inject.Inject;

/**
 * Event filter which makes sure the session is loaded properly.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class RootEventFilter extends ModuleSessionEventFilter<RootEventBus> {

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
        excluded.add(SantaExchangeEventTypes.INIT);              // Init event doesn't require session
        excluded.add(SantaExchangeEventTypes.REPLACE_ROOT_BODY); // Don't bother to filter this event, since it's a rendering step
        excluded.add(SantaExchangeEventTypes.CLEAR_SESSION);     // We don't want to apply any rules if someone says to clear the session
        return excluded;
    }

    /** Filter the event once the session has been initialized. */
    @Override
    protected boolean filterEventOnceInitialized(String eventType, Object[] params, RootEventBus eventBus) {
        return true;  // anything is allowed once there's a session
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
