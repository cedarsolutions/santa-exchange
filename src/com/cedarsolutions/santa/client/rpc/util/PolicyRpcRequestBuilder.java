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
package com.cedarsolutions.santa.client.rpc.util;

import com.cedarsolutions.santa.client.SantaExchangeConfig;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.rpc.RpcRequestBuilder;

/**
 * RPC request builder that applies system-wide policies.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class PolicyRpcRequestBuilder extends RpcRequestBuilder {

    /** Timeout to apply to RPC calls, in milliseconds. */
    private int timeoutMs;

    /** Default constructor. */
    public PolicyRpcRequestBuilder() {
        SantaExchangeConfig config = GWT.create(SantaExchangeConfig.class);
        this.timeoutMs = config.system_defaultRpcTimeoutMs();
    }

    /** Create a request builder that applies system-wide policies. */
    @Override
    protected RequestBuilder doCreate(String serviceEntryPoint) {
        RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, serviceEntryPoint);
        builder.setTimeoutMillis(this.timeoutMs);
        return builder;
    }

}
