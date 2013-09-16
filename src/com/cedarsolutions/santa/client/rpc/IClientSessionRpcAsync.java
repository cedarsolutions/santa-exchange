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
package com.cedarsolutions.santa.client.rpc;

import com.cedarsolutions.santa.shared.domain.ClientSession;
import com.cedarsolutions.santa.shared.domain.Module;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Asynchronous version of IClientSessionRpc.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public interface IClientSessionRpcAsync {

    /**
     * Establish a client session on the back-end.
     * @param module                Module that the session is being established for
     * @param logoutDestinationUrl  Destination URL to use when generating the logout URL
     * @param callback              Callback to be invoked after method call completes
     */
    void establishClientSession(Module module, String logoutDestinationUrl, AsyncCallback<ClientSession> callback);

    /**
     * Invalidate the current session at the back-end.
     * @param module    Module that the session is being invalidated within
     * @param callback  Callback to be invoked after method call completes
     */
    void invalidateClientSession(Module module, AsyncCallback<Void> callback);

}
