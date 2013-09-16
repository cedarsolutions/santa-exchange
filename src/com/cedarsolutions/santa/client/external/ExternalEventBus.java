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
package com.cedarsolutions.santa.client.external;

import com.cedarsolutions.client.gwt.module.ModuleEventBus;
import com.cedarsolutions.santa.client.SantaExchangeCustomLogger;
import com.cedarsolutions.santa.client.SantaExchangeEventTypes;
import com.cedarsolutions.santa.client.SantaExchangeGinModule;
import com.cedarsolutions.santa.client.external.presenter.AccountLockedPagePresenter;
import com.cedarsolutions.santa.client.external.presenter.ExternalClientSessionEventHandler;
import com.cedarsolutions.santa.client.external.presenter.ExternalLandingPagePresenter;
import com.cedarsolutions.santa.client.external.presenter.ExternalPresenter;
import com.cedarsolutions.santa.client.external.presenter.LoginRequiredPagePresenter;
import com.cedarsolutions.shared.domain.OpenIdProvider;
import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Debug.LogLevel;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Filters;

/**
 * Event bus for events that are external (public-facing).
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@Filters(filterClasses = ExternalEventFilter.class)
@Events(startPresenter = ExternalLandingPagePresenter.class,
        module = ExternalModule.class,
        ginModules = { SantaExchangeGinModule.class, ExternalGinModule.class })
@Debug(logLevel = LogLevel.DETAILED, logger = SantaExchangeCustomLogger.class)
public interface ExternalEventBus extends ModuleEventBus {

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
    @Event(handlers = ExternalPresenter.class)
    void replaceModuleBody(IsWidget contents);

    /** Clear all history maintained by the framework. */
    @Event(forwardToParent = true)
    void clearHistory();

    /** Initialize the current session. */
    @Override
    @Event(handlers = ExternalClientSessionEventHandler.class)
    void initializeSession(String eventType, Object[] params);

    /** Clear the current session. */
    @Override
    @Event(handlers = ExternalClientSessionEventHandler.class)
    void clearSession();

    /** Show the landing page, properly redirecting to the right place based on the user. */
    @Event(forwardToParent = true)
    String showLandingPage();

    /** Show the external landing page. */
    @Event(handlers = ExternalLandingPagePresenter.class,
           historyConverter = ExternalHistoryConverter.class,
           name = SantaExchangeEventTypes.EXTERNAL_LANDING_PAGE)
    void showExternalLandingPage();

    /**
     * Show the "login required" page.
     * @param destinationUrl  Destination URL to redirect to after login
     */
    @Event(handlers = LoginRequiredPagePresenter.class)
    void showLoginRequiredPage(String destinationUrl);

    /** Show the "account locked" page. */
    @Event(handlers = AccountLockedPagePresenter.class)
    void showAccountLockedPage();

    /**
     * Show the login page for a particular destination token.
     * @param openIdProvider   Open ID provider that will be used
     * @param destinationToken Destination token (page) to redirect to after login
     */
    @Event(forwardToParent = true)
    void showLoginPageForToken(OpenIdProvider openIdProvider, String destinationToken);

    /**
     * Show the login page for a particular destination URL.
     * @param openIdProvider   Open ID provider that will be used
     * @param destinationUrl   Destination URL to redirect to after login
     */
    @Event(forwardToParent = true)
    void showLoginPageForUrl(OpenIdProvider openIdProvider, String destinationUrl);

    /** Log out the current user. */
    @Event(forwardToParent = true)
    void logout();

}
