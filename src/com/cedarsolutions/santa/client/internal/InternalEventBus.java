/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 *              C E D A R
 *          S O L U T I O N S       "Software done right."
 *           S O F T W A R E
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2011-2013 Kenneth J. Pronovici.
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
package com.cedarsolutions.santa.client.internal;

import com.cedarsolutions.client.gwt.module.ModuleEventBus;
import com.cedarsolutions.santa.client.SantaExchangeCustomLogger;
import com.cedarsolutions.santa.client.SantaExchangeEventTypes;
import com.cedarsolutions.santa.client.SantaExchangeGinModule;
import com.cedarsolutions.santa.client.internal.presenter.BugReportDialogPresenter;
import com.cedarsolutions.santa.client.internal.presenter.EditExchangeTabPresenter;
import com.cedarsolutions.santa.client.internal.presenter.EditParticipantTabPresenter;
import com.cedarsolutions.santa.client.internal.presenter.ExchangeListTabPresenter;
import com.cedarsolutions.santa.client.internal.presenter.InternalClientSessionEventHandler;
import com.cedarsolutions.santa.client.internal.presenter.InternalLandingPagePresenter;
import com.cedarsolutions.santa.client.internal.presenter.InternalPresenter;
import com.cedarsolutions.santa.shared.domain.exchange.Participant;
import com.cedarsolutions.santa.shared.domain.exchange.ParticipantSet;
import com.cedarsolutions.shared.domain.ErrorDescription;
import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Debug.LogLevel;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Filters;
import com.mvp4g.client.annotation.Start;

/**
 * Event bus for events that require a logged in user.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@Filters(filterClasses = InternalEventFilter.class)
@Events(startPresenter = InternalLandingPagePresenter.class,
        module = InternalModule.class,
        ginModules = { SantaExchangeGinModule.class, InternalGinModule.class })
@Debug(logLevel = LogLevel.DETAILED, logger = SantaExchangeCustomLogger.class)
public interface InternalEventBus extends ModuleEventBus {

    /** Handle the module start event. */
    @Start
    @Event(handlers = { InternalPresenter.class,
                        ExchangeListTabPresenter.class,
                        EditExchangeTabPresenter.class,
                        EditParticipantTabPresenter.class })
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
    @Event(handlers = InternalPresenter.class)
    void replaceModuleBody(IsWidget contents);

    /** Clear all history maintained by the framework. */
    @Event(forwardToParent = true)
    void clearHistory();

    /** Initialize the current session. */
    @Override
    @Event(handlers = InternalClientSessionEventHandler.class)
    void initializeSession(String eventType, Object[] params);

    /** Clear the current session. */
    @Override
    @Event(handlers = InternalClientSessionEventHandler.class)
    void clearSession();

    /** Show the landing page, properly redirecting to the right place based on the user. */
    @Event(forwardToParent = true)
    String showLandingPage();

    /** Show the admin landing page. */
    @Event(forwardToParent = true)
    void showAdminLandingPage();

    /** Show the external landing page. */
    @Event(forwardToParent = true)
    void showExternalLandingPage();

    /** Show the internal landing page. */
    @Event(handlers = InternalLandingPagePresenter.class,
           historyConverter = InternalHistoryConverter.class,
           name = SantaExchangeEventTypes.INTERNAL_LANDING_PAGE)
    void showInternalLandingPage();

    /** Show the about pop-up. */
    @Event(forwardToParent = true)
    void showAboutPopup();

    /** Show the welcome pop-up. */
    @Event(forwardToParent = true)
    void showWelcomePopup();

    /** Show an error via the error pop-up. */
    @Event(forwardToParent = true)
    void showErrorPopup(ErrorDescription error);

    /** Show a "bookmark not found" error via the error pop-up. */
    @Event(forwardToParent = true)
    void showBookmarkNotFoundError();

    /** Show the source code page. */
    @Event(forwardToParent = true)
    void showSourceCode();

    /** Show the bug report dialog. */
    @Event(handlers = BugReportDialogPresenter.class)
    void showBugReportDialog();

    /**
     * Show the "login required" page.
     * @param destinationUrl  Destination URL to redirect to after login
     */
    @Event(forwardToParent = true)
    void showLoginRequiredPage(String destinationUrl);

    /** Show the exchange list page. */
    @Event(handlers = ExchangeListTabPresenter.class,
           historyConverter = InternalHistoryConverter.class,
           name = SantaExchangeEventTypes.INTERNAL_EXCHANGE_LIST)
    void showExchangeListPage();

    /**
     * Show the edit exchange page for an exchange identified by id.
     * @param exchangeId  Id of the exchange to edit
     */
    @Event(handlers = EditExchangeTabPresenter.class,
           historyConverter = InternalHistoryConverter.class,
           name = SantaExchangeEventTypes.INTERNAL_EDIT_EXCHANGE)
    void showEditExchangePage(long exchangeId);

    /**
     * Show the edit participant page.
     * @param participant   Participant to edit, already found the current exchange
     * @param isNew         Whether this is a new participant
     * @param participants  List of all participants in this exchange
     */
    @Event(handlers = EditParticipantTabPresenter.class,
           historyConverter = InternalHistoryConverter.class,
           name = SantaExchangeEventTypes.INTERNAL_EDIT_PARTICIPANT)
    void showEditParticipantPage(Participant participant, boolean isNew, ParticipantSet participants);

    /** Edit the current exchange via the exchange edit manager. */
    @Event(handlers = EditExchangeTabPresenter.class)
    void editCurrentExchange();

    /** Log out the current user. */
    @Event(forwardToParent = true)
    void logout();

    /** Lock the user out of the application. */
    @Event(forwardToParent = true)
    void lockOutUser();

    /** Select the exchange list tab on the landing page. */
    @Event(handlers = InternalLandingPagePresenter.class)
    void selectExchangeListTab();

    /** Select the edit exchange tab on the landing page. */
    @Event(handlers = InternalLandingPagePresenter.class)
    void selectEditExchangeTab();

    /** Select the edit participant tab on the landing page. */
    @Event(handlers = InternalLandingPagePresenter.class)
    void selectEditParticipantTab();
}
