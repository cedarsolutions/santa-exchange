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
package com.cedarsolutions.santa.client.external.view;

import java.util.Map;

import com.cedarsolutions.client.gwt.event.ViewEventHandler;
import com.cedarsolutions.client.gwt.module.view.IModulePageView;

/**
 * RPC test page view.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public interface IRpcTestPageView extends IModulePageView {

    /** Get the unprotected RPC handler. */
    ViewEventHandler getUnprotectedRpcHandler();

    /** Get the unprotected RPC handler. */
    void setUnprotectedRpcHandler(ViewEventHandler unprotectedRpcHandler);

    /** Get the user RPC handler. */
    ViewEventHandler getUserRpcHandler();

    /** Set the user RPC handler. */
    void setUserRpcHandler(ViewEventHandler userRpcHandler);

    /** Get the admin RPC handler. */
    ViewEventHandler getAdminRpcHandler();

    /** Set the admin RPC handler. */
    void setAdminRpcHandler(ViewEventHandler adminRpcHandler);

    /** Get the enabled user RPC handler. */
    ViewEventHandler getEnabledUserRpcHandler();

    /** Set the enabled user RPC handler. */
    void setEnabledUserRpcHandler(ViewEventHandler enabledUserRpcHandler);

    /** Get the enabled admin RPC handler. */
    ViewEventHandler getEnabledAdminRpcHandler();

    /** Set the enabled admin RPC handler. */
    void setEnabledAdminRpcHandler(ViewEventHandler enabledAdminRpcHandler);

    /**
     * Get the expected results for all RPCs.
     * The RPC identifier is the name, like "unprotectedRpc" or "userRpc".
     * @return Map from RPC identifier to expected result to display
     */
    Map<String, String> getExpectedResults();

    /**
     * Set the expected results for all RPCs.
     * The RPC identifier is the name, like "unprotectedRpc" or "userRpc".
     * @param expectedResults Map from RPC identifier to expected result to display
     */
    void setExpectedResults(Map<String, String> expectedResults);

    /**
     * Get the actual result for an RPC.
     * The RPC identifier is the name, like "unprotectedRpc" or "userRpc".
     * @param rpc    The RPC identifier
     * @return The actual result for the RPC, null if the RPC is unknown.
     */
    String getActualResult(String rpc);

    /**
     * Set the actual result for an RPC.
     * The RPC identifier is the name, like "unprotectedRpc" or "userRpc".
     * @param rpc    The RPC identifier
     * @param result The actual result for the RPC
     */
    void setActualResult(String rpc, String result);

}
