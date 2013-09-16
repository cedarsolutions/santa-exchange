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

import com.cedarsolutions.client.gwt.event.ViewEventHandler;
import com.cedarsolutions.client.gwt.module.view.IModulePageView;
import com.cedarsolutions.shared.domain.OpenIdProvider;



/**
 * View for the external (public-facing) landing page.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public interface IExternalLandingPageView extends IModulePageView {

    /** Tell the view whether a user is currently logged in. */
    void setIsLoggedIn(boolean isLoggedIn);

    /** Find out whether the view thinks anyone is logged in. */
    boolean getIsLoggedIn();

    /** Set the event handler for the continue action. */
    void setContinueEventHandler(ViewEventHandler continueEventHandler);

    /** Get the continue event handler. */
    ViewEventHandler getContinueEventHandler();

    /** Set the event handler for the login selector. */
    void setLoginSelectorEventHandler(ViewEventHandler loginSelectorEventHandler);

    /** Get the login selector event handler. */
    ViewEventHandler getLoginSelectorEventHandler();

    /** Get the selected OpenId provider key. */
    OpenIdProvider getSelectedProvider();

}
