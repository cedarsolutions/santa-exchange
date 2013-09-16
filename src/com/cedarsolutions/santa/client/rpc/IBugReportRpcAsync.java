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

import com.cedarsolutions.santa.shared.domain.BugReport;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Asynchronous version of IBugReportRpc.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public interface IBugReportRpcAsync {

    /**
     * Submit a bug report.
     * @param bugReport  Bug report to submit
     * @param callback   Callback to be invoked after method call completes
     */
    void submitBugReport(BugReport bugReport, AsyncCallback<Void> callback);

}
