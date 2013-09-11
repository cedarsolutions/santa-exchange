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
package com.cedarsolutions.santa.client.root.presenter;

import com.cedarsolutions.santa.client.SantaExchangeMessages;
import com.cedarsolutions.santa.client.common.widget.AboutPopup;
import com.cedarsolutions.santa.client.common.widget.WelcomePopup;
import com.cedarsolutions.santa.client.common.widget.WidgetUtils;
import com.cedarsolutions.santa.client.root.RootEventBus;
import com.cedarsolutions.santa.client.root.view.IRootView;
import com.cedarsolutions.santa.client.root.view.RootView;
import com.cedarsolutions.shared.domain.ErrorDescription;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

/**
 * Presenter for root view.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@Presenter(view = RootView.class)
public class RootPresenter extends BasePresenter<IRootView, RootEventBus> {

    /** Handle the start event. */
    public void onStart() {
    }

    /** Handle the init event. */
    public void onInit() {
        this.eventBus.showLandingPage();
    }

    /** Handle the notFound event. */
    public void onNotFound() {
        this.eventBus.clearHistory();
        this.eventBus.showLandingPage();
        this.eventBus.showBookmarkNotFoundError();
    }

    /** Handle the clearHistory event. */
    public void onClearHistory() {
        // no-op target so the ClearHistory converter can do its job
    }

    /** Handle show the about pop-up. */
    public void onShowAboutPopup() {
        new AboutPopup().showPopup();
    }

    /** Show the welcome pop-up. */
    public void onShowWelcomePopup() {
        new WelcomePopup().showPopup();
    }

    /** Show an error via the error pop-up. */
    public void onShowErrorPopup(ErrorDescription error) {
        WidgetUtils.getInstance().showErrorPopup(error);
    }

    /** Show a "bookmark not found" error via the error pop-up. */
    public void onShowBookmarkNotFoundError() {
        SantaExchangeMessages messages = GWT.create(SantaExchangeMessages.class);
        String message = messages.bookmark_bookmarkNotFound();
        String supporting = messages.bookmark_youWillBeRedirected();
        ErrorDescription error = new ErrorDescription(message, supporting);
        WidgetUtils.getInstance().showErrorPopup(error);
    }

    /**
     * Replace the root body, rendering the passed-in widget.
     *
     * <p>
     * Mvp4g automatically adds the root module to the RootLayoutPanel, but
     * this doesn't happen for child modules.  They need to explicitly notify a
     * parent that they need to be rendered, and then the parent needs to make
     * it happen.  This has been a source of confusion for me more than once,
     * because it always surprises me that a module which otherwise does
     * everything right won't render until its parent gets involved.
     * </p>
     *
     * <p>
     * The Mvp4g examples show a few different ways for the parent module to
     * render a child.  One of the more common is to use a root view with some
     * sort of container in it (i.e.  SimplePanel).  The problem with this is
     * that some sorts of containers are not compatible with each other.  For
     * instance, it isn't possible for a SimplePanel to have a DockLayoutPanel
     * as a child.  The page simply won't render.
     * </p>
     *
     * <p>
     * Instead, I have decided to go after the root layout panel directly.  I
     * don't know if this is correct, but it works, and it (apparently) lets
     * the other underlying modules use whatever sorts of containers they want.
     * There still must be a root view, because the EventBus interface requires
     * a start view.  However, in this implementation, the root view is empty
     * and is never used other than as the startView target for the event bus.
     * </p>
     *
     * @param contents  Widget contents to render
     */
    public void onReplaceRootBody(IsWidget contents) {
        RootLayoutPanel.get().clear();
        RootLayoutPanel.get().add(contents);
    }

}
