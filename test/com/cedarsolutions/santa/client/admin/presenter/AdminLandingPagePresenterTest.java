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
package com.cedarsolutions.santa.client.admin.presenter;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import com.cedarsolutions.santa.client.admin.AdminEventBus;
import com.cedarsolutions.santa.client.admin.view.IAdminLandingPageView;
import com.cedarsolutions.santa.client.common.presenter.SystemStateInjector;
import com.cedarsolutions.santa.client.common.widget.WidgetUtils;
import com.cedarsolutions.santa.client.junit.StubbedClientTestCase;
import com.cedarsolutions.santa.shared.domain.ClientSession;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * Unit tests for AdminLandingPagePresenter.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class AdminLandingPagePresenterTest extends StubbedClientTestCase {

    /** Test onShowAdminLandingPage(), no welcome message. */
    @Test public void testOnShowAdminLandingPage1() {
        IsWidget viewWidget = mock(IsWidget.class);

        AdminLandingPagePresenter presenter = createPresenter();
        when(presenter.getView().getViewWidget()).thenReturn(viewWidget);

        // If it's not the first login, no welcome message is displayed
        when(presenter.getSession().getCurrentUser().isFirstLogin()).thenReturn(false);
        when(presenter.getSession().getCurrentUser().isAdmin()).thenReturn(false);
        presenter.onShowAdminLandingPage();
        InOrder order = Mockito.inOrder(presenter.getEventBus(), WidgetUtils.getInstance());
        order.verify(presenter.getEventBus()).selectHomeTab();
        order.verify(presenter.getEventBus(), never()).showWelcomePopup();
        verify(presenter.getSession().getCurrentUser(), never()).setFirstLogin(false);
    }

    /** Test onShowAdminLandingPage(), with welcome message. */
    @Test public void testOnShowAdminLandingPage2() {
        IsWidget viewWidget = mock(IsWidget.class);

        AdminLandingPagePresenter presenter = createPresenter();
        when(presenter.getView().getViewWidget()).thenReturn(viewWidget);

        // If it's their first login and they're an admin user, we'll show the welcome pop-up
        when(presenter.getSession().getCurrentUser().isFirstLogin()).thenReturn(true);
        when(presenter.getSession().getCurrentUser().isAdmin()).thenReturn(true);
        presenter.onShowAdminLandingPage();
        InOrder order = Mockito.inOrder(presenter.getEventBus(), WidgetUtils.getInstance());
        order.verify(presenter.getEventBus()).selectHomeTab();
        order.verify(presenter.getEventBus()).showWelcomePopup();
        verify(presenter.getSession().getCurrentUser()).setFirstLogin(false);
    }

    /** Test onShowAdminLandingPage(), no welcome message (non-admin). */
    @Test public void testOnShowAdminLandingPage3() {
        IsWidget viewWidget = mock(IsWidget.class);

        AdminLandingPagePresenter presenter = createPresenter();
        when(presenter.getView().getViewWidget()).thenReturn(viewWidget);

        // In the odd case where it the first login but they're not admin, we'll just reset the flag
        when(presenter.getSession().getCurrentUser().isFirstLogin()).thenReturn(true);
        when(presenter.getSession().getCurrentUser().isAdmin()).thenReturn(false);
        presenter.onShowAdminLandingPage();
        InOrder order = Mockito.inOrder(presenter.getEventBus(), WidgetUtils.getInstance());
        order.verify(presenter.getEventBus()).selectHomeTab();
        order.verify(presenter.getEventBus(), never()).showWelcomePopup();
        verify(presenter.getSession().getCurrentUser()).setFirstLogin(false);
    }

    /** Test selectHomeTab(). */
    @Test public void testSelectHomeTab() {
        IsWidget viewWidget = mock(IsWidget.class);

        AdminLandingPagePresenter presenter = createPresenter();
        when(presenter.getView().getViewWidget()).thenReturn(viewWidget);

        presenter.onSelectHomeTab();
        InOrder order = Mockito.inOrder(presenter.getEventBus(), presenter.getView());
        order.verify(presenter.getView()).selectHomeTab();
        order.verify(presenter.getEventBus()).replaceModuleBody(viewWidget);
    }

    /** Test selectAuditTab(). */
    @Test public void testSelectAuditTab() {
        IsWidget viewWidget = mock(IsWidget.class);

        AdminLandingPagePresenter presenter = createPresenter();
        when(presenter.getView().getViewWidget()).thenReturn(viewWidget);

        presenter.onSelectAuditTab();
        InOrder order = Mockito.inOrder(presenter.getEventBus(), presenter.getView());
        order.verify(presenter.getView()).selectAuditTab();
        order.verify(presenter.getEventBus()).replaceModuleBody(viewWidget);
    }

    /** Test selectUserTab(). */
    @Test public void testSelectUserTab() {
        IsWidget viewWidget = mock(IsWidget.class);

        AdminLandingPagePresenter presenter = createPresenter();
        when(presenter.getView().getViewWidget()).thenReturn(viewWidget);

        presenter.onSelectUserTab();
        InOrder order = Mockito.inOrder(presenter.getEventBus(), presenter.getView());
        order.verify(presenter.getView()).selectUserTab();
        order.verify(presenter.getEventBus()).replaceModuleBody(viewWidget);
    }

    /** Create a properly-mocked presenter, including everything that needs to be injected. */
    private static AdminLandingPagePresenter createPresenter() {
        AdminEventBus eventBus = mock(AdminEventBus.class);
        IAdminLandingPageView view = mock(IAdminLandingPageView.class);
        ClientSession session = mock(ClientSession.class, RETURNS_DEEP_STUBS);
        SystemStateInjector systemStateInjector = mock(SystemStateInjector.class);
        when(systemStateInjector.getSession()).thenReturn(session);

        AdminLandingPagePresenter presenter = new AdminLandingPagePresenter();
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
