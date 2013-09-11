/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 *              C E D A R
 *          S O L U T I O N S       "Software done right."
 *           S O F T W A R E
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2011-2012 Kenneth J. Pronovici.
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import org.junit.Test;

import com.cedarsolutions.santa.client.admin.AdminEventBus;
import com.cedarsolutions.santa.client.admin.view.IHomeTabView;
import com.cedarsolutions.santa.client.junit.StubbedClientTestCase;
import com.mvp4g.client.event.BaseEventBus;

/**
 * Unit tests for HomeTabPresenter.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class HomeTabPresenterTest extends StubbedClientTestCase {

    /** Test onStart(). */
    @Test public void testOnStart() {
        HomeTabPresenter presenter = createPresenter();
        presenter.onStart();  // just make sure it doesn't blow up
    }

    /** Test bind(). */
    @Test public void testBind() {
        HomeTabPresenter presenter = createPresenter();
        when(presenter.getEventBus().selectHomeTab()).thenReturn("token");
        presenter.bind();
        verify(presenter.getView()).setHistoryToken("token");
    }

    /** Create a properly-mocked presenter, including everything that needs to be injected. */
    private static HomeTabPresenter createPresenter() {
        // Our mock needs to extend BaseEventBus and implement AdminEventBus, otherwise getTokenGenerator() doesn't work
        AdminEventBus eventBus = (AdminEventBus) mock(BaseEventBus.class, withSettings().extraInterfaces(AdminEventBus.class));

        IHomeTabView view = mock(IHomeTabView.class);

        HomeTabPresenter presenter = new HomeTabPresenter();
        presenter.setEventBus(eventBus);
        presenter.setView(view);

        assertSame(eventBus, presenter.getEventBus());
        assertSame(view, presenter.getView());

        return presenter;
    }
}
