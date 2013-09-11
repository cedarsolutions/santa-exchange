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

import java.util.ArrayList;
import java.util.List;

import com.cedarsolutions.client.gwt.validation.IValidationErrorWidget;
import com.cedarsolutions.client.gwt.widget.ListItem;
import com.cedarsolutions.client.gwt.widget.OrderedList;
import com.cedarsolutions.util.gwt.GwtStringUtils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.idhandler.client.ElementIdHandler;
import com.gwtplatform.idhandler.client.WithElementId;

/**
 * Widget that displays validation errors.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ValidationErrorWidget extends Composite implements IValidationErrorWidget {

    /** Reference to the UI binder. */
    private static final WidgetUiBinder BINDER = GWT.create(WidgetUiBinder.class);

    /** Reference to the id handler. */
    private static WidgetIdHandler ID_HANDLER = GWT.create(WidgetIdHandler.class);

    /** UI binder interface. */
    protected interface WidgetUiBinder extends UiBinder<Widget, ValidationErrorWidget> {
    }

    /** Id handler interface. */
    protected interface WidgetIdHandler extends ElementIdHandler<ValidationErrorWidget> {
    }

    // User interface fields fron the UI binder
    @UiField @WithElementId protected HTMLPanel validationErrorPanel;
    @UiField @WithElementId protected Label validationErrorSummary;
    @UiField @WithElementId protected OrderedList validationErrorList;

    /** Create the view. */
    public ValidationErrorWidget() {
        this.initWidget(BINDER.createAndBindUi(this));
        ID_HANDLER.generateAndSetIds(this);
        this.hide();
    }

    /** Hide validation errors. */
    @Override
    public void hide() {
        this.validationErrorPanel.setVisible(false);
    }

    /** Show validation errors. */
    @Override
    public void show() {
        this.validationErrorPanel.setVisible(true);
    }

    /** Clear the error summary. */
    @Override
    public void clearErrorSummary() {
        this.setErrorSummary("");
    }

    /** Set the error summary. */
    @Override
    public void setErrorSummary(String errorSummary) {
        this.validationErrorSummary.setText(errorSummary);
    }

    /** Get the error summary. */
    @Override
    public String getErrorSummary() {
        return this.validationErrorSummary.getText();
    }

    /** Clear the error list. */
    @Override
    public void clearErrorList() {
        this.validationErrorList.clear();
    }

    /** Add an error to the list. */
    @Override
    public void addError(String error) {
        if (!GwtStringUtils.isEmpty(error)) {
            this.validationErrorList.add(error);
        }
    }

    /** Get a list of the displayed errors, independent of the underlying view. */
    @Override
    public List<String> getErrorList() {
        List<String> result = new ArrayList<String>();

        for (int i = 0; i < this.validationErrorList.getWidgetCount(); i++) {
            String error = ((ListItem) this.validationErrorList.getWidget(i)).getText();
            result.add(error);
        }

        return result;
    }

}
