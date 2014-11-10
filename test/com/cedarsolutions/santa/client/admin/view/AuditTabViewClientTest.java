/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 *              C E D A R
 *          S O L U T I O N S       "Software done right."
 *           S O F T W A R E
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2013-2014 Kenneth J. Pronovici.
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

import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.santa.client.admin.view.AuditTabView.EventIdColumn;
import com.cedarsolutions.santa.client.admin.view.AuditTabView.EventTimestampColumn;
import com.cedarsolutions.santa.client.admin.view.AuditTabView.EventTypeColumn;
import com.cedarsolutions.santa.client.admin.view.AuditTabView.ExtraDataColumn;
import com.cedarsolutions.santa.client.admin.view.AuditTabView.SessionIdColumn;
import com.cedarsolutions.santa.client.admin.view.AuditTabView.UserIdColumn;
import com.cedarsolutions.santa.client.junit.ClientTestCase;
import com.cedarsolutions.santa.shared.domain.audit.AuditEventCriteria;
import com.cedarsolutions.santa.shared.domain.audit.AuditEventType;
import com.cedarsolutions.shared.domain.LocalizableMessage;
import com.cedarsolutions.util.gwt.GwtDateUtils;
import com.google.gwt.core.client.GWT;

/**
 * Client-side unit tests for AuditTabView.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class AuditTabViewClientTest extends ClientTestCase {

    /** Test the constructor. */
    public void testConstructor() {
        AuditTabView view = new AuditTabView();
        assertWidgetsSetProperly(view);
        assertTableConfiguredProperly(view);
    }

    /** Test get/setHistoryToken(). */
    public void testGetSetHistoryToken() {
        AuditTabView view = new AuditTabView();
        assertNull(view.getHistoryToken());
        view.setHistoryToken("history");
        assertEquals("history", view.getHistoryToken());
    }

    /** Test the getters and setters for event handlers. */
    public void testEventHandlers() {
        AuditTabView view = new AuditTabView();

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

        StubbedViewEventHandlerWithContext<String> userIdSelectedHandler = new StubbedViewEventHandlerWithContext<String>();
        view.setUserIdSelectedHandler(userIdSelectedHandler);
        assertSame(userIdSelectedHandler, view.getUserIdSelectedHandler());
    }

    /** Test the validation functionality. */
    public void testValidation() {
        AuditTabView view = new AuditTabView();
        assertSame(view.validationErrorWidget, view.getValidationErrorWidget());
        assertTrue(view.getValidatedFieldMap().isEmpty());

        AdminMessageConstants constants = GWT.create(AdminMessageConstants.class);
        assertEquals(constants.auditTab_required_startDate(), view.translate(new LocalizableMessage(REQUIRED, "startDate", "date invalid")));

        view.showValidationError(new InvalidDataException("whatever")); // make sure it doesn't blow up
    }

    /** Test get/set search criteria. */
    public void testSearchCriteria() {
        AuditTabView view = new AuditTabView();

        assertFalse(view.hasSearchCriteria());
        AuditEventCriteria result = view.getSearchCriteria();
        assertNotNull(result);
        assertEquals(new AuditEventCriteria(), result);

        AuditEventCriteria criteria = createCriteria();
        view.setSearchCriteria(criteria);
        assertTrue(view.hasSearchCriteria());
        result = view.getSearchCriteria();
        criteria.setStartDate(GwtDateUtils.resetTime(criteria.getStartDate(), "00:00"));
        criteria.setEndDate(GwtDateUtils.resetTime(criteria.getEndDate(), "23:59"));
        assertEquals(criteria, result); // The view always resets the range to be inclusive

        criteria = new AuditEventCriteria();
        view.setSearchCriteria(criteria);
        assertTrue(view.hasSearchCriteria());
        result = view.getSearchCriteria();
        assertEquals(criteria, result); // The view always resets the range to be inclusive
    }

    /** Create a AuditEventCriteria for testing. */
    private static AuditEventCriteria createCriteria() {
        AuditEventCriteria criteria = new AuditEventCriteria();

        List<AuditEventType> eventTypes = new ArrayList<AuditEventType>();
        eventTypes.add(AuditEventType.ADMIN_LOGIN);

        List<String> userIds = new ArrayList<String>();
        userIds.add("user");

        criteria.setEventTypes(eventTypes);
        criteria.setUserIds(userIds);
        criteria.setStartDate(GwtDateUtils.createDate(2011, 10, 1, 18, 2));
        criteria.setEndDate(GwtDateUtils.createDate(2011, 10, 2, 3, 14));

        return criteria;
    }

    /** Assert that all of the widgets are configured properly. */
    private static void assertWidgetsSetProperly(AuditTabView view) {
        AdminConstants constants = GWT.create(AdminConstants.class);

        assertNotNull(view.filterDisclosure);
        assertFalse(view.filterDisclosure.isOpen());
        // unfortunately, we can't verify that the header text is set properly

        assertNotNull(view.validationErrorWidget);

        assertNotNull(view.eventTypeLabel);
        assertEquals(constants.auditTab_eventTypeCriteria(), view.eventTypeLabel.getText());

        assertNotNull(view.eventTypeInput);
        assertEquals(null, view.eventTypeInput.getSelectedObjectValue());  // null == any

        assertNotNull(view.userIdLabel);
        assertEquals(constants.auditTab_userIdCriteria(), view.userIdLabel.getText());

        assertNotNull(view.userIdInput);
        assertEquals("", view.userIdInput.getText());

        assertNotNull(view.eventDateLabel);
        assertEquals(constants.auditTab_eventDateCriteria(), view.eventDateLabel.getText());

        assertNotNull(view.toLabel);
        assertEquals(constants.auditTab_to(), view.toLabel.getText());

        assertNotNull(view.startDateInput);
        assertEquals(null, view.startDateInput.getValue());
        assertDateFormatValid(view.startDateInput, GwtDateUtils.DATE_FORMAT);

        assertNotNull(view.endDateInput);
        assertEquals(null, view.endDateInput.getValue());
        assertDateFormatValid(view.endDateInput, GwtDateUtils.DATE_FORMAT);

        assertNotNull(view.refreshButton);
        assertEquals(constants.auditTab_refreshButton(), view.refreshButton.getText());

        assertNotNull(view.clearButton);
        assertEquals(constants.auditTab_clearButton(), view.clearButton.getText());
    }

    /** Assert that the table is configured properly. */
    private static void assertTableConfiguredProperly(AuditTabView view) {
        AdminConstants constants = GWT.create(AdminConstants.class);

        assertEquals(constants.auditTab_pageSize(), view.getPageSize());
        assertSame(view.table, view.getDisplay());

        assertNotNull(view.table);
        assertNotNull(view.pager);
        assertFalse(view.table.hasSelectionColumn());
        assertEquals(constants.auditTab_noRows(), view.table.getNoRowsMessage());

        assertEquals(6, view.table.getColumnCount());
        assertTableColumnValid(view.table, 0, EventIdColumn.class, constants.auditTab_eventIdTitle());
        assertTableColumnValid(view.table, 1, EventTypeColumn.class, constants.auditTab_eventTypeTitle());
        assertTableColumnValid(view.table, 2, EventTimestampColumn.class, constants.auditTab_eventTimestampTitle());
        assertTableColumnValid(view.table, 3, UserIdColumn.class, constants.auditTab_userIdTitle());
        assertTableColumnValid(view.table, 4, SessionIdColumn.class, constants.auditTab_sessionIdTitle());
        assertTableColumnValid(view.table, 5, ExtraDataColumn.class, constants.auditTab_extraDataTitle());
    }

}
