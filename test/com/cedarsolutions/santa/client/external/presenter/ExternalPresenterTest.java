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
package com.cedarsolutions.santa.client.external.presenter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import com.cedarsolutions.santa.client.external.ExternalEventBus;
import com.cedarsolutions.santa.client.external.view.IExternalView;
import com.cedarsolutions.santa.client.junit.StubbedClientTestCase;

/**
 * Unit tests for ExternalPresenter.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ExternalPresenterTest extends StubbedClientTestCase {

    /** Test the constructor. */
    @Test public void testConstructor() {
        ExternalPresenter presenter = createPresenter();
        assertNotNull(presenter);
    }

    /** Create a properly-mocked presenter, including everything that needs to be injected. */
    private static ExternalPresenter createPresenter() {
        ExternalEventBus eventBus = mock(ExternalEventBus.class);
        IExternalView view = mock(IExternalView.class);

        ExternalPresenter presenter = new ExternalPresenter();
        presenter.setEventBus(eventBus);
        presenter.setView(view);

        assertSame(eventBus, presenter.getEventBus());
        assertSame(view, presenter.getView());

        return presenter;
    }

}
