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
package com.cedarsolutions.santa.client.admin;

import com.cedarsolutions.client.gwt.module.ModuleEventBus;
import com.cedarsolutions.santa.client.SantaExchangeCustomLogger;
import com.cedarsolutions.santa.client.SantaExchangeEventTypes;
import com.cedarsolutions.santa.client.SantaExchangeGinModule;
import com.cedarsolutions.santa.client.admin.presenter.AdminClientSessionEventHandler;
import com.cedarsolutions.santa.client.admin.presenter.AdminLandingPagePresenter;
import com.cedarsolutions.santa.client.admin.presenter.AdminPresenter;
import com.cedarsolutions.santa.client.admin.presenter.AuditTabPresenter;
import com.cedarsolutions.santa.client.admin.presenter.HomeTabPresenter;
import com.cedarsolutions.santa.client.admin.presenter.UserTabPresenter;
import com.cedarsolutions.shared.domain.ErrorDescription;
import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Debug.LogLevel;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Filters;
import com.mvp4g.client.annotation.Start;

/**
 * Event bus for events that require an admin user.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@Filters(filterClasses = AdminEventFilter.class)
@Events(startPresenter = AdminLandingPagePresenter.class,
        module = AdminModule.class,
        ginModules = { SantaExchangeGinModule.class, AdminGinModule.class })
@Debug(logLevel = LogLevel.DETAILED, logger = SantaExchangeCustomLogger.class)
public interface AdminEventBus extends ModuleEventBus {

    /** Handle the module start event. */
    @Start
    @Event(handlers = { HomeTabPresenter.class,
                        AuditTabPresenter.class,
                        UserTabPresenter.class })
    void start();

    /**
     * Replace the root body, rendering the passed-in widget.
     * @param contents  Widget contents to render
     */
    @Override
    @Event(forwardToParent = true)
    void replaceRootBody(IsWidget contents);

    /**
     * Replace the module body, rendering the passed-in widget.
     * @param contents  Widget contents to render
     */
    @Override
    @Event(handlers = AdminPresenter.class)
    void replaceModuleBody(IsWidget contents);

    /** Clear all history maintained by the framework. */
    @Event(forwardToParent = true)
    void clearHistory();

    /** Initialize the current session. */
    @Override
    @Event(handlers = AdminClientSessionEventHandler.class)
    void initializeSession(String eventType, Object[] params);

    /** Clear the current session. */
    @Override
    @Event(handlers = AdminClientSessionEventHandler.class)
    void clearSession();

    /** Show the landing page, properly redirecting to the right place based on the user. */
    @Event(forwardToParent = true)
    String showLandingPage();

    /** Show the external landing page. */
    @Event(forwardToParent = true)
    void showExternalLandingPage();

    /** Show the internal landing page. */
    @Event(forwardToParent = true)
    void showInternalLandingPage();

    /** Show the application dashboard. */
    @Event(forwardToParent = true)
    void showApplicationDashboard();

    /** Show the source code page. */
    @Event(forwardToParent = true)
    void showSourceCode();

    /** Show the admin landing page. */
    @Event(handlers = AdminLandingPagePresenter.class,
           historyConverter = AdminHistoryConverter.class,
           name = SantaExchangeEventTypes.ADMIN_LANDING_PAGE)
    void showAdminLandingPage();

    /**
     * Show the "login required" page.
     * @param destinationUrl  Destination URL to redirect to after login
     */
    @Event(forwardToParent = true)
    void showLoginRequiredPage(String destinationUrl);

    /**
     * Show the user search page, with criteria limited by user id.
     * @param userId  Id of the user to search for
     */
    @Event(handlers = UserTabPresenter.class)
    void showUserSearchById(String userId);

    /** Show the about pop-up. */
    @Event(forwardToParent = true)
    void showAboutPopup();

    /** Show an error via the error pop-up. */
    @Event(forwardToParent = true)
    void showErrorPopup(ErrorDescription error);

    /** Show a "bookmark not found" error via the error pop-up. */
    @Event(forwardToParent = true)
    void showBookmarkNotFoundError();

    /** Show the bug report dialog. */
    @Event(forwardToParent = true)
    void showBugReportDialog();

    /** Log out the current user. */
    @Event(forwardToParent = true)
    void logout();

    /** Lock the user out of the application. */
    @Event(forwardToParent = true)
    void lockOutUser();

    /** Select the home tab on the landing page. */
    @Event(handlers = AdminLandingPagePresenter.class,
           historyConverter = AdminHistoryConverter.class,
           name = SantaExchangeEventTypes.ADMIN_HOME_TAB)
    String selectHomeTab();

    /** Select the audit tab on the landing page. */
    @Event(handlers = AdminLandingPagePresenter.class,
           historyConverter = AdminHistoryConverter.class,
           name = SantaExchangeEventTypes.ADMIN_AUDIT_TAB)
    String selectAuditTab();

    /** Select the user tab on the landing page. */
    @Event(handlers = AdminLandingPagePresenter.class,
           historyConverter = AdminHistoryConverter.class,
           name = SantaExchangeEventTypes.ADMIN_USER_TAB)
    String selectUserTab();

}
