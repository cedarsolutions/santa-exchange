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
package com.cedarsolutions.santa.client.internal.presenter;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import com.cedarsolutions.santa.client.common.presenter.SystemStateInjector;
import com.cedarsolutions.santa.client.common.widget.WidgetUtils;
import com.cedarsolutions.santa.client.internal.InternalEventBus;
import com.cedarsolutions.santa.client.internal.view.IInternalLandingPageView;
import com.cedarsolutions.santa.client.junit.StubbedClientTestCase;
import com.cedarsolutions.santa.shared.domain.ClientSession;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * Unit tests for InternalLandingPagePresenter.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class InternalLandingPagePresenterTest extends StubbedClientTestCase {

    /** Test onShowInternalLandingPage(), no welcome message. */
    @Test public void testOnShowInternalLandingPage1() {
        IsWidget viewWidget = mock(IsWidget.class);

        InternalLandingPagePresenter presenter = createPresenter();
        when(presenter.getView().getViewWidget()).thenReturn(viewWidget);

        // If it's not the first login, no welcome message is displayed
        when(presenter.getSession().getCurrentUser().isFirstLogin()).thenReturn(false);
        when(presenter.getSession().getCurrentUser().isAdmin()).thenReturn(false);

        presenter.onShowInternalLandingPage();
        InOrder order = Mockito.inOrder(presenter.getEventBus(), WidgetUtils.getInstance(), presenter.getSession().getCurrentUser());
        order.verify(presenter.getEventBus()).showExchangeListPage();
        order.verify(presenter.getSession().getCurrentUser(), never()).setFirstLogin(false);
        order.verify(presenter.getEventBus(), never()).showWelcomePopup();
    }

    /** Test onShowInternalLandingPage(), with welcome message. */
    @Test public void testOnShowInternalLandingPage2() {
        IsWidget viewWidget = mock(IsWidget.class);

        InternalLandingPagePresenter presenter = createPresenter();
        when(presenter.getView().getViewWidget()).thenReturn(viewWidget);

        // If it's their first login and they're an admin user, we'll show the welcome pop-up
        when(presenter.getSession().getCurrentUser().isFirstLogin()).thenReturn(true);
        when(presenter.getSession().getCurrentUser().isAdmin()).thenReturn(false);

        presenter.onShowInternalLandingPage();
        InOrder order = Mockito.inOrder(presenter.getEventBus(), WidgetUtils.getInstance(), presenter.getSession().getCurrentUser());
        order.verify(presenter.getEventBus()).showExchangeListPage();
        order.verify(presenter.getSession().getCurrentUser()).setFirstLogin(false);
        order.verify(presenter.getEventBus()).showWelcomePopup();
    }

    /** Test onShowInternalLandingPage(), no welcome message (admin). */
    @Test public void testOnShowInternalLandingPage3() {
        IsWidget viewWidget = mock(IsWidget.class);

        InternalLandingPagePresenter presenter = createPresenter();
        when(presenter.getView().getViewWidget()).thenReturn(viewWidget);

        // In the odd case where it the first login but they're admin, we'll just reset the flag
        when(presenter.getSession().getCurrentUser().isFirstLogin()).thenReturn(true);
        when(presenter.getSession().getCurrentUser().isAdmin()).thenReturn(true);

        presenter.onShowInternalLandingPage();
        InOrder order = Mockito.inOrder(presenter.getEventBus(), WidgetUtils.getInstance(), presenter.getSession().getCurrentUser());
        order.verify(presenter.getEventBus()).showExchangeListPage();
        order.verify(presenter.getEventBus(), never()).showWelcomePopup();
        order.verify(presenter.getSession().getCurrentUser()).setFirstLogin(false);
    }

    /** Test onSelectExchangeListTab(). */
    @Test public void testOnSelectExchangeListTab() {
        IsWidget viewWidget = mock(IsWidget.class);

        InternalLandingPagePresenter presenter = createPresenter();
        when(presenter.getView().getViewWidget()).thenReturn(viewWidget);

        presenter.onSelectExchangeListTab();
        InOrder order = Mockito.inOrder(presenter.getView(), presenter.getEventBus());
        order.verify(presenter.getView()).selectExchangeListTab();
        order.verify(presenter.getEventBus()).replaceModuleBody(viewWidget);
    }

    /** Test onSelectEditExchangeTab(). */
    @Test public void testOnSelectEditExchangeTab() {
        IsWidget viewWidget = mock(IsWidget.class);

        InternalLandingPagePresenter presenter = createPresenter();
        when(presenter.getView().getViewWidget()).thenReturn(viewWidget);

        presenter.onSelectEditExchangeTab();
        InOrder order = Mockito.inOrder(presenter.getView(), presenter.getEventBus());
        order.verify(presenter.getView()).selectEditExchangeTab();
        order.verify(presenter.getEventBus()).replaceModuleBody(viewWidget);
    }

    /** Test onSelectEditParticipantTab(). */
    @Test public void testOnSelectEditParticipantTab() {
        IsWidget viewWidget = mock(IsWidget.class);

        InternalLandingPagePresenter presenter = createPresenter();
        when(presenter.getView().getViewWidget()).thenReturn(viewWidget);

        presenter.onSelectEditParticipantTab();
        InOrder order = Mockito.inOrder(presenter.getView(), presenter.getEventBus());
        order.verify(presenter.getView()).selectEditParticipantTab();
        order.verify(presenter.getEventBus()).replaceModuleBody(viewWidget);
    }

    /** Create a properly-mocked presenter, including everything that needs to be injected. */
    private static InternalLandingPagePresenter createPresenter() {
        InternalEventBus eventBus = mock(InternalEventBus.class);
        IInternalLandingPageView view = mock(IInternalLandingPageView.class);
        ClientSession session = mock(ClientSession.class, RETURNS_DEEP_STUBS);
        SystemStateInjector systemStateInjector = mock(SystemStateInjector.class);
        when(systemStateInjector.getSession()).thenReturn(session);

        InternalLandingPagePresenter presenter = new InternalLandingPagePresenter();
        presenter.setEventBus(eventBus);
        presenter.setView(view);
        presenter.setSystemStateInjector(systemStateInjector);

        assertSame(eventBus, presenter.getEventBus());
        assertSame(view, presenter.getView());
        assertSame(systemStateInjector, presenter.getSystemStateInjector());
        assertSame(session, presenter.getSession());

        return presenter;
    }

}
