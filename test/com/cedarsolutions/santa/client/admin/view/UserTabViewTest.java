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
package com.cedarsolutions.santa.client.admin.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.cedarsolutions.client.gwt.event.UnifiedEvent;
import com.cedarsolutions.client.gwt.event.UnifiedEventType;
import com.cedarsolutions.client.gwt.event.ViewEventHandler;
import com.cedarsolutions.santa.client.admin.view.UserTabView.AdminColumn;
import com.cedarsolutions.santa.client.admin.view.UserTabView.ClearClickHandler;
import com.cedarsolutions.santa.client.admin.view.UserTabView.DeleteClickHandler;
import com.cedarsolutions.santa.client.admin.view.UserTabView.DeleteConfirmHandler;
import com.cedarsolutions.santa.client.admin.view.UserTabView.LastLoginColumn;
import com.cedarsolutions.santa.client.admin.view.UserTabView.LockClickHandler;
import com.cedarsolutions.santa.client.admin.view.UserTabView.LockedColumn;
import com.cedarsolutions.santa.client.admin.view.UserTabView.LoginsColumn;
import com.cedarsolutions.santa.client.admin.view.UserTabView.OpenIdProviderColumn;
import com.cedarsolutions.santa.client.admin.view.UserTabView.RefreshClickHandler;
import com.cedarsolutions.santa.client.admin.view.UserTabView.RegistrationDateColumn;
import com.cedarsolutions.santa.client.admin.view.UserTabView.SortHandler;
import com.cedarsolutions.santa.client.admin.view.UserTabView.UnlockClickHandler;
import com.cedarsolutions.santa.client.admin.view.UserTabView.UserIdColumn;
import com.cedarsolutions.santa.client.admin.view.UserTabView.UserNameColumn;
import com.cedarsolutions.santa.client.common.widget.ConfirmationPopup;
import com.cedarsolutions.santa.client.common.widget.WidgetConstants;
import com.cedarsolutions.santa.client.junit.StubbedClientTestCase;
import com.cedarsolutions.santa.shared.domain.user.RegisteredUser;
import com.cedarsolutions.santa.shared.domain.user.RegisteredUserSortColumn;
import com.cedarsolutions.shared.domain.OpenIdProvider;
import com.cedarsolutions.util.DateUtils;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent;

/**
 * Unit tests for UserTabView.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class UserTabViewTest extends StubbedClientTestCase {

    /** Test UserIdColumn. */
    @Test public void testUserIdColumn() {
        RegisteredUser item = mock(RegisteredUser.class);

        UserIdColumn column = new UserIdColumn();
        assertEquals(RegisteredUserSortColumn.USER_ID.toString(), column.getName());
        assertTrue(column.getCell() instanceof TextCell);
        assertTrue(column.isSortable());

        assertEquals("", column.getValue(null));

        when(item.getUserId()).thenReturn(null);
        assertEquals("", column.getValue(item));

        when(item.getUserId()).thenReturn("user");
        assertEquals("user", column.getValue(item));
    }

    /** Test UserNameColumn. */
    @Test public void testUserNameColumn() {
        RegisteredUser item = mock(RegisteredUser.class);

        UserNameColumn column = new UserNameColumn();
        assertEquals(RegisteredUserSortColumn.USER_NAME.toString(), column.getName());
        assertTrue(column.getCell() instanceof TextCell);
        assertTrue(column.isSortable());

        assertEquals("", column.getValue(null));

        when(item.getUserName()).thenReturn(null);
        assertEquals("", column.getValue(item));

        when(item.getUserName()).thenReturn("user");
        assertEquals("user", column.getValue(item));
    }

    /** Test RegistrationDateColumn. */
    @Test public void testRegistrationDateColumn() {
        RegisteredUser item = mock(RegisteredUser.class);

        RegistrationDateColumn column = new RegistrationDateColumn();
        assertEquals(RegisteredUserSortColumn.REGISTRATION_DATE.toString(), column.getName());
        assertTrue(column.getCell() instanceof TextCell);
        assertTrue(column.isSortable());

        assertEquals("", column.getValue(null));

        when(item.getRegistrationDate()).thenReturn(null);
        assertEquals("", column.getValue(item));

        when(item.getRegistrationDate()).thenReturn(DateUtils.createDate(2011, 12, 14, 8, 32));
        assertEquals("2011-12-14", column.getValue(item));
    }

    /** Test OpenIdProviderColumn. */
    @Test public void testOpenIdProviderColumn() {
        WidgetConstants constants = GWT.create(WidgetConstants.class);
        RegisteredUser item = mock(RegisteredUser.class);

        OpenIdProviderColumn column = new OpenIdProviderColumn();
        assertEquals(RegisteredUserSortColumn.OPEN_ID_PROVIDER.toString(), column.getName());
        assertTrue(column.getCell() instanceof TextCell);
        assertTrue(column.isSortable());

        assertEquals("", column.getValue(null));

        when(item.getOpenIdProvider()).thenReturn(null);
        assertEquals("", column.getValue(item));

        when(item.getOpenIdProvider()).thenReturn(OpenIdProvider.GOOGLE);
        assertEquals(constants.openId_Google(), column.getValue(item));
    }

    /** Test LoginsColumn. */
    @Test public void testLoginsColumn() {
        RegisteredUser item = mock(RegisteredUser.class);

        LoginsColumn column = new LoginsColumn();
        assertEquals(RegisteredUserSortColumn.LOGINS.toString(), column.getName());
        assertTrue(column.getCell() instanceof TextCell);
        assertTrue(column.isSortable());

        assertEquals("", column.getValue(null));

        when(item.getLogins()).thenReturn(12);
        assertEquals("12", column.getValue(item));
    }

    /** Test LastLoginColumn. */
    @Test public void testLastLoginColumn() {
        RegisteredUser item = mock(RegisteredUser.class);

        LastLoginColumn column = new LastLoginColumn();
        assertEquals(RegisteredUserSortColumn.LAST_LOGIN.toString(), column.getName());
        assertTrue(column.getCell() instanceof TextCell);
        assertTrue(column.isSortable());

        assertEquals("", column.getValue(null));

        when(item.getLastLogin()).thenReturn(null);
        assertEquals("", column.getValue(item));

        when(item.getLastLogin()).thenReturn(DateUtils.createDate(2011, 12, 14, 8, 32, 16, 993));
        assertEquals("2011-12-14T08:32", column.getValue(item));
    }

    /** Test AdminColumn. */
    @Test public void testAdminColumn() {
        AdminConstants constants = GWT.create(AdminConstants.class);
        RegisteredUser item = mock(RegisteredUser.class);

        AdminColumn column = new AdminColumn();
        assertEquals(RegisteredUserSortColumn.ADMIN.toString(), column.getName());
        assertTrue(column.getCell() instanceof TextCell);
        assertTrue(column.isSortable());

        assertEquals("", column.getValue(null));

        when(item.isAdmin()).thenReturn(true);
        assertEquals(constants.userTab_yes(), column.getValue(item));

        when(item.isAdmin()).thenReturn(false);
        assertEquals(constants.userTab_no(), column.getValue(item));
    }

    /** Test LockedColumn. */
    @Test public void testLockedColumn() {
        AdminConstants constants = GWT.create(AdminConstants.class);
        RegisteredUser item = mock(RegisteredUser.class);

        LockedColumn column = new LockedColumn();
        assertEquals(RegisteredUserSortColumn.LOCKED.toString(), column.getName());
        assertTrue(column.getCell() instanceof TextCell);
        assertTrue(column.isSortable());

        assertEquals("", column.getValue(null));

        when(item.isLocked()).thenReturn(true);
        assertEquals(constants.userTab_yes(), column.getValue(item));

        when(item.isLocked()).thenReturn(false);
        assertEquals(constants.userTab_no(), column.getValue(item));
    }

    /** Test SortHandler. */
    @Test public void testSortHandler() {
        ColumnSortEvent event = mock(ColumnSortEvent.class);

        UserTabView view = mock(UserTabView.class);
        SortHandler handler = new SortHandler(view);
        assertSame(view, handler.getParent());

        when(view.getRefreshEventHandler()).thenReturn(null);
        handler.onColumnSort(event);  // just make sure it doesn't blow up

        ArgumentCaptor<UnifiedEvent> captor = ArgumentCaptor.forClass(UnifiedEvent.class);
        ViewEventHandler refreshEventHandler = mock(ViewEventHandler.class);
        when(view.getRefreshEventHandler()).thenReturn(refreshEventHandler);
        handler.onColumnSort(event);  // just make sure it doesn't blow up
        verify(refreshEventHandler).handleEvent(captor.capture());
        assertEquals(UnifiedEventType.SORT_EVENT, captor.getValue().getEventType());
    }

    /** Test DeleteClickHandler. */
    @Test public void testDeleteClickHandler() {
        ConfirmationPopup popup = mock(ConfirmationPopup.class);
        UserTabView view = mock(UserTabView.class, Mockito.RETURNS_DEEP_STUBS);
        when(view.getDeletePopup()).thenReturn(popup);

        DeleteClickHandler handler = new DeleteClickHandler(view);
        assertSame(view, handler.getParent());

        ClickEvent event = mock(ClickEvent.class);
        List<RegisteredUser> selected = new ArrayList<RegisteredUser>();
        when(view.getTable().getSelectedRecords()).thenReturn(selected);

        handler.onClick(event);
        verify(view.getDeletePopup(), times(0)).showPopup();

        selected.add(mock(RegisteredUser.class));
        handler.onClick(event);
        verify(view.getDeletePopup(), times(1)).showPopup();
    }

    /** Test DeleteConfirmHandler. */
    @Test public void testDeleteConfirmHandler() {
        UnifiedEvent event = mock(UnifiedEvent.class);

        UserTabView view = mock(UserTabView.class);
        DeleteConfirmHandler handler = new DeleteConfirmHandler(view);
        assertSame(view, handler.getParent());

        when(view.getDeleteEventHandler()).thenReturn(null);
        handler.handleEvent(event);

        ViewEventHandler deleteConfirmEventHandler = mock(ViewEventHandler.class);
        when(view.getDeleteEventHandler()).thenReturn(deleteConfirmEventHandler);
        handler.handleEvent(event);
        verify(deleteConfirmEventHandler).handleEvent(event);
    }

    /** Test LockClickHandler. */
    @Test public void testLockClickHandler() {
        ClickEvent event = mock(ClickEvent.class);

        UserTabView view = mock(UserTabView.class);
        LockClickHandler handler = new LockClickHandler(view);
        assertSame(view, handler.getParent());

        when(view.getLockEventHandler()).thenReturn(null);
        handler.onClick(event);  // just make sure it doesn't blow up

        ArgumentCaptor<UnifiedEvent> captor = ArgumentCaptor.forClass(UnifiedEvent.class);
        ViewEventHandler lockEventHandler = mock(ViewEventHandler.class);
        when(view.getLockEventHandler()).thenReturn(lockEventHandler);
        handler.onClick(event);
        verify(lockEventHandler).handleEvent(captor.capture());
        assertEquals(UnifiedEventType.CLICK_EVENT, captor.getValue().getEventType());
        assertSame(event, captor.getValue().getClickEvent());
    }

    /** Test UnlockClickHandler. */
    @Test public void testUnlockClickHandler() {
        ClickEvent event = mock(ClickEvent.class);

        UserTabView view = mock(UserTabView.class);
        UnlockClickHandler handler = new UnlockClickHandler(view);
        assertSame(view, handler.getParent());

        when(view.getUnlockEventHandler()).thenReturn(null);
        handler.onClick(event);  // just make sure it doesn't blow up

        ArgumentCaptor<UnifiedEvent> captor = ArgumentCaptor.forClass(UnifiedEvent.class);
        ViewEventHandler unlockEventHandler = mock(ViewEventHandler.class);
        when(view.getUnlockEventHandler()).thenReturn(unlockEventHandler);
        handler.onClick(event);
        verify(unlockEventHandler).handleEvent(captor.capture());
        assertEquals(UnifiedEventType.CLICK_EVENT, captor.getValue().getEventType());
        assertSame(event, captor.getValue().getClickEvent());
    }

    /** Test RefreshClickHandler. */
    @Test public void testRefreshClickHandler() {
        ClickEvent event = mock(ClickEvent.class);

        UserTabView view = mock(UserTabView.class);
        RefreshClickHandler handler = new RefreshClickHandler(view);
        assertSame(view, handler.getParent());

        when(view.getRefreshEventHandler()).thenReturn(null);
        handler.onClick(event);  // just make sure it doesn't blow up

        ArgumentCaptor<UnifiedEvent> captor = ArgumentCaptor.forClass(UnifiedEvent.class);
        ViewEventHandler refreshEventHandler = mock(ViewEventHandler.class);
        when(view.getRefreshEventHandler()).thenReturn(refreshEventHandler);
        handler.onClick(event);
        verify(refreshEventHandler).handleEvent(captor.capture());
        assertEquals(UnifiedEventType.CLICK_EVENT, captor.getValue().getEventType());
        assertSame(event, captor.getValue().getClickEvent());
    }

    /** Test ClearClickHandler. */
    @Test public void testClearClickHandler() {
        ClickEvent event = mock(ClickEvent.class);

        UserTabView view = mock(UserTabView.class);
        ClearClickHandler handler = new ClearClickHandler(view);
        assertSame(view, handler.getParent());

        when(view.getCriteriaResetEventHandler()).thenReturn(null);
        handler.onClick(event);  // just make sure it doesn't blow up

        ArgumentCaptor<UnifiedEvent> captor = ArgumentCaptor.forClass(UnifiedEvent.class);
        ViewEventHandler criteriaResetEventHandler = mock(ViewEventHandler.class);
        when(view.getCriteriaResetEventHandler()).thenReturn(criteriaResetEventHandler);
        handler.onClick(event);
        verify(criteriaResetEventHandler).handleEvent(captor.capture());
        assertEquals(UnifiedEventType.CLICK_EVENT, captor.getValue().getEventType());
        assertSame(event, captor.getValue().getClickEvent());
    }

}
