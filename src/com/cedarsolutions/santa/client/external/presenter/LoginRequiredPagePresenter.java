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
package com.cedarsolutions.santa.client.external.presenter;

import com.cedarsolutions.client.gwt.event.UnifiedEvent;
import com.cedarsolutions.client.gwt.handler.AbstractViewEventHandler;
import com.cedarsolutions.client.gwt.module.presenter.ModulePagePresenter;
import com.cedarsolutions.santa.client.external.ExternalEventBus;
import com.cedarsolutions.santa.client.external.view.ILoginRequiredPageView;
import com.cedarsolutions.santa.client.external.view.LoginRequiredPageView;
import com.cedarsolutions.shared.domain.OpenIdProvider;
import com.mvp4g.client.annotation.Presenter;

/**
 * Page to display when the user must log in.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@Presenter(view = LoginRequiredPageView.class)
public class LoginRequiredPagePresenter extends ModulePagePresenter<ILoginRequiredPageView, ExternalEventBus> {

    /**
     * Show the "login required" page.
     * @param destinationUrl  Destination URL to redirect to after login
     */
    public void onShowLoginRequiredPage(String destinationUrl) {
        this.getView().setLoginSelectorEventHandler(new LoginSelectorEventHandler(this, destinationUrl));
        this.replaceModuleBody();
    }

    /** Handle the action that selects a login provider. */
    protected static class LoginSelectorEventHandler extends AbstractViewEventHandler<LoginRequiredPagePresenter> {
        protected String destinationUrl;

        public LoginSelectorEventHandler(LoginRequiredPagePresenter parent, String destinationUrl) {
            super(parent);
            this.destinationUrl = destinationUrl;
        }

        @Override
        public void handleEvent(UnifiedEvent event) {
            OpenIdProvider openIdProvider = this.getParent().getView().getSelectedProvider();
            this.getParent().getEventBus().showLoginPageForUrl(openIdProvider, destinationUrl);
        }
    }
}
