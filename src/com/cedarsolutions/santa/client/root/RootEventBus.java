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
package com.cedarsolutions.santa.client.root;

import com.cedarsolutions.client.gwt.module.SessionAwareEventBus;
import com.cedarsolutions.santa.client.SantaExchangeCustomLogger;
import com.cedarsolutions.santa.client.SantaExchangeEventTypes;
import com.cedarsolutions.santa.client.SantaExchangeGinModule;
import com.cedarsolutions.santa.client.admin.AdminModule;
import com.cedarsolutions.santa.client.external.ExternalModule;
import com.cedarsolutions.santa.client.internal.InternalModule;
import com.cedarsolutions.santa.client.root.presenter.LandingPageEventHandler;
import com.cedarsolutions.santa.client.root.presenter.LoginEventHandler;
import com.cedarsolutions.santa.client.root.presenter.RedirectEventHandler;
import com.cedarsolutions.santa.client.root.presenter.RootClientSessionEventHandler;
import com.cedarsolutions.santa.client.root.presenter.RootPresenter;
import com.cedarsolutions.shared.domain.ErrorDescription;
import com.cedarsolutions.shared.domain.OpenIdProvider;
import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Debug.LogLevel;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Filters;
import com.mvp4g.client.annotation.InitHistory;
import com.mvp4g.client.annotation.NotFoundHistory;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.annotation.module.ChildModule;
import com.mvp4g.client.annotation.module.ChildModules;
import com.mvp4g.client.history.ClearHistory;

/**
 * The root event bus for the application.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@Filters(filterClasses = RootEventFilter.class)
@ChildModules({@ChildModule(moduleClass = AdminModule.class, autoDisplay = false),
               @ChildModule(moduleClass = ExternalModule.class, autoDisplay = false),
               @ChildModule(moduleClass = InternalModule.class, autoDisplay = false) })
@Events(startPresenter = RootPresenter.class, historyOnStart = true,
        ginModules = SantaExchangeGinModule.class)
@Debug(logLevel = LogLevel.DETAILED, logger = SantaExchangeCustomLogger.class)
public interface RootEventBus extends SessionAwareEventBus {

    /** MVP4G start target. */
    @Start
    @Event(handlers = RootPresenter.class)
    void start();

    /**
     * Replace the root body, rendering the passed-in widget.
     * @param contents  Widget contents to render
     */
    @Event(handlers = RootPresenter.class)
    void replaceRootBody(IsWidget contents);

    /** Initialization target. */
    @InitHistory
    @Event(handlers = RootPresenter.class)
    void init();

    /** Handle unknown URLs. */
    @NotFoundHistory
    @Event(handlers = RootPresenter.class)
    void notFound();

    /** Clear all history maintained by the framework. */
    @Event(handlers = RootPresenter.class, historyConverter = ClearHistory.class)
    void clearHistory();

    /** Initialize the current session. */
    @Override
    @Event(handlers = RootClientSessionEventHandler.class)
    void initializeSession(String eventType, Object[] params);

    /** Clear the current session. */
    @Override
    @Event(handlers = RootClientSessionEventHandler.class)
    void clearSession();

    /**
     * Show the login page for a particular destination token.
     * @param openIdProvider   Open ID provider that will be used
     * @param destinationToken Destination token (page) to redirect to after login
     */
    @Event(handlers = LoginEventHandler.class)
    void showLoginPageForToken(OpenIdProvider openIdProvider, String destinationToken);

    /**
     * Show the login page for a particular destination URL.
     * @param openIdProvider   Open ID provider that will be used
     * @param destinationUrl   Destination URL to redirect to after login
     */
    @Event(handlers = LoginEventHandler.class)
    void showLoginPageForUrl(OpenIdProvider openIdProvider, String destinationUrl);

    /** Log out the current user. */
    @Event(handlers = LoginEventHandler.class)
    void logout();

    /** Lock the user out of the application. */
    @Event(handlers = LoginEventHandler.class)
    void lockOutUser();

    /**
     * Show the "login required" page.
     * @param destinationUrl  Destination URL to redirect to after login
     */
    @Event(forwardToModules = ExternalModule.class)
    void showLoginRequiredPage(String destinationUrl);

    /** Show the "account locked" page. */
    @Event(forwardToModules = ExternalModule.class,
           historyConverter = RootHistoryConverter.class,
           name = SantaExchangeEventTypes.ACCOUNT_LOCKED_PAGE)
    void showAccountLockedPage();

    /** Show the landing page, properly redirecting to the right place based on the user. */
    @Event(handlers = LandingPageEventHandler.class,
           historyConverter = RootHistoryConverter.class,
           name = SantaExchangeEventTypes.LANDING_PAGE)
    String showLandingPage();

    /** Show the external landing page. */
    @Event(forwardToModules = ExternalModule.class)
    void showExternalLandingPage();

    /** Show the admin landing page. */
    @Event(forwardToModules = AdminModule.class)
    void showAdminLandingPage();

    /** Show the internal landing page. */
    @Event(forwardToModules = InternalModule.class)
    void showInternalLandingPage();

    /** Show the application dashboard. */
    @Event(handlers = RedirectEventHandler.class)
    void showApplicationDashboard();

    /** Show the source code page. */
    @Event(handlers = RedirectEventHandler.class)
    void showSourceCode();

    /** Show the about pop-up. */
    @Event(handlers = RootPresenter.class)
    void showAboutPopup();

    /** Show the welcome pop-up. */
    @Event(handlers = RootPresenter.class)
    void showWelcomePopup();

    /** Show an error via the error pop-up. */
    @Event(handlers = RootPresenter.class)
    void showErrorPopup(ErrorDescription error);

    /** Show a "bookmark not found" error via the error pop-up. */
    @Event(handlers = RootPresenter.class)
    void showBookmarkNotFoundError();

    /** Show the bug report dialog. */
    @Event(forwardToModules = InternalModule.class)
    void showBugReportDialog();

    /** Show the RPC test page. */
    @Event(forwardToModules = ExternalModule.class,
           historyConverter = RootHistoryConverter.class,
           name = SantaExchangeEventTypes.RPC_TEST_PAGE)
    String showRpcTestPage();

}
