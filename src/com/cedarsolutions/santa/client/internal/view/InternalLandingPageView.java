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
package com.cedarsolutions.santa.client.internal.view;

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
 * View for internal landing page.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class InternalLandingPageView extends ModuleTabPanelView implements IInternalLandingPageView {

    /** Reference to the UI binder. */
    private static ViewUiBinder BINDER = GWT.create(ViewUiBinder.class);

    /** Reference to the id handler. */
    private static ViewIdHandler ID_HANDLER = GWT.create(ViewIdHandler.class);

    /** UI binder interface. */
    protected interface ViewUiBinder extends UiBinder<Widget, InternalLandingPageView> {
    }

    /** Id handler interface. */
    protected interface ViewIdHandler extends ElementIdHandler<InternalLandingPageView> {
    }

    // User interface fields fron the UI binder
    @UiField @WithElementId protected TabLayoutPanel tabPanel;

    // Other instance variables
    protected ExchangeListTabView exchangeListTab;
    protected EditExchangeTabView editExchangeTab;
    protected EditParticipantTabView editParticipantTab;

    /** Create the view. */
    @Inject
    public InternalLandingPageView(ExchangeListTabView exchangeListTab,
                                   EditExchangeTabView editExchangeTab,
                                   EditParticipantTabView editParticipantTab) {
        this.exchangeListTab = exchangeListTab;
        this.editExchangeTab = editExchangeTab;
        this.editParticipantTab = editParticipantTab;

        this.initWidget(BINDER.createAndBindUi(this));
        ID_HANDLER.generateAndSetIds(this);
        this.configureFullScreen();
    }

    /** Get the underlying tab panel. */
    @Override
    public TabLayoutPanel getTabPanel() {
        return this.tabPanel;
    }

    /** Select the exchange list tab. */
    @Override
    public void selectExchangeListTab() {
        this.replaceFirstTabWithView(this.exchangeListTab, "exchangeListTab", this.getTabTitle());
    }

    /** Select the edit exchange tab. */
    @Override
    public void selectEditExchangeTab() {
        this.replaceFirstTabWithView(this.editExchangeTab, "editExchangeTab", this.getTabTitle());
    }

    /** Select the edit participant tab. */
    @Override
    public void selectEditParticipantTab() {
        this.replaceFirstTabWithView(this.editParticipantTab, "editParticipantTab", this.getTabTitle());
    }

    /** Get the title of the only tab on the screen. */
    private String getTabTitle() {
        InternalConstants constants = GWT.create(InternalConstants.class);
        return constants.landing_manageTitle();
    }

}
