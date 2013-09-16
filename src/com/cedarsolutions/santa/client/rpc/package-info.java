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

/**
 * Interfaces for client-visible GWT RPC services.
 *
 * <p>
 * RPCs are client-visible services.  <i>Only</i> functionality that is useful to
 * clients should be exposed in the RPC interface.
 * </p>
 *
 * <p>
 * These interfaces are always paired, with one main interface that the service
 * implements (i.e. IClientSessionRpc) and an associated asynchronous interface
 * that GWT actually invokes (i.e. IClientSessionRpcAsync).
 * </p>
 *
 * <p>
 * Either entire RPC interfaces or specific RPC method calls can be protected
 * using Spring security annotations.  Interfaces or methods annotated with
 * \@Secured("ROLE_USER") can only be invoked by logged-in users.  Interfaces or
 * methods  annotated as \@Secured("ROLE_ADMIN") can only be invoked by logged-in
 * adminstrators.  Other methods or interfaces can be invoked without an
 * existing session (i.e. by anyone in the world), so be careful.  Most
 * RPCs should be limited to either ROLE_USER or ROLE_ADMIN.
 * </p>
 *
 * <p>
 * Some methods may interact with data owned by a specific user.  For security
 * purposes, it is very important that these methods rely on the session to
 * identify the user, rather than accepting credentials or other user identifiers
 * as method arguments.  This way, one logged in user will not be able to spoof
 * another logged in user.  Users will only be able to get at their own data.
 * </p>
 *
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
package com.cedarsolutions.santa.client.rpc;
