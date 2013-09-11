/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 *              C E D A R
 *          S O L U T I O N S       "Software done right."
 *           S O F T W A R E
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2011-2013 Kenneth J. Pronovici.
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.junit.util.Assertions;
import com.cedarsolutions.santa.client.common.presenter.SystemStateInjector;
import com.cedarsolutions.santa.client.common.widget.WidgetUtils;
import com.cedarsolutions.santa.client.internal.InternalEventBus;
import com.cedarsolutions.santa.client.internal.presenter.BugReportDialogPresenter.SubmitBugReportCaller;
import com.cedarsolutions.santa.client.internal.presenter.BugReportDialogPresenter.SubmitEventHandler;
import com.cedarsolutions.santa.client.internal.view.IBugReportDialogView;
import com.cedarsolutions.santa.client.junit.StubbedClientTestCase;
import com.cedarsolutions.santa.client.rpc.IBugReportRpcAsync;
import com.cedarsolutions.santa.shared.domain.BugReport;
import com.cedarsolutions.santa.shared.domain.ClientSession;
import com.cedarsolutions.shared.domain.FederatedUser;
import com.cedarsolutions.util.DateUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Unit tests for BugReportDialogPresenter.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class BugReportDialogPresenterTest extends StubbedClientTestCase {

    /** Test bind(). */
    @Test public void testBind() {
        BugReportDialogPresenter presenter = createPresenter();
        presenter.bind();
        verify(presenter.getView()).setSubmitEventHandler(any(SubmitEventHandler.class));
    }

    /** Test onShowBugReportDialog(). */
    @Test public void testOnShowBugReportDialog() {
        Date now = DateUtils.getCurrentDate();

        FederatedUser currentUser = new FederatedUser();
        currentUser.setUserName("name");

        ArgumentCaptor<BugReport> bugReport = ArgumentCaptor.forClass(BugReport.class);

        BugReportDialogPresenter presenter = createPresenter();
        when(presenter.getSession().getCurrentUser()).thenReturn(currentUser);
        when(WidgetUtils.getInstance().getApplicationVersion()).thenReturn("version");

        presenter.onShowBugReportDialog();
        verify(presenter.getView()).showDialog(bugReport.capture());
        Assertions.assertAfter(now, bugReport.getValue().getReportDate());
        assertEquals("version", bugReport.getValue().getApplicationVersion());
        assertSame(currentUser, bugReport.getValue().getSubmittingUser());
        assertNull(bugReport.getValue().getEmailAddress());
        assertNull(bugReport.getValue().getProblemSummary());
        assertNull(bugReport.getValue().getDetailedDescription());
    }

    /** Test SubmitEventHandler. */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test public void testSubmitEventHandler() {
        BugReportDialogPresenter presenter = createPresenter();
        SubmitEventHandler handler = new SubmitEventHandler(presenter);

        BugReport report = new BugReport();
        when(presenter.getView().getBugReport()).thenReturn(report);

        ArgumentCaptor<BugReport> bugReport = ArgumentCaptor.forClass(BugReport.class);
        ArgumentCaptor<AsyncCallback> callback = ArgumentCaptor.forClass(AsyncCallback.class);

        handler.handleEvent(null); // actual event does not matter
        verify(presenter.getBugReportRpc()).submitBugReport(bugReport.capture(), callback.capture());
        assertSame(report, bugReport.getValue());
        assertNotNull(callback.getValue());
    }

    /** Test SubmitBugReportCaller. */
    @SuppressWarnings("unchecked")
    @Test public void testSubmitBugReportCaller() {
        BugReportDialogPresenter presenter = createPresenter();
        SubmitBugReportCaller caller = new SubmitBugReportCaller(presenter);
        assertNotNull(caller);
        assertEquals("IBugReportRpc", caller.getRpc());
        assertEquals("submitBugReport", caller.getMethod());
        assertSame(presenter, caller.parent);
        assertFalse(caller.isMarkedRetryable());

        BugReport bugReport = new BugReport();
        caller.setMethodArguments(bugReport);
        assertSame(bugReport, caller.bugReport);

        AsyncCallback<Void> callback = mock(AsyncCallback.class);
        caller.invoke(callback);
        verify(presenter.getBugReportRpc()).submitBugReport(bugReport, callback);

        caller.onSuccessResult(null);  // result does not matter
        verify(presenter.getView()).hideDialog();

        InvalidDataException error = new InvalidDataException("whatever");
        assertTrue(caller.onValidationError(error));
        verify(presenter.getView()).showValidationError(error);
    }

    /** Create a properly-mocked presenter, including everything that needs to be injected. */
    private static BugReportDialogPresenter createPresenter() {
        InternalEventBus eventBus = mock(InternalEventBus.class);
        IBugReportDialogView view = mock(IBugReportDialogView.class);
        ClientSession session = mock(ClientSession.class, RETURNS_DEEP_STUBS);  // so call1().call2().call3() works
        SystemStateInjector systemStateInjector = mock(SystemStateInjector.class);
        when(systemStateInjector.getSession()).thenReturn(session);
        IBugReportRpcAsync bugReportRpc = mock(IBugReportRpcAsync.class);

        BugReportDialogPresenter presenter = new BugReportDialogPresenter();
        presenter.setEventBus(eventBus);
        presenter.setView(view);
        presenter.setSystemStateInjector(systemStateInjector);
        presenter.setBugReportRpc(bugReportRpc);

        assertSame(eventBus, presenter.getEventBus());
        assertSame(view, presenter.getView());
        assertSame(systemStateInjector, presenter.getSystemStateInjector());
        assertSame(session, presenter.getSession());
        assertSame(bugReportRpc, presenter.getBugReportRpc());

        return presenter;
    }

}
