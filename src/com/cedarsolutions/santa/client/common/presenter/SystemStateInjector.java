/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 *              C E D A R
 *          S O L U T I O N S       "Software done right."
 *           S O F T W A R E
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2012-2013 Kenneth J. Pronovici.
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
package com.cedarsolutions.santa.client.common.presenter;

import com.cedarsolutions.santa.client.SantaExchangeGinModule;
import com.cedarsolutions.santa.client.rpc.util.CallerIdManager;
import com.cedarsolutions.santa.shared.domain.ClientSession;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

/**
 * Injector for the shared system state.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@GinModules(SantaExchangeGinModule.class)
public interface SystemStateInjector extends Ginjector {

    /** Get the shared client session. */
    ClientSession getSession();

    /** Get the shared caller id manager. */
    CallerIdManager getCallerIdManager();

}
