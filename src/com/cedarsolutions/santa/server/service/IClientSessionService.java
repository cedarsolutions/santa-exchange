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
package com.cedarsolutions.santa.server.service;

import com.cedarsolutions.santa.shared.domain.ClientSession;
import com.cedarsolutions.santa.shared.domain.Module;

/**
 * Client session functionality.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public interface IClientSessionService {

    /**
     * Establish a client session on the back-end.
     * @param module                Module that the session is being established for
     * @param logoutDestinationUrl  Destination URL to use when generating the logout URL
     * @return Newly-established client session as retrieved from the back-end.
     */
    ClientSession establishClientSession(Module module, String logoutDestinationUrl);

    /**
     * Invalidate the current session at the back-end.
     * @param module  Module that the session is being invalidated within
     */
    void invalidateClientSession(Module module);

    /**
     * Retrieve a client session from the back-end, with no destination URL set.
     * @return Client session retrieved from the back-end.
     */
    ClientSession retrieveClientSession();

    /**
     * Retrieve a client session from the back-end.
     * @param logoutDestinationUrl  Destination URL to use when generating the logout URL
     * @return Client session retrieved from the back-end.
     */
    ClientSession retrieveClientSession(String logoutDestinationUrl);

}
