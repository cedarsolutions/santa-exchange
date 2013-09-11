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
package com.cedarsolutions.santa.client.external.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.cedarsolutions.client.gwt.event.UnifiedEvent;
import com.cedarsolutions.client.gwt.event.UnifiedEventType;
import com.cedarsolutions.client.gwt.event.ViewEventHandler;
import com.cedarsolutions.santa.client.external.view.AccountLockedPageView.ContinueClickHandler;
import com.cedarsolutions.santa.client.junit.StubbedClientTestCase;
import com.google.gwt.event.dom.client.ClickEvent;

/**
 * Unit tests for AccountLockedPageView.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class AccountLockedPageViewTest extends StubbedClientTestCase {

    /** Test ContinueClickHandler. */
    @Test public void testContinueClickHandler() {
        ClickEvent event = mock(ClickEvent.class);

        AccountLockedPageView view = mock(AccountLockedPageView.class);
        ContinueClickHandler handler = new ContinueClickHandler(view);
        assertSame(view, handler.getParent());

        when(view.getContinueEventHandler()).thenReturn(null);
        handler.onClick(event);  // just make sure it doesn't blow up

        ArgumentCaptor<UnifiedEvent> captor = ArgumentCaptor.forClass(UnifiedEvent.class);
        ViewEventHandler continueEventHandler = mock(ViewEventHandler.class);
        when(view.getContinueEventHandler()).thenReturn(continueEventHandler);
        handler.onClick(event);
        verify(continueEventHandler).handleEvent(captor.capture());
        assertEquals(UnifiedEventType.CLICK_EVENT, captor.getValue().getEventType());
        assertSame(event, captor.getValue().getClickEvent());
    }

}
