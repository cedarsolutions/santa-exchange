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
package com.cedarsolutions.santa.client.common.widget;

import com.cedarsolutions.santa.client.SantaExchangeConfig;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.idhandler.client.ElementIdHandler;
import com.gwtplatform.idhandler.client.WithElementId;

/**
 * Widget for the common footer that is shared on all pages.
 *
 * <p>
 * Arguably, you could say that this view should have its own presenter.
 * However, all it's really doing is figuring out how to display common
 * application data, and I've decided that this feels like a view-layer task.
 * It seems pointless to have a presenter that does nothing except pass in
 * values out of the shared configuration class.
 * </p>
 *
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class PageFooter extends Composite {

    /** Reference to the UI binder. */
    private static WidgetUiBinder BINDER = GWT.create(WidgetUiBinder.class);

    /** Reference to the id handler. */
    private static WidgetIdHandler ID_HANDLER = GWT.create(WidgetIdHandler.class);

    /** UI binder interface. */
    protected interface WidgetUiBinder extends UiBinder<Widget, PageFooter> {
    }

    /** Id handler interface. */
    protected interface WidgetIdHandler extends ElementIdHandler<PageFooter> {
    }

    // User interface fields fron the UI binder
    @UiField @WithElementId protected Label copyrightStatement;
    @UiField @WithElementId protected Label applicationVersion;

    /** Create the view. */
    public PageFooter() {
        this.initWidget(BINDER.createAndBindUi(this));
        ID_HANDLER.generateAndSetIds(this);

        SantaExchangeConfig config = GWT.create(SantaExchangeConfig.class);
        this.copyrightStatement.setText(config.system_copyrightStatement());
        this.applicationVersion.setText(WidgetUtils.getInstance().getApplicationVersion());
    }

}
