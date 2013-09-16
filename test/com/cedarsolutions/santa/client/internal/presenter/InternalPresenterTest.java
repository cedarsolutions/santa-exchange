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
package com.cedarsolutions.santa.client.internal.presenter;

import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.cedarsolutions.santa.client.common.presenter.SystemStateInjector;
import com.cedarsolutions.santa.client.internal.InternalEventBus;
import com.cedarsolutions.santa.client.internal.presenter.InternalPresenter.AboutEventHandler;
import com.cedarsolutions.santa.client.internal.presenter.InternalPresenter.AdminLandingPageEventHandler;
import com.cedarsolutions.santa.client.internal.presenter.InternalPresenter.BugReportEventHandler;
import com.cedarsolutions.santa.client.internal.presenter.InternalPresenter.LogoutEventHandler;
import com.cedarsolutions.santa.client.internal.presenter.InternalPresenter.SourceCodeEventHandler;
import com.cedarsolutions.santa.client.internal.view.IInternalView;
import com.cedarsolutions.santa.client.junit.StubbedClientTestCase;
import com.cedarsolutions.santa.shared.domain.ClientSession;
import com.cedarsolutions.shared.domain.FederatedUser;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * Unit tests for InternalPresenter.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class InternalPresenterTest extends StubbedClientTestCase {

    /** Test onStart(). */
    @Test public void testOnStart() {
        InternalPresenter presenter = createPresenter();
        presenter.onStart();  // just make sure it doesn't blow up
    }

    /** Test bind(). */
    @Test public void testBind() {
        InternalPresenter presenter = createPresenter();
        presenter.bind();
        verify(presenter.getView()).setAdminLandingPageEventHandler(any(AdminLandingPageEventHandler.class));
        verify(presenter.getView()).setAboutEventHandler(any(AboutEventHandler.class));
        verify(presenter.getView()).setBugReportEventHandler(any(BugReportEventHandler.class));
        verify(presenter.getView()).setSourceCodeEventHandler(any(SourceCodeEventHandler.class));
        verify(presenter.getView()).setLogoutEventHandler(any(LogoutEventHandler.class));
    }

    /** Test onReplaceModuleBody() for an admin user. */
    @Test public void testOnReplaceModuleBodyAdmin() {
        IsWidget viewWidget = mock(IsWidget.class);
        IsWidget contents = mock(IsWidget.class);

        FederatedUser currentUser = new FederatedUser();
        currentUser.setUserName("name");
        currentUser.setAdmin(true);

        InternalPresenter presenter = createPresenter();
        when(presenter.getSession().getCurrentUser()).thenReturn(currentUser);
        when(presenter.getView().getViewWidget()).thenReturn(viewWidget);

        presenter.onReplaceModuleBody(contents);
        verify(presenter.getView()).setEnableAdminLandingPage(true);
        verify(presenter.getView()).setCurrentUser(currentUser);
        verify(presenter.getView()).replaceModuleBody(contents);
        verify(presenter.getEventBus()).replaceRootBody(viewWidget);
    }

    /** Test onReplaceModuleBody() for a non-admin user. */
    @Test public void testOnReplaceModuleBodyNonAdmin() {
        IsWidget viewWidget = mock(IsWidget.class);
        IsWidget contents = mock(IsWidget.class);

        FederatedUser currentUser = new FederatedUser();
        currentUser.setUserName("name");
        currentUser.setAdmin(false);

        InternalPresenter presenter = createPresenter();
        when(presenter.getSession().getCurrentUser()).thenReturn(currentUser);
        when(presenter.getView().getViewWidget()).thenReturn(viewWidget);

        presenter.onReplaceModuleBody(contents);
        verify(presenter.getView()).setEnableAdminLandingPage(false);
        verify(presenter.getView()).setCurrentUser(currentUser);
        verify(presenter.getView()).replaceModuleBody(contents);
        verify(presenter.getEventBus()).replaceRootBody(viewWidget);
    }

    /** Test AdminLandingPageEventHandler. */
    @Test public void testAdminLandingPageEventHandler() {
        InternalPresenter presenter = createPresenter();
        AdminLandingPageEventHandler handler = new AdminLandingPageEventHandler(presenter);
        handler.handleEvent(null); // specific event doesn't matter
        verify(presenter.getEventBus()).showAdminLandingPage();
    }

    /** Test AboutEventHandler. */
    @Test public void testAboutEventHandler() {
        InternalPresenter presenter = createPresenter();
        AboutEventHandler handler = new AboutEventHandler(presenter);
        handler.handleEvent(null); // specific event doesn't matter
        verify(presenter.getEventBus()).showAboutPopup();
    }

    /** Test BugReportEventHandler. */
    @Test public void testBugReportEventHandler() {
        InternalPresenter presenter = createPresenter();
        BugReportEventHandler handler = new BugReportEventHandler(presenter);
        handler.handleEvent(null); // specific event doesn't matter
        verify(presenter.getEventBus()).showBugReportDialog();
    }

    /** Test SourceCodeEventHandler. */
    @Test public void testSourceCodeEventHandler() {
        InternalPresenter presenter = createPresenter();
        SourceCodeEventHandler handler = new SourceCodeEventHandler(presenter);
        handler.handleEvent(null); // specific event doesn't matter
        verify(presenter.getEventBus()).showSourceCode();
    }

    /** Test LogoutEventHandler. */
    @Test public void testLogoutEventHandler() {
        InternalPresenter presenter = createPresenter();
        LogoutEventHandler handler = new LogoutEventHandler(presenter);
        handler.handleEvent(null); // specific event doesn't matter
        verify(presenter.getEventBus()).logout();
    }

    /** Create a properly-mocked presenter, including everything that needs to be injected. */
    private static InternalPresenter createPresenter() {
        InternalEventBus eventBus = mock(InternalEventBus.class);
        IInternalView view = mock(IInternalView.class);
        ClientSession session = mock(ClientSession.class, RETURNS_DEEP_STUBS);  // so call1().call2().call3() works
        SystemStateInjector systemStateInjector = mock(SystemStateInjector.class);
        when(systemStateInjector.getSession()).thenReturn(session);

        InternalPresenter presenter = new InternalPresenter();
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
