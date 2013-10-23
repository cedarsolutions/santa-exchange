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

import org.springframework.security.annotation.Secured;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.server.rpc.XsrfProtect;

/**
 * RPC used for security testing in the UI.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@RemoteServiceRelativePath("rpc/testRpc.rpc")
@XsrfProtect  // Apply protection to prevent CSRF/XSRF attacks
public interface ITestRpc extends RemoteService {

    /** RPC call that is unprotected. */
    String unprotectedRpc(String value);

    /** RPC call that requires ROLE_USER. */
    @Secured("ROLE_USER")
    String userRpc(String value);

    /** RPC call that requires ROLE_ADMIN. */
    @Secured("ROLE_ADMIN")
    String adminRpc(String value);

    /** RPC call that requires ROLE_USER and ROLE_ENABLED. */
    @Secured({ "ROLE_USER", "ROLE_ENABLED" })
    String enabledUserRpc(String value);

    /** RPC call that requires ROLE_ADMIN and ROLE_ENABLED. */
    @Secured({ "ROLE_ADMIN", "ROLE_ENABLED" })
    String enabledAdminRpc(String value);

}
