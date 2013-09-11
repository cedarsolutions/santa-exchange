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
package com.cedarsolutions.santa.client.admin.view;

import javax.inject.Inject;

import com.cedarsolutions.client.gwt.custom.tab.TabLayoutPanel;
import com.cedarsolutions.client.gwt.module.view.ModuleTabPanelView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.idhandler.client.ElementIdHandler;
import com.gwtplatform.idhandler.client.WithElementId;

/**
 * View for the admin landing page.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class AdminLandingPageView extends ModuleTabPanelView implements IAdminLandingPageView {

    /** Reference to the UI binder. */
    private static ViewUiBinder BINDER = GWT.create(ViewUiBinder.class);

    /** Reference to the id handler. */
    private static ViewIdHandler ID_HANDLER = GWT.create(ViewIdHandler.class);

    /** UI binder interface. */
    protected interface ViewUiBinder extends UiBinder<Widget, AdminLandingPageView> {
    }

    /** Id handler interface. */
    protected interface ViewIdHandler extends ElementIdHandler<AdminLandingPageView> {
    }

    // User interface fields fron the UI binder
    @UiField @WithElementId protected TabLayoutPanel tabPanel;

    // Other instance variables
    protected HomeTabView homeTab;
    protected AuditTabView auditTab;
    protected UserTabView userTab;

    /** Create the view. */
    @Inject
    public AdminLandingPageView(HomeTabView homeTab, AuditTabView auditTab, UserTabView userTab) {
        this.homeTab = homeTab;
        this.auditTab = auditTab;
        this.userTab = userTab;

        this.initWidget(BINDER.createAndBindUi(this));
        ID_HANDLER.generateAndSetIds(this);
        this.configureFullScreen();

        AdminConstants constants = GWT.create(AdminConstants.class);
        this.addTab(homeTab, "homeTab", constants.homeTab_tabTitle());
        this.addTab(auditTab, "auditTab", constants.auditTab_tabTitle());
        this.addTab(userTab, "userTab", constants.userTab_tabTitle());
    }

    /** Get the underlying tab panel. */
    @Override
    public TabLayoutPanel getTabPanel() {
        return this.tabPanel;
    }

    /** Select the home tab. */
    @Override
    public void selectHomeTab() {
        this.selectTabForView(this.homeTab);
    }

    /** Select the audit tab. */
    @Override
    public void selectAuditTab() {
        this.selectTabForView(this.auditTab);
    }

    /** Select the user tab. */
    @Override
    public void selectUserTab() {
        this.selectTabForView(this.userTab);
    }

}
