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

import com.cedarsolutions.client.gwt.event.UnifiedMenuHandler;
import com.cedarsolutions.client.gwt.event.ViewEventHandler;
import com.cedarsolutions.client.gwt.module.view.ModuleTemplateView;
import com.cedarsolutions.santa.client.common.view.ViewUtils;
import com.cedarsolutions.shared.domain.FederatedUser;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.idhandler.client.ElementIdHandler;
import com.gwtplatform.idhandler.client.WithElementId;

/**
 * Template view that all other views in this module are rendered in terms of.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class AdminView extends ModuleTemplateView implements IAdminView {

    /** Reference to the UI binder. */
    private static ViewUiBinder BINDER = GWT.create(ViewUiBinder.class);

    /** Reference to the id handler. */
    private static ViewIdHandler ID_HANDLER = GWT.create(ViewIdHandler.class);

    /** UI binder interface. */
    protected interface ViewUiBinder extends UiBinder<Widget, AdminView> {
    }

    /** Id handler interface. */
    protected interface ViewIdHandler extends ElementIdHandler<AdminView> {
    }

    // User interface fields fron the UI binder
    @UiField @WithElementId protected Label title;
    @UiField @WithElementId protected MenuBar menuBar;
    @UiField @WithElementId protected MenuItem mainMenu;
    @UiField @WithElementId protected MenuItem internalLandingPageItem;
    @UiField @WithElementId protected MenuItem dashboardItem;
    @UiField @WithElementId protected MenuItem aboutItem;
    @UiField @WithElementId protected MenuItem bugReportItem;
    @UiField @WithElementId protected MenuItem sourceCodeItem;
    @UiField @WithElementId protected MenuItem logoutItem;
    @UiField protected SimplePanel moduleBody;

    // Other instance variables
    private FederatedUser currentUser;
    private ViewEventHandler internalLandingPageEventHandler;
    private ViewEventHandler dashboardEventHandler;
    private ViewEventHandler aboutEventHandler;
    private ViewEventHandler bugReportEventHandler;
    private ViewEventHandler sourceCodeEventHandler;
    private ViewEventHandler logoutEventHandler;

    /** Create the view. */
    public AdminView() {
        this.initWidget(BINDER.createAndBindUi(this));
        ID_HANDLER.generateAndSetIds(this);

        AdminConstants constants = GWT.create(AdminConstants.class);
        this.title.setText(constants.header_title());
        this.internalLandingPageItem.setText(constants.header_internalLandingPage());
        this.dashboardItem.setText(constants.header_dashboard());
        this.aboutItem.setText(constants.header_about());
        this.bugReportItem.setText(constants.header_bugReport());
        this.sourceCodeItem.setText(constants.header_sourceCode());
        this.logoutItem.setText(constants.header_logout());
    }

    /** Get the module body from the UI binder. */
    @Override
    protected Panel getModuleBody() {
        return this.moduleBody;
    }

    /** Set the current user. */
    @Override
    public void setCurrentUser(FederatedUser currentUser) {
        this.currentUser = currentUser;
        this.mainMenu.setText(ViewUtils.getInstance().deriveDisplayedUsername(currentUser));
    }

    /** Get the current user. */
    @Override
    public FederatedUser getCurrentUser() {
        return this.currentUser;
    }

    /** Set the internal landing page event handler. */
    @Override
    public void setInternalLandingPageEventHandler(ViewEventHandler internalLandingPageEventHandler) {
        this.internalLandingPageEventHandler = internalLandingPageEventHandler;
        this.internalLandingPageItem.setScheduledCommand(new UnifiedMenuHandler(internalLandingPageEventHandler));
    }

    /** Get the internalLandingPage event handler. */
    @Override
    public ViewEventHandler getInternalLandingPageEventHandler() {
        return this.internalLandingPageEventHandler;
    }

    /** Set the dashboard event handler. */
    @Override
    public void setDashboardEventHandler(ViewEventHandler dashboardEventHandler) {
        this.dashboardEventHandler = dashboardEventHandler;
        this.dashboardItem.setScheduledCommand(new UnifiedMenuHandler(dashboardEventHandler));
    }

    /** Get the dashboard event handler. */
    @Override
    public ViewEventHandler getDashboardEventHandler() {
        return this.dashboardEventHandler;
    }

    /** Set the event handler for the about action. */
    @Override
    public void setAboutEventHandler(ViewEventHandler aboutEventHandler) {
        this.aboutEventHandler = aboutEventHandler;
        this.aboutItem.setScheduledCommand(new UnifiedMenuHandler(aboutEventHandler));
    }

    /** Get the about event handler. */
    @Override
    public ViewEventHandler getAboutEventHandler() {
        return this.aboutEventHandler;
    }

    /** Set the event handler for the bug report action. */
    @Override
    public void setBugReportEventHandler(ViewEventHandler bugReportEventHandler) {
        this.bugReportEventHandler = bugReportEventHandler;
        this.bugReportItem.setScheduledCommand(new UnifiedMenuHandler(bugReportEventHandler));
    }

    /** Get the bug report event handler. */
    @Override
    public ViewEventHandler getBugReportEventHandler() {
        return this.bugReportEventHandler;
    }

    /** Set the event handler for the source code action. */
    @Override
    public void setSourceCodeEventHandler(ViewEventHandler sourceCodeEventHandler) {
        this.sourceCodeEventHandler = sourceCodeEventHandler;
        this.sourceCodeItem.setScheduledCommand(new UnifiedMenuHandler(sourceCodeEventHandler));
    }

    /** Get the source code event handler. */
    @Override
    public ViewEventHandler getSourceCodeEventHandler() {
        return this.sourceCodeEventHandler;
    }

    /** Set the logout event handler. */
    @Override
    public void setLogoutEventHandler(ViewEventHandler logoutEventHandler) {
        this.logoutEventHandler = logoutEventHandler;
        this.logoutItem.setScheduledCommand(new UnifiedMenuHandler(logoutEventHandler));
    }

    /** Get the logout event handler. */
    @Override
    public ViewEventHandler getLogoutEventHandler() {
        return this.logoutEventHandler;
    }

}
