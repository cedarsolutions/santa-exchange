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
package com.cedarsolutions.santa.client.external.view;

import com.cedarsolutions.client.gwt.module.view.ModuleTemplateView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Template view that all other views in this module are rendered in terms of.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ExternalView extends ModuleTemplateView implements IExternalView {

    /** Reference to the UI binder. */
    private static ViewUiBinder BINDER = GWT.create(ViewUiBinder.class);

    /** UI binder interface. */
    protected interface ViewUiBinder extends UiBinder<Widget, ExternalView> {
    }

    // User interface fields fron the UI binder
    @UiField protected SimplePanel moduleBody;

    /** Create the view. */
    public ExternalView() {
        this.initWidget(BINDER.createAndBindUi(this));
    }

    /** Get the module body from the UI binder. */
    @Override
    protected Panel getModuleBody() {
        return this.moduleBody;
    }

}
