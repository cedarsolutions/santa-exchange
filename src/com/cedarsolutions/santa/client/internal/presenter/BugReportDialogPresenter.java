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
package com.cedarsolutions.santa.client.internal.presenter;

import java.util.Date;

import com.cedarsolutions.client.gwt.event.UnifiedEvent;
import com.cedarsolutions.client.gwt.handler.AbstractViewEventHandler;
import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.santa.client.common.presenter.SystemStateInjector;
import com.cedarsolutions.santa.client.common.widget.WidgetUtils;
import com.cedarsolutions.santa.client.internal.InternalEventBus;
import com.cedarsolutions.santa.client.internal.view.BugReportDialogView;
import com.cedarsolutions.santa.client.internal.view.IBugReportDialogView;
import com.cedarsolutions.santa.client.rpc.IBugReportRpcAsync;
import com.cedarsolutions.santa.client.rpc.util.StandardRpcCaller;
import com.cedarsolutions.santa.shared.domain.BugReport;
import com.cedarsolutions.santa.shared.domain.ClientSession;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

/**
 * Presenter for the bug report dialog.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@Presenter(view = BugReportDialogView.class)
public class BugReportDialogPresenter extends BasePresenter<IBugReportDialogView, InternalEventBus> {

    /** Injector for the shared client session singleton. */
    private SystemStateInjector systemStateInjector;

    /** Bug report service. */
    private IBugReportRpcAsync bugReportRpc;

    /** Bind the user interface. */
    @Override
    public void bind() {
        this.view.setSubmitEventHandler(new SubmitEventHandler(this));
    }

    /** Show the bug report dialog. */
    public void onShowBugReportDialog() {
        BugReport bugReport = new BugReport();
        bugReport.setReportDate(new Date());
        bugReport.setSubmittingUser(this.getSession().getCurrentUser());
        bugReport.setApplicationVersion(WidgetUtils.getInstance().getApplicationVersion());
        this.getView().showDialog(bugReport);
    }

    /** Get the session from the injector. */
    public ClientSession getSession() {
        return this.systemStateInjector.getSession();
    }

    public SystemStateInjector getSystemStateInjector() {
        return this.systemStateInjector;
    }

    @Inject
    public void setSystemStateInjector(SystemStateInjector systemStateInjector) {
        this.systemStateInjector = systemStateInjector;
    }

    public IBugReportRpcAsync getBugReportRpc() {
        return this.bugReportRpc;
    }

    @Inject
    public void setBugReportRpc(IBugReportRpcAsync bugReportRpc) {
        this.bugReportRpc = bugReportRpc;
    }

    /** Event handler for submit action. */
    protected static class SubmitEventHandler extends AbstractViewEventHandler<BugReportDialogPresenter> {
        public SubmitEventHandler(BugReportDialogPresenter parent) {
            super(parent);
        }

        @Override
        public void handleEvent(UnifiedEvent event) {
            SubmitBugReportCaller caller = new SubmitBugReportCaller(this.getParent());
            caller.setMethodArguments(this.getParent().getView().getBugReport());
            caller.invoke();
        }
    }

    /** Caller for IBugReportRpc.submitBugReport(). */
    protected static class SubmitBugReportCaller extends StandardRpcCaller<IBugReportRpcAsync, Void> {
        protected BugReportDialogPresenter parent;
        protected BugReport bugReport;

        public SubmitBugReportCaller(BugReportDialogPresenter parent) {
            super(parent.bugReportRpc, "IBugReportRpc", "submitBugReport");
            this.parent = parent;
            this.markNotRetryable();  // it's NOT safe to retry this RPC call
        }

        public void setMethodArguments(BugReport bugReport) {
            this.bugReport = bugReport;
        }

        @Override
        public void invokeRpcMethod(IBugReportRpcAsync async, AsyncCallback<Void> callback) {
            async.submitBugReport(this.bugReport, callback);
        }

        @Override
        public void onSuccessResult(Void result) {
            this.parent.getView().hideDialog();
        }

        @Override
        public boolean onValidationError(InvalidDataException caught) {
            this.parent.getView().showValidationError(caught);
            return true;  // indicate that we've handled the validation error
        }
    }

}
