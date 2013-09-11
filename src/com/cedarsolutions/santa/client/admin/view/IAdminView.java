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
package com.cedarsolutions.santa.client.admin.view;

import com.cedarsolutions.client.gwt.event.ViewEventHandler;
import com.cedarsolutions.client.gwt.module.view.IModuleTemplateView;
import com.cedarsolutions.shared.domain.FederatedUser;

/**
 * Template view that all other views in this module are rendered in terms of.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public interface IAdminView extends IModuleTemplateView {

    /** Set the current user. */
    void setCurrentUser(FederatedUser currentUser);

    /** Get the current user. */
    FederatedUser getCurrentUser();

    /** Set the internal landing page event handler. */
    void setInternalLandingPageEventHandler(ViewEventHandler internalLandingPageEventHandler);

    /** Get the internal landing page event handler. */
    ViewEventHandler getInternalLandingPageEventHandler();

    /** Set the dashboard event handler. */
    void setDashboardEventHandler(ViewEventHandler dashboardEventHandler);

    /** Get the dashboard event handler. */
    ViewEventHandler getDashboardEventHandler();

    /** Set the event handler for the about action. */
    void setAboutEventHandler(ViewEventHandler aboutEventHandler);

    /** Get the about event handler. */
    ViewEventHandler getAboutEventHandler();

    /** Set the event handler for the bug report action. */
    void setBugReportEventHandler(ViewEventHandler bugReportEventHandler);

    /** Get the bug report event handler. */
    ViewEventHandler getBugReportEventHandler();

    /** Set the event handler for the source code action. */
    void setSourceCodeEventHandler(ViewEventHandler sourceCodeEventHandler);

    /** Get the source code event handler. */
    ViewEventHandler getSourceCodeEventHandler();

    /** Set the logout event handler. */
    void setLogoutEventHandler(ViewEventHandler logoutEventHandler);

    /** Get the logout event handler. */
    ViewEventHandler getLogoutEventHandler();

}
