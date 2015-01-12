/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 *              C E D A R
 *          S O L U T I O N S       "Software done right."
 *           S O F T W A R E
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2013,2015 Kenneth J. Pronovici.
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
package com.cedarsolutions.santa.client.external.presenter;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.cedarsolutions.santa.client.SantaExchangeEventTypes;
import com.cedarsolutions.santa.client.common.presenter.SystemStateInjector;
import com.cedarsolutions.santa.client.external.ExternalEventBus;
import com.cedarsolutions.santa.client.external.presenter.ExternalLandingPagePresenter.LoginEventHandler;
import com.cedarsolutions.santa.client.external.view.IExternalLandingPageView;
import com.cedarsolutions.santa.client.junit.StubbedClientTestCase;
import com.cedarsolutions.santa.shared.domain.ClientSession;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * Unit tests for ExternalLandingPagePresenter.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ExternalLandingPagePresenterTest extends StubbedClientTestCase {

    /** Test onShowExternalLandingPage() when the user is not logged in. */
    @Test public void testOnShowExternalLandingPageNotLoggedIn() {
        ArgumentCaptor<LoginEventHandler> loginEventHandler = ArgumentCaptor.forClass(LoginEventHandler.class);
        IsWidget viewWidget = mock(IsWidget.class);

        ExternalLandingPagePresenter presenter = createPresenter();
        when(presenter.getSession().isLoggedIn()).thenReturn(false);
        when(presenter.getView().getViewWidget()).thenReturn(viewWidget);

        presenter.onShowExternalLandingPage();
        verify(presenter.getView()).setLoginEventHandler(loginEventHandler.capture());
        verify(presenter.getEventBus()).replaceModuleBody(viewWidget);
        assertSame(presenter, loginEventHandler.getValue().getParent());
    }

    /** Test onShowExternalLandingPage() when the user is currently logged in. */
    @Test public void testOnShowExternalLandingPageLoggedIn() {
        ArgumentCaptor<LoginEventHandler> loginEventHandler = ArgumentCaptor.forClass(LoginEventHandler.class);
        IsWidget viewWidget = mock(IsWidget.class);

        ExternalLandingPagePresenter presenter = createPresenter();
        when(presenter.getSession().isLoggedIn()).thenReturn(true);
        when(presenter.getView().getViewWidget()).thenReturn(viewWidget);

        presenter.onShowExternalLandingPage();
        verify(presenter.getView()).setLoginEventHandler(loginEventHandler.capture());
        verify(presenter.getEventBus()).replaceModuleBody(viewWidget);
        assertSame(presenter, loginEventHandler.getValue().getParent());
    }

    /** Test LoginEventHandler when the user is logged in. */
    @Test public void testLoginEventHandlerLoggedIn() {
        ExternalLandingPagePresenter presenter = createPresenter();
        when(presenter.getSystemStateInjector().getSession().isLoggedIn()).thenReturn(true);

        LoginEventHandler handler = new LoginEventHandler(presenter);
        assertSame(presenter, handler.getParent());

        handler.handleEvent(null); // event is ignored
        verify(presenter.getEventBus()).showLandingPage();
    }

    /** Test LoginEventHandler when the user is not logged in. */
    @Test public void testLoginEventHandlerNotLoggedIn() {
        ExternalLandingPagePresenter presenter = createPresenter();
        when(presenter.getSystemStateInjector().getSession().isLoggedIn()).thenReturn(false);

        LoginEventHandler handler = new LoginEventHandler(presenter);
        assertSame(presenter, handler.getParent());

        handler.handleEvent(null); // event is ignored
        verify(presenter.getEventBus()).showGoogleAccountsLoginPageForToken(SantaExchangeEventTypes.LANDING_PAGE);
    }

    /** Create a properly-mocked presenter, including everything that needs to be injected. */
    private static ExternalLandingPagePresenter createPresenter() {
        ClientSession session = mock(ClientSession.class);
        SystemStateInjector systemStateInjector = mock(SystemStateInjector.class);
        when(systemStateInjector.getSession()).thenReturn(session);
        ExternalEventBus eventBus = mock(ExternalEventBus.class);
        IExternalLandingPageView view = mock(IExternalLandingPageView.class);

        ExternalLandingPagePresenter presenter = new ExternalLandingPagePresenter();
        presenter.setSystemStateInjector(systemStateInjector);
        presenter.setEventBus(eventBus);
        presenter.setView(view);

        assertSame(systemStateInjector, presenter.getSystemStateInjector());
        assertSame(session, presenter.getSession());
        assertSame(eventBus, presenter.getEventBus());
        assertSame(view, presenter.getView());

        return presenter;
    }

}
