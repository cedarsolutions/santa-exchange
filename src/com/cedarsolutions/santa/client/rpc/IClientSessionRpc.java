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
package com.cedarsolutions.santa.client.rpc;

import com.cedarsolutions.exception.ServiceException;
import com.cedarsolutions.santa.shared.domain.ClientSession;
import com.cedarsolutions.santa.shared.domain.Module;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Client session functionality.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@RemoteServiceRelativePath("rpc/clientSessionRpc.rpc")
public interface IClientSessionRpc extends RemoteService {

    /**
     * Establish a client session on the back-end.
     * @param module                Module that the session is being established for
     * @param logoutDestinationUrl  Destination URL to use when generating the logout URL
     * @return Newly-established client session as retrieved from the back-end.
     * @throws ServiceException If there is a problem with the method call.
     */
    ClientSession establishClientSession(Module module, String logoutDestinationUrl) throws ServiceException;

    /**
     * Invalidate the current session at the back-end.
     * @param module  Module that the session is being invalidated within
     * @throws ServiceException If there is a problem with the method call.
     */
    void invalidateClientSession(Module module) throws ServiceException;

}
