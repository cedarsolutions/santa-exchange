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
package com.cedarsolutions.santa.server.rpc.impl;

import com.cedarsolutions.santa.client.rpc.ITestRpc;
import com.cedarsolutions.server.service.impl.AbstractService;

/**
 * RPC used for security testing in the UI.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class TestRpc extends AbstractService implements ITestRpc {

    /** RPC call that is unprotected. */
    @Override
    public String unprotectedRpc(String value) {
        return "unprotectedRpc/" + value;
    }

    /** RPC call that requires ROLE_USER. */
    @Override
    public String userRpc(String value) {
        return "userRpc/" + value;
    }

    /** RPC call that requires ROLE_ADMIN. */
    @Override
    public String adminRpc(String value) {
        return "adminRpc/" + value;
    }

    /** RPC call that requires ROLE_USER and ROLE_ENABLED. */
    @Override
    public String enabledUserRpc(String value) {
        return "enabledUserRpc/" + value;
    }

    /** RPC call that requires ROLE_ADMIN and ROLE_ENABLED. */
    @Override
    public String enabledAdminRpc(String value) {
        return "enabledAdminRpc/" + value;
    }
}
