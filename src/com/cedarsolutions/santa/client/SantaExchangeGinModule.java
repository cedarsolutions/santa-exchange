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
package com.cedarsolutions.santa.client;

import com.cedarsolutions.santa.client.rpc.util.CallerIdManager;
import com.cedarsolutions.santa.shared.domain.ClientSession;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;

/**
 * Application-wide GIN module for client-side dependency injection.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class SantaExchangeGinModule extends AbstractGinModule {

    /** Cross-module client session singleton. */
    private static ClientSession CLIENT_SESSION;

    /** Cross-module caller id manager singleton. */
    private static CallerIdManager CALLER_ID_MANAGER;

    /** Configure GIN. */
    @Override
    protected void configure() {
        // No application-wide configuration
    }

    /** Get the cross-module client session singleton. */
    @Provides
    public ClientSession getClientSession() {
        if (CLIENT_SESSION == null) {
            CLIENT_SESSION = new ClientSession();
        }
        return CLIENT_SESSION;
    }

    /** Get the cross-module caller id manager singleton. */
    @Provides
    public CallerIdManager getCallerIdManager() {
        if (CALLER_ID_MANAGER == null) {
            CALLER_ID_MANAGER = new CallerIdManager();
        }
        return CALLER_ID_MANAGER;
    }

}
