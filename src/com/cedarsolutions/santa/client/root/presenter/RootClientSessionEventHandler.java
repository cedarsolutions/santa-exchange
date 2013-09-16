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
package com.cedarsolutions.santa.client.root.presenter;

import com.cedarsolutions.santa.client.common.presenter.ModuleClientSessionEventHandler;
import com.cedarsolutions.santa.client.root.RootEventBus;
import com.cedarsolutions.santa.shared.domain.Module;
import com.mvp4g.client.annotation.EventHandler;

/**
 * Client session event handler.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@EventHandler
public class RootClientSessionEventHandler extends ModuleClientSessionEventHandler<RootEventBus> {

    /** Get the current module. */
    @Override
    public Module getModule() {
        return Module.ROOT;
    }

}
