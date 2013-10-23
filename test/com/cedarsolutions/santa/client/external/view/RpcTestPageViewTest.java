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
import com.cedarsolutions.santa.client.external.view.RpcTestPageView.AdminRpcClickHandler;
import com.cedarsolutions.santa.client.external.view.RpcTestPageView.EnabledAdminRpcClickHandler;
import com.cedarsolutions.santa.client.external.view.RpcTestPageView.EnabledUserRpcClickHandler;
import com.cedarsolutions.santa.client.external.view.RpcTestPageView.UnprotectedRpcClickHandler;
import com.cedarsolutions.santa.client.external.view.RpcTestPageView.UserRpcClickHandler;
import com.cedarsolutions.santa.client.junit.StubbedClientTestCase;
import com.google.gwt.event.dom.client.ClickEvent;

/**
 * Unit tests for RpcTestPageView.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class RpcTestPageViewTest extends StubbedClientTestCase {

    /** Test UnprotectedRpcClickHandler. */
    @Test public void testUnprotectedRpcClickHandler() {
        ClickEvent event = mock(ClickEvent.class);

        RpcTestPageView view = mock(RpcTestPageView.class);
        UnprotectedRpcClickHandler handler = new UnprotectedRpcClickHandler(view);
        assertSame(view, handler.getParent());

        when(view.getUnprotectedRpcHandler()).thenReturn(null);
        handler.onClick(event);  // just make sure it doesn't blow up

        ArgumentCaptor<UnifiedEvent> captor = ArgumentCaptor.forClass(UnifiedEvent.class);
        ViewEventHandler eventHandler = mock(ViewEventHandler.class);
        when(view.getUnprotectedRpcHandler()).thenReturn(eventHandler);
        handler.onClick(event);
        verify(eventHandler).handleEvent(captor.capture());
        assertEquals(UnifiedEventType.CLICK_EVENT, captor.getValue().getEventType());
        assertSame(event, captor.getValue().getClickEvent());
    }

    /** Test UserRpcClickHandler. */
    @Test public void testUserRpcClickHandler() {
        ClickEvent event = mock(ClickEvent.class);

        RpcTestPageView view = mock(RpcTestPageView.class);
        UserRpcClickHandler handler = new UserRpcClickHandler(view);
        assertSame(view, handler.getParent());

        when(view.getUserRpcHandler()).thenReturn(null);
        handler.onClick(event);  // just make sure it doesn't blow up

        ArgumentCaptor<UnifiedEvent> captor = ArgumentCaptor.forClass(UnifiedEvent.class);
        ViewEventHandler eventHandler = mock(ViewEventHandler.class);
        when(view.getUserRpcHandler()).thenReturn(eventHandler);
        handler.onClick(event);
        verify(eventHandler).handleEvent(captor.capture());
        assertEquals(UnifiedEventType.CLICK_EVENT, captor.getValue().getEventType());
        assertSame(event, captor.getValue().getClickEvent());
    }

    /** Test AdminRpcClickHandler. */
    @Test public void testAdminRpcClickHandler() {
        ClickEvent event = mock(ClickEvent.class);

        RpcTestPageView view = mock(RpcTestPageView.class);
        AdminRpcClickHandler handler = new AdminRpcClickHandler(view);
        assertSame(view, handler.getParent());

        when(view.getAdminRpcHandler()).thenReturn(null);
        handler.onClick(event);  // just make sure it doesn't blow up

        ArgumentCaptor<UnifiedEvent> captor = ArgumentCaptor.forClass(UnifiedEvent.class);
        ViewEventHandler eventHandler = mock(ViewEventHandler.class);
        when(view.getAdminRpcHandler()).thenReturn(eventHandler);
        handler.onClick(event);
        verify(eventHandler).handleEvent(captor.capture());
        assertEquals(UnifiedEventType.CLICK_EVENT, captor.getValue().getEventType());
        assertSame(event, captor.getValue().getClickEvent());
    }

    /** Test EnabledUserRpcClickHandler. */
    @Test public void testEnabledUserRpcClickHandler() {
        ClickEvent event = mock(ClickEvent.class);

        RpcTestPageView view = mock(RpcTestPageView.class);
        EnabledUserRpcClickHandler handler = new EnabledUserRpcClickHandler(view);
        assertSame(view, handler.getParent());

        when(view.getEnabledUserRpcHandler()).thenReturn(null);
        handler.onClick(event);  // just make sure it doesn't blow up

        ArgumentCaptor<UnifiedEvent> captor = ArgumentCaptor.forClass(UnifiedEvent.class);
        ViewEventHandler eventHandler = mock(ViewEventHandler.class);
        when(view.getEnabledUserRpcHandler()).thenReturn(eventHandler);
        handler.onClick(event);
        verify(eventHandler).handleEvent(captor.capture());
        assertEquals(UnifiedEventType.CLICK_EVENT, captor.getValue().getEventType());
        assertSame(event, captor.getValue().getClickEvent());
    }

    /** Test EnabledAdminRpcClickHandler. */
    @Test public void testEnabledAdminRpcClickHandler() {
        ClickEvent event = mock(ClickEvent.class);

        RpcTestPageView view = mock(RpcTestPageView.class);
        EnabledAdminRpcClickHandler handler = new EnabledAdminRpcClickHandler(view);
        assertSame(view, handler.getParent());

        when(view.getEnabledAdminRpcHandler()).thenReturn(null);
        handler.onClick(event);  // just make sure it doesn't blow up

        ArgumentCaptor<UnifiedEvent> captor = ArgumentCaptor.forClass(UnifiedEvent.class);
        ViewEventHandler eventHandler = mock(ViewEventHandler.class);
        when(view.getEnabledAdminRpcHandler()).thenReturn(eventHandler);
        handler.onClick(event);
        verify(eventHandler).handleEvent(captor.capture());
        assertEquals(UnifiedEventType.CLICK_EVENT, captor.getValue().getEventType());
        assertSame(event, captor.getValue().getClickEvent());
    }

}
