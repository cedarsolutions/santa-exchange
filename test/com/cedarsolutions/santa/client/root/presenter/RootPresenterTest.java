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
package com.cedarsolutions.santa.client.root.presenter;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import com.cedarsolutions.santa.client.common.widget.WidgetUtils;
import com.cedarsolutions.santa.client.junit.StubbedClientTestCase;
import com.cedarsolutions.santa.client.root.RootEventBus;

/**
 * Unit tests for RootPresenter.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class RootPresenterTest extends StubbedClientTestCase {

    /** Test onStart(). */
    @Test public void testOnStart() {
        RootPresenter presenter = createPresenter();
        presenter.onStart();  // just make sure it works with no errors
    }

    /** Test onInit(). */
    @Test public void testOnInit() {
        RootPresenter presenter = createPresenter();
        presenter.onInit();
        verify(presenter.getEventBus()).showLandingPage();
    }

    /** Test onNotFound(). */
    @Test public void testOnNotFound() {
        RootPresenter presenter = createPresenter();
        presenter.onNotFound();
        InOrder order = Mockito.inOrder(presenter.getEventBus(), WidgetUtils.getInstance());
        order.verify(presenter.getEventBus()).clearHistory();
        order.verify(presenter.getEventBus()).showLandingPage();
        order.verify(presenter.getEventBus()).showBookmarkNotFoundError();
    }

    /*** Test onClearHistory(). */
    @Test public void testOnClearHistory() {
        RootPresenter presenter = createPresenter();
        presenter.onClearHistory();  // just make sure it works with no errors
    }

    /** Create a properly-mocked presenter, including everything that needs to be injected. */
    private static RootPresenter createPresenter() {
        RootEventBus eventBus = mock(RootEventBus.class);

        RootPresenter presenter = new RootPresenter();
        presenter.setEventBus(eventBus);

        assertSame(eventBus, presenter.getEventBus());

        return presenter;
    }
}
