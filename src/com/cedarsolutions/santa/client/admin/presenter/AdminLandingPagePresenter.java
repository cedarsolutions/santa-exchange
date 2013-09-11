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
package com.cedarsolutions.santa.client.admin.presenter;

import com.cedarsolutions.client.gwt.module.presenter.ModulePagePresenter;
import com.cedarsolutions.santa.client.admin.AdminEventBus;
import com.cedarsolutions.santa.client.admin.view.AdminLandingPageView;
import com.cedarsolutions.santa.client.admin.view.IAdminLandingPageView;
import com.cedarsolutions.santa.client.common.presenter.SystemStateInjector;
import com.cedarsolutions.santa.shared.domain.ClientSession;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;

/**
 * Presenter for admin landing page.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@Presenter(view = AdminLandingPageView.class)
public class AdminLandingPagePresenter extends ModulePagePresenter<IAdminLandingPageView, AdminEventBus> {

    /** Injector for the shared client session singleton. */
    private SystemStateInjector systemStateInjector;

    /** Show the admin landing page. */
    public void onShowAdminLandingPage() {
        this.getEventBus().selectHomeTab();
        this.showWelcomePopup();
    }

    /** Select the home tab. */
    public void onSelectHomeTab() {
        this.getView().selectHomeTab();
        this.replaceModuleBody();
    }

    /** Select the audit tab. */
    public void onSelectAuditTab() {
        this.getView().selectAuditTab();
        this.replaceModuleBody();
    }

    /** Select the user tab. */
    public void onSelectUserTab() {
        this.getView().selectUserTab();
        this.replaceModuleBody();
    }

    /** Show the welcome popup, if necessary. */
    private void showWelcomePopup() {
        if (this.getSession().getCurrentUser() != null) {
            if (this.getSession().getCurrentUser().isFirstLogin()) {
                this.getSession().getCurrentUser().setFirstLogin(false);
                if (this.getSession().getCurrentUser().isAdmin()) {
                    this.getEventBus().showWelcomePopup();
                }
            }
        }
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
}
