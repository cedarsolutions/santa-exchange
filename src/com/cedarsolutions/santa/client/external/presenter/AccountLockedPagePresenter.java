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
package com.cedarsolutions.santa.client.external.presenter;

import com.cedarsolutions.client.gwt.event.UnifiedEvent;
import com.cedarsolutions.client.gwt.handler.AbstractViewEventHandler;
import com.cedarsolutions.client.gwt.module.presenter.ModulePagePresenter;
import com.cedarsolutions.santa.client.external.ExternalEventBus;
import com.cedarsolutions.santa.client.external.view.AccountLockedPageView;
import com.cedarsolutions.santa.client.external.view.IAccountLockedPageView;
import com.mvp4g.client.annotation.Presenter;

/**
 * Show the "account locked" page.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@Presenter(view = AccountLockedPageView.class)
public class AccountLockedPagePresenter extends ModulePagePresenter<IAccountLockedPageView, ExternalEventBus> {

    /** Show the account locked page. */
    public void onShowAccountLockedPage() {
        this.view.setContinueEventHandler(new ContinueEventHandler(this));
        this.replaceModuleBody();
    }

    /** Continue event handler. */
    protected static class ContinueEventHandler extends AbstractViewEventHandler<AccountLockedPagePresenter> {
        public ContinueEventHandler(AccountLockedPagePresenter parent) {
            super(parent);
        }

        @Override
        public void handleEvent(UnifiedEvent event) {
            this.getParent().getEventBus().logout();
        }
    }

}
