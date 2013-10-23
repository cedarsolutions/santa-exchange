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

import java.util.HashMap;
import java.util.Map;

import com.cedarsolutions.client.gwt.event.ViewEventHandler;
import com.cedarsolutions.client.gwt.handler.AbstractClickHandler;
import com.cedarsolutions.client.gwt.module.view.ModulePageView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.idhandler.client.ElementIdHandler;
import com.gwtplatform.idhandler.client.WithElementId;

/**
 * RPC test page view.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class RpcTestPageView extends ModulePageView implements IRpcTestPageView {

    /** Reference to the UI binder. */
    private static ViewUiBinder BINDER = GWT.create(ViewUiBinder.class);

    /** Reference to the id handler. */
    private static ViewIdHandler ID_HANDLER = GWT.create(ViewIdHandler.class);

    /** UI binder interface. */
    protected interface ViewUiBinder extends UiBinder<Widget, RpcTestPageView> {
    }

    /** Id handler interface. */
    protected interface ViewIdHandler extends ElementIdHandler<RpcTestPageView> {
    }

    // User interface fields fron the UI binder
    @UiField @WithElementId protected Label unprotectedRpcLabel;
    @UiField @WithElementId protected Button unprotectedRpcButton;
    @UiField @WithElementId protected Label unprotectedRpcExpectedResult;
    @UiField @WithElementId protected Label unprotectedRpcActualResult;
    @UiField @WithElementId protected Label userRpcLabel;
    @UiField @WithElementId protected Button userRpcButton;
    @UiField @WithElementId protected Label userRpcExpectedResult;
    @UiField @WithElementId protected Label userRpcActualResult;
    @UiField @WithElementId protected Label adminRpcLabel;
    @UiField @WithElementId protected Button adminRpcButton;
    @UiField @WithElementId protected Label adminRpcExpectedResult;
    @UiField @WithElementId protected Label adminRpcActualResult;
    @UiField @WithElementId protected Label enabledUserRpcLabel;
    @UiField @WithElementId protected Button enabledUserRpcButton;
    @UiField @WithElementId protected Label enabledUserRpcExpectedResult;
    @UiField @WithElementId protected Label enabledUserRpcActualResult;
    @UiField @WithElementId protected Label enabledAdminRpcLabel;
    @UiField @WithElementId protected Button enabledAdminRpcButton;
    @UiField @WithElementId protected Label enabledAdminRpcExpectedResult;
    @UiField @WithElementId protected Label enabledAdminRpcActualResult;

    // Other instance variables
    private ViewEventHandler unprotectedRpcHandler;
    private ViewEventHandler userRpcHandler;
    private ViewEventHandler adminRpcHandler;
    private ViewEventHandler enabledUserRpcHandler;
    private ViewEventHandler enabledAdminRpcHandler;

    /** Create the view. */
    public RpcTestPageView() {
        this.initWidget(BINDER.createAndBindUi(this));
        ID_HANDLER.generateAndSetIds(this);
        this.setupBinderWidgets();
    }

    /** Get the unprotected RPC handler. */
    @Override
    public ViewEventHandler getUnprotectedRpcHandler() {
        return this.unprotectedRpcHandler;
    }

    /** Set the unprotected RPC handler. */
    @Override
    public void setUnprotectedRpcHandler(ViewEventHandler unprotectedRpcHandler) {
        this.unprotectedRpcHandler = unprotectedRpcHandler;
    }

    /** Get the user RPC handler. */
    @Override
    public ViewEventHandler getUserRpcHandler() {
        return this.userRpcHandler;
    }

    /** Set the user RPC handler. */
    @Override
    public void setUserRpcHandler(ViewEventHandler userRpcHandler) {
        this.userRpcHandler = userRpcHandler;
    }

    /** Get the admin RPC handler. */
    @Override
    public ViewEventHandler getAdminRpcHandler() {
        return this.adminRpcHandler;
    }

    /** Set the admin RPC handler. */
    @Override
    public void setAdminRpcHandler(ViewEventHandler adminRpcHandler) {
        this.adminRpcHandler = adminRpcHandler;
    }

    /** Get the enabled user RPC handler. */
    @Override
    public ViewEventHandler getEnabledUserRpcHandler() {
        return this.enabledUserRpcHandler;
    }

    /** Set the enabled user RPC handler. */
    @Override
    public void setEnabledUserRpcHandler(ViewEventHandler enabledUserRpcHandler) {
        this.enabledUserRpcHandler = enabledUserRpcHandler;
    }

    /** Get the enabled admin RPC handler. */
    @Override
    public ViewEventHandler getEnabledAdminRpcHandler() {
        return this.enabledAdminRpcHandler;
    }

    /** Set the enabled admin RPC handler. */
    @Override
    public void setEnabledAdminRpcHandler(ViewEventHandler enabledAdminRpcHandler) {
        this.enabledAdminRpcHandler = enabledAdminRpcHandler;
    }

    /**
     * Get the expected results for all RPCs.
     * The RPC identifier is the name, like "unprotectedRpc" or "userRpc".
     * @return Map from RPC identifier to expected result to display
     */
    @Override
    public Map<String, String> getExpectedResults() {
        Map<String, String> expectedResults = new HashMap<String, String>();

        expectedResults.put("unprotectedRpc", this.unprotectedRpcExpectedResult.getText());
        expectedResults.put("userRpc", this.userRpcExpectedResult.getText());
        expectedResults.put("adminRpc", this.adminRpcExpectedResult.getText());
        expectedResults.put("enabledUserRpc", this.enabledUserRpcExpectedResult.getText());
        expectedResults.put("enabledAdminRpc", this.enabledAdminRpcExpectedResult.getText());

        return expectedResults;
    }

    /**
     * Set the expected results for all RPCs.
     * The RPC identifier is the name, like "unprotectedRpc" or "userRpc".
     * @param expectedResults Map from RPC identifier to expected result to display
     */
    @Override
    public void setExpectedResults(Map<String, String> expectedResults) {
        this.unprotectedRpcExpectedResult.setText(expectedResults.get("unprotectedRpc"));
        this.userRpcExpectedResult.setText(expectedResults.get("userRpc"));
        this.adminRpcExpectedResult.setText(expectedResults.get("adminRpc"));
        this.enabledUserRpcExpectedResult.setText(expectedResults.get("enabledUserRpc"));
        this.enabledAdminRpcExpectedResult.setText(expectedResults.get("enabledAdminRpc"));
    }

    /**
     * Get the actual result for an RPC.
     * The RPC identifier is the name, like "unprotectedRpc" or "userRpc".
     * @param rpc    The RPC identifier
     * @return The actual result for the RPC, null if the RPC is unknown.
     */
    @Override
    public String getActualResult(String rpc) {
        if ("unprotectedRpc".equals(rpc)) {
            return this.unprotectedRpcActualResult.getText();
        } else if ("userRpc".equals(rpc)) {
            return this.userRpcActualResult.getText();
        } else if ("adminRpc".equals(rpc)) {
            return this.adminRpcActualResult.getText();
        } else if ("enabledUserRpc".equals(rpc)) {
            return this.enabledUserRpcActualResult.getText();
        } else if ("enabledAdminRpc".equals(rpc)) {
            return this.enabledAdminRpcActualResult.getText();
        } else {
            return null;
        }
    }

    /**
     * Set the actual result for an RPC.
     * The RPC identifier is the name, like "unprotectedRpc" or "userRpc".
     * @param rpc    The RPC identifier
     * @param result The actual result for the RPC
     */
    @Override
    public void setActualResult(String rpc, String result) {
        if ("unprotectedRpc".equals(rpc)) {
            this.unprotectedRpcActualResult.setText(result);
        } else if ("userRpc".equals(rpc)) {
            this.userRpcActualResult.setText(result);
        } else if ("adminRpc".equals(rpc)) {
            this.adminRpcActualResult.setText(result);
        } else if ("enabledUserRpc".equals(rpc)) {
            this.enabledUserRpcActualResult.setText(result);
        } else if ("enabledAdminRpc".equals(rpc)) {
            this.enabledAdminRpcActualResult.setText(result);
        }
    }

    /** Set up the widgets that are controlled by the UI binder. */
    private void setupBinderWidgets() {
        ExternalConstants constants = GWT.create(ExternalConstants.class);

        this.unprotectedRpcLabel.setText(constants.rpcTest_unprotectedRpcLabel());
        this.unprotectedRpcButton.setText(constants.rpcTest_invokeRpcButton());
        this.unprotectedRpcButton.setTitle(constants.rpcTest_unprotectedRpcTooltip());
        this.unprotectedRpcButton.addClickHandler(new UnprotectedRpcClickHandler(this));

        this.userRpcLabel.setText(constants.rpcTest_userRpcLabel());
        this.userRpcButton.setText(constants.rpcTest_invokeRpcButton());
        this.userRpcButton.setTitle(constants.rpcTest_userRpcTooltip());
        this.userRpcButton.addClickHandler(new UserRpcClickHandler(this));

        this.adminRpcLabel.setText(constants.rpcTest_adminRpcLabel());
        this.adminRpcButton.setText(constants.rpcTest_invokeRpcButton());
        this.adminRpcButton.setTitle(constants.rpcTest_adminRpcTooltip());
        this.adminRpcButton.addClickHandler(new AdminRpcClickHandler(this));

        this.enabledUserRpcLabel.setText(constants.rpcTest_enabledUserRpcLabel());
        this.enabledUserRpcButton.setText(constants.rpcTest_invokeRpcButton());
        this.enabledUserRpcButton.setTitle(constants.rpcTest_enabledUserRpcTooltip());
        this.enabledUserRpcButton.addClickHandler(new EnabledUserRpcClickHandler(this));

        this.enabledAdminRpcLabel.setText(constants.rpcTest_enabledAdminRpcLabel());
        this.enabledAdminRpcButton.setText(constants.rpcTest_invokeRpcButton());
        this.enabledAdminRpcButton.setTitle(constants.rpcTest_enabledAdminRpcTooltip());
        this.enabledAdminRpcButton.addClickHandler(new EnabledAdminRpcClickHandler(this));
    }

    /** Unprotected RPC click handler. */
    protected static class UnprotectedRpcClickHandler extends AbstractClickHandler<RpcTestPageView> {
        public UnprotectedRpcClickHandler(RpcTestPageView parent) {
            super(parent);
        }

        @Override
        public ViewEventHandler getViewEventHandler() {
            return this.getParent().getUnprotectedRpcHandler();
        }
    }

    /** User RPC click handler. */
    protected static class UserRpcClickHandler extends AbstractClickHandler<RpcTestPageView> {
        public UserRpcClickHandler(RpcTestPageView parent) {
            super(parent);
        }

        @Override
        public ViewEventHandler getViewEventHandler() {
            return this.getParent().getUserRpcHandler();
        }
    }

    /** Admin RPC click handler. */
    protected static class AdminRpcClickHandler extends AbstractClickHandler<RpcTestPageView> {
        public AdminRpcClickHandler(RpcTestPageView parent) {
            super(parent);
        }

        @Override
        public ViewEventHandler getViewEventHandler() {
            return this.getParent().getAdminRpcHandler();
        }
    }

    /** Enabled user RPC click handler. */
    protected static class EnabledUserRpcClickHandler extends AbstractClickHandler<RpcTestPageView> {
        public EnabledUserRpcClickHandler(RpcTestPageView parent) {
            super(parent);
        }

        @Override
        public ViewEventHandler getViewEventHandler() {
            return this.getParent().getEnabledUserRpcHandler();
        }
    }

    /** Enabled admin RPC click handler. */
    protected static class EnabledAdminRpcClickHandler extends AbstractClickHandler<RpcTestPageView> {
        public EnabledAdminRpcClickHandler(RpcTestPageView parent) {
            super(parent);
        }

        @Override
        public ViewEventHandler getViewEventHandler() {
            return this.getParent().getEnabledAdminRpcHandler();
        }
    }
}
