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
package com.cedarsolutions.santa.server.rpc.impl;

import org.apache.log4j.Logger;

import com.cedarsolutions.exception.NotConfiguredException;
import com.cedarsolutions.exception.ServiceException;
import com.cedarsolutions.santa.client.rpc.IClientSessionRpc;
import com.cedarsolutions.santa.server.service.IClientSessionService;
import com.cedarsolutions.santa.shared.domain.ClientSession;
import com.cedarsolutions.santa.shared.domain.Module;
import com.cedarsolutions.server.service.impl.AbstractService;
import com.cedarsolutions.util.LoggingUtils;

/**
 * Client session functionality.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ClientSessionRpc extends AbstractService implements IClientSessionRpc {

    /** Logger instance. */
    private static Logger LOGGER = LoggingUtils.getLogger(ClientSessionRpc.class);

    /** Client session service. */
    private IClientSessionService clientSessionService;

    /**
     * Invoked by a bean factory after it has set all bean properties.
     * @throws NotConfiguredException In the event of misconfiguration.
     */
    @Override
    public void afterPropertiesSet() throws NotConfiguredException {
        super.afterPropertiesSet();
        if (this.clientSessionService == null) {
            throw new NotConfiguredException("ClientSessionRpc is not properly configured.");
        }
    }

    /**
     * Establish a client session on the back-end.
     * @param module                Module that the session is being established for
     * @param logoutDestinationUrl  Destination URL to use when generating the logout URL
     * @return Newly-established client session as retrieved from the back-end.
     * @throws ServiceException If there is a problem with the method call.
     */
    @Override
    public ClientSession establishClientSession(Module module, String logoutDestinationUrl) throws ServiceException {
        try  {
            return this.clientSessionService.establishClientSession(module, logoutDestinationUrl);
        } catch (Exception e) {
            LOGGER.error("Error establishing client session: " + e.getMessage(), e);
            throw new ServiceException("Error establishing client session: " + e.getMessage(), e);
        }
    }

    /**
     * Invalidate the current session at the back-end.
     * @param module  Module that the session is being invalidated within
     * @throws ServiceException If there is a problem with the method call.
     */
    @Override
    public void invalidateClientSession(Module module) throws ServiceException {
        try  {
            this.clientSessionService.invalidateClientSession(module);
        } catch (Exception e) {
            LOGGER.error("Error invalidating client session: " + e.getMessage(), e);
            throw new ServiceException("Error invalidating client session: " + e.getMessage(), e);
        }
    }

    public IClientSessionService getClientSessionService() {
        return this.clientSessionService;
    }

    public void setClientSessionService(IClientSessionService clientSessionService) {
        this.clientSessionService = clientSessionService;
    }

}
