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
package com.cedarsolutions.santa.client.external.presenter;

import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.cedarsolutions.santa.client.external.ExternalEventBus;
import com.cedarsolutions.santa.client.external.presenter.AccountLockedPagePresenter.ContinueEventHandler;
import com.cedarsolutions.santa.client.external.view.IAccountLockedPageView;
import com.cedarsolutions.santa.client.junit.StubbedClientTestCase;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * Unit tests for AccountLockedPagePresenter.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class AccountLockedPagePresenterTest extends StubbedClientTestCase {

    /** Test onShowAccountLockedPage(). */
    @Test public void testOnShowAccountLockedPage() {
        IsWidget viewWidget = mock(IsWidget.class);

        AccountLockedPagePresenter presenter = createPresenter();
        when(presenter.getView().getViewWidget()).thenReturn(viewWidget);
        presenter.onShowAccountLockedPage();

        verify(presenter.getView()).setContinueEventHandler(isA(ContinueEventHandler.class));
        verify(presenter.getEventBus()).replaceModuleBody(viewWidget);
    }

    /** Test ContinueEventHandler. */
    @Test public void testContinueEventHandler() {
        ExternalEventBus eventBus = mock(ExternalEventBus.class);
        AccountLockedPagePresenter parent = mock(AccountLockedPagePresenter.class);

        when(parent.getEventBus()).thenReturn(eventBus);

        ContinueEventHandler handler = new ContinueEventHandler(parent);
        assertSame(parent, handler.getParent());

        handler.handleEvent(null);  // event is ignored
        verify(parent.getEventBus()).logout();
    }

    /** Create a properly-mocked presenter, including everything that needs to be injected. */
    private static AccountLockedPagePresenter createPresenter() {
        ExternalEventBus eventBus = mock(ExternalEventBus.class);
        IAccountLockedPageView view = mock(IAccountLockedPageView.class);

        AccountLockedPagePresenter presenter = new AccountLockedPagePresenter();
        presenter.setEventBus(eventBus);
        presenter.setView(view);

        assertSame(eventBus, presenter.getEventBus());
        assertSame(view, presenter.getView());

        return presenter;
    }

}
