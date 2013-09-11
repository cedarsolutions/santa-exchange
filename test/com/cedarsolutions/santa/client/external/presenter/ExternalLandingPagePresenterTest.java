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
import com.cedarsolutions.santa.client.external.presenter.ExternalLandingPagePresenter.ContinueEventHandler;
import com.cedarsolutions.santa.client.external.presenter.ExternalLandingPagePresenter.LoginSelectorEventHandler;
import com.cedarsolutions.santa.client.external.view.IExternalLandingPageView;
import com.cedarsolutions.santa.client.junit.StubbedClientTestCase;
import com.cedarsolutions.santa.shared.domain.ClientSession;
import com.cedarsolutions.shared.domain.OpenIdProvider;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * Unit tests for ExternalLandingPagePresenter.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ExternalLandingPagePresenterTest extends StubbedClientTestCase {

    /** Test onShowExternalLandingPage() when the user is not logged in. */
    @Test public void testOnShowExternalLandingPageNotLoggedIn() {
        ArgumentCaptor<ContinueEventHandler> continueEventHandler = ArgumentCaptor.forClass(ContinueEventHandler.class);
        ArgumentCaptor<LoginSelectorEventHandler> loginSelectorEventHandler = ArgumentCaptor.forClass(LoginSelectorEventHandler.class);
        IsWidget viewWidget = mock(IsWidget.class);

        ExternalLandingPagePresenter presenter = createPresenter();
        when(presenter.getSession().isLoggedIn()).thenReturn(false);
        when(presenter.getView().getViewWidget()).thenReturn(viewWidget);

        presenter.onShowExternalLandingPage();
        verify(presenter.getView()).setIsLoggedIn(false);
        verify(presenter.getView()).setContinueEventHandler(continueEventHandler.capture());
        verify(presenter.getView()).setLoginSelectorEventHandler(loginSelectorEventHandler.capture());
        verify(presenter.getEventBus()).replaceModuleBody(viewWidget);
        assertSame(presenter, continueEventHandler.getValue().getParent());
        assertSame(presenter, loginSelectorEventHandler.getValue().getParent());
    }

    /** Test onShowExternalLandingPage() when the user is currently logged in. */
    @Test public void testOnShowExternalLandingPageLoggedIn() {
        ArgumentCaptor<ContinueEventHandler> continueEventHandler = ArgumentCaptor.forClass(ContinueEventHandler.class);
        ArgumentCaptor<LoginSelectorEventHandler> loginSelectorEventHandler = ArgumentCaptor.forClass(LoginSelectorEventHandler.class);
        IsWidget viewWidget = mock(IsWidget.class);

        ExternalLandingPagePresenter presenter = createPresenter();
        when(presenter.getSession().isLoggedIn()).thenReturn(true);
        when(presenter.getView().getViewWidget()).thenReturn(viewWidget);

        presenter.onShowExternalLandingPage();
        verify(presenter.getView()).setIsLoggedIn(true);
        verify(presenter.getView()).setContinueEventHandler(continueEventHandler.capture());
        verify(presenter.getView()).setLoginSelectorEventHandler(loginSelectorEventHandler.capture());
        verify(presenter.getEventBus()).replaceModuleBody(viewWidget);
        assertSame(presenter, continueEventHandler.getValue().getParent());
        assertSame(presenter, loginSelectorEventHandler.getValue().getParent());
    }

    /** Test ContinueEventHandler. */
    @Test public void testContinueEventHandler() {
        ExternalLandingPagePresenter presenter = createPresenter();
        ContinueEventHandler handler = new ContinueEventHandler(presenter);
        assertSame(presenter, handler.getParent());
        handler.handleEvent(null); // event is ignored
        verify(presenter.getEventBus()).showLandingPage();
    }

    /** Test LoginSelectorEventHandler. */
    @Test public void testLoginSelectorEventHandler() {
        ExternalLandingPagePresenter presenter = createPresenter();
        LoginSelectorEventHandler handler = new LoginSelectorEventHandler(presenter);
        assertSame(presenter, handler.getParent());
        when(presenter.getView().getSelectedProvider()).thenReturn(OpenIdProvider.MYOPENID);
        handler.handleEvent(null); // event is ignored
        verify(presenter.getEventBus()).showLoginPageForToken(OpenIdProvider.MYOPENID, SantaExchangeEventTypes.LANDING_PAGE);
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
