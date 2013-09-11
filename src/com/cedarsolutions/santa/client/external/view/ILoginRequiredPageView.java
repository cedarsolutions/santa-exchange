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
package com.cedarsolutions.santa.client.external.view;

import com.cedarsolutions.client.gwt.event.ViewEventHandler;
import com.cedarsolutions.client.gwt.module.view.IModulePageView;
import com.cedarsolutions.shared.domain.OpenIdProvider;

/**
 * Page to display when the user must log in.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public interface ILoginRequiredPageView extends IModulePageView {

    /** Set the event handler for the login selector. */
    void setLoginSelectorEventHandler(ViewEventHandler loginSelectorEventHandler);

    /** Get the login selector event handler. */
    ViewEventHandler getLoginSelectorEventHandler();

    /** Get the selected OpenId provider key. */
    OpenIdProvider getSelectedProvider();

}
