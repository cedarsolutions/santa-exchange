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

import static com.cedarsolutions.santa.shared.domain.MessageKeys.REQUIRED;

import java.util.ArrayList;
import java.util.List;

import com.cedarsolutions.dao.domain.SortOrder;
import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.santa.client.admin.view.UserTabView.AdminColumn;
import com.cedarsolutions.santa.client.admin.view.UserTabView.LastLoginColumn;
import com.cedarsolutions.santa.client.admin.view.UserTabView.LockedColumn;
import com.cedarsolutions.santa.client.admin.view.UserTabView.LoginsColumn;
import com.cedarsolutions.santa.client.admin.view.UserTabView.OpenIdProviderColumn;
import com.cedarsolutions.santa.client.admin.view.UserTabView.RegistrationDateColumn;
import com.cedarsolutions.santa.client.admin.view.UserTabView.UserIdColumn;
import com.cedarsolutions.santa.client.admin.view.UserTabView.UserNameColumn;
import com.cedarsolutions.santa.client.junit.ClientTestCase;
import com.cedarsolutions.santa.shared.domain.user.RegisteredUserCriteria;
import com.cedarsolutions.santa.shared.domain.user.RegisteredUserSortColumn;
import com.cedarsolutions.shared.domain.LocalizableMessage;
import com.cedarsolutions.shared.domain.OpenIdProvider;
import com.cedarsolutions.util.gwt.GwtDateUtils;
import com.google.gwt.core.client.GWT;

/**
 * Client-side unit tests for UserTabView.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class UserTabViewClientTest extends ClientTestCase {

    /** Test the constructor. */
    public void testConstructor() {
        UserTabView view = new UserTabView();
        assertWidgetsSetProperly(view);
        assertTableConfiguredProperly(view);
    }

    /** Test get/setHistoryToken(). */
    public void testGetSetHistoryToken() {
        UserTabView view = new UserTabView();
        assertNull(view.getHistoryToken());
        view.setHistoryToken("history");
        assertEquals("history", view.getHistoryToken());
    }

    /** Test the getters and setters for event handlers. */
    public void testEventHandlers() {
        UserTabView view = new UserTabView();

        StubbedViewEventHandler deleteEventHandler = new StubbedViewEventHandler();
        view.setDeleteEventHandler(deleteEventHandler);
        assertSame(deleteEventHandler, view.getDeleteEventHandler());
        clickButton(view.deleteButton);
        clickButton(view.getDeletePopup().getCancelButton());
        assertFalse(deleteEventHandler.handledEvent());
        clickButton(view.deleteButton);
        clickButton(view.getDeletePopup().getOkButton());
        assertTrue(deleteEventHandler.handledEvent());

        StubbedViewEventHandler lockEventHandler = new StubbedViewEventHandler();
        view.setLockEventHandler(lockEventHandler);
        assertSame(lockEventHandler, view.getLockEventHandler());
        clickButton(view.lockButton);
        assertTrue(lockEventHandler.handledEvent());

        StubbedViewEventHandler unlockEventHandler = new StubbedViewEventHandler();
        view.setUnlockEventHandler(unlockEventHandler);
        assertSame(unlockEventHandler, view.getUnlockEventHandler());
        clickButton(view.unlockButton);
        assertTrue(unlockEventHandler.handledEvent());

        StubbedViewEventHandler criteriaRefreshHandler = new StubbedViewEventHandler();
        view.setCriteriaResetEventHandler(criteriaRefreshHandler);
        assertSame(criteriaRefreshHandler, view.getCriteriaResetEventHandler());
        clickButton(view.clearButton);
        assertTrue(criteriaRefreshHandler.handledEvent());

        StubbedViewEventHandler refreshEventHandler = new StubbedViewEventHandler();
        view.setRefreshEventHandler(refreshEventHandler);
        assertSame(refreshEventHandler, view.getRefreshEventHandler());
        clickButton(view.refreshButton);
        assertTrue(refreshEventHandler.handledEvent());
    }

    /** Test the validation functionality. */
    public void testValidation() {
        UserTabView view = new UserTabView();
        assertSame(view.validationErrorWidget, view.getValidationErrorWidget());
        assertTrue(view.getValidatedFieldMap().isEmpty());

        // Just make sure it doesn't blow up; there are no configured validations right now
        view.translate(new LocalizableMessage(REQUIRED, "startDate", "date invalid"));

        // make sure it doesn't blow up
        view.showValidationError(new InvalidDataException("whatever"));
    }

    /** Test get/set search criteria. */
    public void testSearchCriteria() {
        UserTabView view = new UserTabView();

        assertFalse(view.hasSearchCriteria());
        RegisteredUserCriteria result = view.getSearchCriteria();
        assertNotNull(result);
        assertEquals(new RegisteredUserCriteria(), result);

        RegisteredUserCriteria criteria = createCriteria();
        view.setSearchCriteria(criteria);
        assertTrue(view.hasSearchCriteria());
        result = view.getSearchCriteria();
        criteria.setStartDate(GwtDateUtils.resetTime(criteria.getStartDate(), "00:00"));
        criteria.setEndDate(GwtDateUtils.resetTime(criteria.getEndDate(), "23:59"));
        assertEquals(criteria, result); // The view always resets the range to be inclusive

        criteria = new RegisteredUserCriteria();
        view.setSearchCriteria(criteria);
        assertTrue(view.hasSearchCriteria());
        result = view.getSearchCriteria();
        assertEquals(criteria, result); // The view always resets the range to be inclusive
    }

    /** Test getSelectedRecords(). */
    public void testGetSelectedRecords() {
        UserTabView view = new UserTabView();
        assertNotNull(view.getSelectedRecords());  // just make sure that it doesn't blow up
    }

    /** Test showSearchCriteria() and hideSearchCriteria(). */
    public void testHideShowSearchCriteria() {
        UserTabView view = new UserTabView();
        assertFalse(view.filterDisclosure.isOpen());

        view.showSearchCriteria();
        assertTrue(view.filterDisclosure.isOpen());

        view.hideSearchCriteria();
        assertFalse(view.filterDisclosure.isOpen());
    }

    /** Create a RegisteredUserCriteria for testing. */
    private static RegisteredUserCriteria createCriteria() {
        RegisteredUserCriteria criteria = new RegisteredUserCriteria();

        List<String> userIds = new ArrayList<String>();
        userIds.add("user");

        List<String> userNames = new ArrayList<String>();
        userNames.add("name");

        List<OpenIdProvider> openIdProviders = new ArrayList<OpenIdProvider>();
        openIdProviders.add(OpenIdProvider.GOOGLE);

        criteria.setUserIds(userIds);
        criteria.setUserNames(userNames);
        criteria.setOpenIdProviders(openIdProviders);
        criteria.setAdmin(true);
        criteria.setLocked(true);
        criteria.setStartDate(GwtDateUtils.createDate(2011, 10, 1));
        criteria.setEndDate(GwtDateUtils.createDate(2011, 10, 2));
        criteria.setSortOrder(SortOrder.DESCENDING);
        criteria.setSortColumn(RegisteredUserSortColumn.ADMIN);

        return criteria;
    }

    /** Assert that all of the widgets are configured properly. */
    private static void assertWidgetsSetProperly(UserTabView view) {
        AdminConstants constants = GWT.create(AdminConstants.class);

        assertNotNull(view.deleteButton);
        assertNotNull(view.lockButton);
        assertNotNull(view.unlockButton);

        assertNotNull(view.filterDisclosure);
        assertFalse(view.filterDisclosure.isOpen());
        // unfortunately, we can't verify that the header text is set properly

        assertNotNull(view.validationErrorWidget);

        assertNotNull(view.userNameLabel);
        assertEquals(constants.userTab_userNameCriteria(), view.userNameLabel.getText());

        assertNotNull(view.userNameInput);
        assertEquals("", view.userNameInput.getText());

        assertNotNull(view.userIdLabel);
        assertEquals(constants.userTab_userIdCriteria(), view.userIdLabel.getText());

        assertNotNull(view.userIdInput);
        assertEquals("", view.userIdInput.getText());

        assertNotNull(view.providerLabel);
        assertEquals(constants.userTab_openIdProviderCriteria(), view.providerLabel.getText());

        assertNotNull(view.providerInput);
        assertEquals(null, view.providerInput.getSelectedValue());  // null == any

        assertNotNull(view.adminLabel);
        assertEquals(constants.userTab_adminCriteria(), view.adminLabel.getText());

        assertNotNull(view.adminInput);
        assertEquals(null, view.adminInput.getSelectedValue());  // null == any

        assertNotNull(view.lockedLabel);
        assertEquals(constants.userTab_lockedCriteria(), view.lockedLabel.getText());

        assertNotNull(view.lockedInput);
        assertEquals(null, view.lockedInput.getSelectedValue());  // null == any

        assertNotNull(view.registrationDateLabel);
        assertEquals(constants.userTab_registrationDateCriteria(), view.registrationDateLabel.getText());

        assertNotNull(view.toLabel);
        assertEquals(constants.userTab_to(), view.toLabel.getText());

        assertNotNull(view.startDateInput);
        assertEquals(null, view.startDateInput.getValue());
        assertDateFormatValid(view.startDateInput, GwtDateUtils.DATE_FORMAT);

        assertNotNull(view.endDateInput);
        assertEquals(null, view.endDateInput.getValue());
        assertDateFormatValid(view.endDateInput, GwtDateUtils.DATE_FORMAT);

        assertNotNull(view.refreshButton);
        assertEquals(constants.userTab_refreshButton(), view.refreshButton.getText());

        assertNotNull(view.clearButton);
        assertEquals(constants.userTab_clearButton(), view.clearButton.getText());
    }

    /** Assert that the table is configured properly. */
    private static void assertTableConfiguredProperly(UserTabView view) {
        AdminConstants constants = GWT.create(AdminConstants.class);

        assertEquals(constants.userTab_pageSize(), view.getPageSize());
        assertSame(view.table, view.getDisplay());

        assertNotNull(view.table);
        assertNotNull(view.pager);
        assertTrue(view.table.hasSelectionColumn());
        assertEquals(constants.userTab_noRows(), view.table.getNoRowsMessage());

        assertEquals(9, view.table.getColumnCount());
        assertSelectionColumnAtIndex(view.table, 0);
        assertTableColumnValid(view.table, 1, UserNameColumn.class, constants.userTab_userNameTitle());
        assertTableColumnValid(view.table, 2, UserIdColumn.class, constants.userTab_userIdTitle());
        assertTableColumnValid(view.table, 3, RegistrationDateColumn.class, constants.userTab_registrationDateTitle());
        assertTableColumnValid(view.table, 4, OpenIdProviderColumn.class, constants.userTab_openIdProviderTitle());
        assertTableColumnValid(view.table, 5, LoginsColumn.class, constants.userTab_loginsTitle());
        assertTableColumnValid(view.table, 6, LastLoginColumn.class, constants.userTab_lastLoginTitle());
        assertTableColumnValid(view.table, 7, AdminColumn.class, constants.userTab_adminTitle());
        assertTableColumnValid(view.table, 8, LockedColumn.class, constants.userTab_lockedTitle());
    }
}
