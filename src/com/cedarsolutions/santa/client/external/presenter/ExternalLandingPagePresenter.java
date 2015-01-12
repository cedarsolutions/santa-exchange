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
package com.cedarsolutions.santa.client.external.presenter;

import com.cedarsolutions.client.gwt.event.UnifiedEvent;
import com.cedarsolutions.client.gwt.handler.AbstractViewEventHandler;
import com.cedarsolutions.client.gwt.module.presenter.ModulePagePresenter;
import com.cedarsolutions.santa.client.SantaExchangeEventTypes;
import com.cedarsolutions.santa.client.common.presenter.SystemStateInjector;
import com.cedarsolutions.santa.client.external.ExternalEventBus;
import com.cedarsolutions.santa.client.external.view.ExternalLandingPageView;
import com.cedarsolutions.santa.client.external.view.IExternalLandingPageView;
import com.cedarsolutions.santa.shared.domain.ClientSession;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;

/**
 * Presenter for public landing page.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@Presenter(view = ExternalLandingPageView.class)
public class ExternalLandingPagePresenter extends ModulePagePresenter<IExternalLandingPageView, ExternalEventBus> {

    /** Injector for the shared client session singleton. */
    private SystemStateInjector systemStateInjector;

    /** Show the external landing page. */
    public void onShowExternalLandingPage() {
        this.view.setLoginEventHandler(new LoginEventHandler(this));
        this.replaceModuleBody();
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

    /** Handler for the login event. */
    protected static class LoginEventHandler extends AbstractViewEventHandler<ExternalLandingPagePresenter> {
        public LoginEventHandler(ExternalLandingPagePresenter parent) {
            super(parent);
        }

        @Override
        public void handleEvent(UnifiedEvent event) {
            if (this.getParent().getSystemStateInjector().getSession().isLoggedIn()) {
                this.getParent().getEventBus().showLandingPage();
            } else {
                String destinationToken = SantaExchangeEventTypes.LANDING_PAGE;
                this.getParent().getEventBus().showGoogleAccountsLoginPageForToken(destinationToken);
            }
        }
    }
}
