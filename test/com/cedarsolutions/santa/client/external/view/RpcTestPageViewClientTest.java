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

import com.cedarsolutions.santa.client.junit.ClientTestCase;
import com.google.gwt.core.client.GWT;

/**
 * Client-side unit tests for RpcTestPageView.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class RpcTestPageViewClientTest extends ClientTestCase {

    /** Test the constructor(). */
    public void testConstructor() {
        RpcTestPageView view = new RpcTestPageView();
        assertWidgetsSetProperly(view);
    }

    /** Test the getters and setters for event handlers. */
    public void testEventHandlers() {
        RpcTestPageView view = new RpcTestPageView();

        StubbedViewEventHandler unprotectedRpcHandler = new StubbedViewEventHandler();
        view.setUnprotectedRpcHandler(unprotectedRpcHandler);
        assertSame(unprotectedRpcHandler, view.getUnprotectedRpcHandler());
        clickButton(view.unprotectedRpcButton);
        assertTrue(unprotectedRpcHandler.handledEvent());

        StubbedViewEventHandler userRpcHandler = new StubbedViewEventHandler();
        view.setUserRpcHandler(userRpcHandler);
        assertSame(userRpcHandler, view.getUserRpcHandler());
        clickButton(view.userRpcButton);
        assertTrue(userRpcHandler.handledEvent());

        StubbedViewEventHandler adminRpcHandler = new StubbedViewEventHandler();
        view.setAdminRpcHandler(adminRpcHandler);
        assertSame(adminRpcHandler, view.getAdminRpcHandler());
        clickButton(view.adminRpcButton);
        assertTrue(adminRpcHandler.handledEvent());

        StubbedViewEventHandler enabledUserRpcHandler = new StubbedViewEventHandler();
        view.setEnabledUserRpcHandler(enabledUserRpcHandler);
        assertSame(enabledUserRpcHandler, view.getEnabledUserRpcHandler());
        clickButton(view.enabledUserRpcButton);
        assertTrue(enabledUserRpcHandler.handledEvent());

        StubbedViewEventHandler enabledAdminRpcHandler = new StubbedViewEventHandler();
        view.setEnabledAdminRpcHandler(enabledAdminRpcHandler);
        assertSame(enabledAdminRpcHandler, view.getEnabledAdminRpcHandler());
        clickButton(view.enabledAdminRpcButton);
        assertTrue(enabledAdminRpcHandler.handledEvent());
    }

    /** Test get/set expected results. */
    public void testGetSetExpectedResults() {
        RpcTestPageView view = new RpcTestPageView();

        Map<String, String> original = view.getExpectedResults();
        assertEquals(5, original.size());
        assertEquals("", original.get("unprotectedRpc"));
        assertEquals("", original.get("userRpc"));
        assertEquals("", original.get("adminRpc"));
        assertEquals("", original.get("enabledUserRpc"));
        assertEquals("", original.get("enabledAdminRpc"));

        Map<String, String> changed = new HashMap<String, String>();
        changed.put("unprotectedRpc", "one");
        changed.put("userRpc", "two");
        changed.put("adminRpc", "three");
        changed.put("enabledUserRpc", "four");
        changed.put("enabledAdminRpc", "five");
        view.setExpectedResults(changed);

        Map<String, String> result = view.getExpectedResults();
        assertEquals(changed, result);
    }

    /** Test get/set actual results. */
    public void testGetSetActualResults() {
        RpcTestPageView view = new RpcTestPageView();

        assertEquals(null, view.getActualResult("bogus"));
        view.setActualResult("bogus", "one");
        assertEquals(null, view.getActualResult("bogus"));

        assertEquals("", view.getActualResult("unprotectedRpc"));
        view.setActualResult("unprotectedRpc", "two");
        assertEquals("two", view.getActualResult("unprotectedRpc"));

        assertEquals("", view.getActualResult("userRpc"));
        view.setActualResult("userRpc", "three");
        assertEquals("three", view.getActualResult("userRpc"));

        assertEquals("", view.getActualResult("adminRpc"));
        view.setActualResult("adminRpc", "four");
        assertEquals("four", view.getActualResult("adminRpc"));

        assertEquals("", view.getActualResult("enabledUserRpc"));
        view.setActualResult("enabledUserRpc", "five");
        assertEquals("five", view.getActualResult("enabledUserRpc"));

        assertEquals("", view.getActualResult("enabledAdminRpc"));
        view.setActualResult("enabledAdminRpc", "six");
        assertEquals("six", view.getActualResult("enabledAdminRpc"));
    }

    /** Assert that all of the widgets are configured properly. */
    private static void assertWidgetsSetProperly(RpcTestPageView view) {
        ExternalConstants constants = GWT.create(ExternalConstants.class);

        assertEquals(constants.rpcTest_unprotectedRpcLabel(), view.unprotectedRpcLabel.getText());
        assertEquals(constants.rpcTest_userRpcLabel(), view.userRpcLabel.getText());
        assertEquals(constants.rpcTest_adminRpcLabel(), view.adminRpcLabel.getText());
        assertEquals(constants.rpcTest_enabledUserRpcLabel(), view.enabledUserRpcLabel.getText());
        assertEquals(constants.rpcTest_enabledAdminRpcLabel(), view.enabledAdminRpcLabel.getText());

        assertEquals(constants.rpcTest_invokeRpcButton(), view.unprotectedRpcButton.getText());
        assertEquals(constants.rpcTest_invokeRpcButton(), view.userRpcButton.getText());
        assertEquals(constants.rpcTest_invokeRpcButton(), view.adminRpcButton.getText());
        assertEquals(constants.rpcTest_invokeRpcButton(), view.enabledUserRpcButton.getText());
        assertEquals(constants.rpcTest_invokeRpcButton(), view.enabledAdminRpcButton.getText());

        assertEquals(constants.rpcTest_unprotectedRpcTooltip(), view.unprotectedRpcButton.getTitle());
        assertEquals(constants.rpcTest_userRpcTooltip(), view.userRpcButton.getTitle());
        assertEquals(constants.rpcTest_adminRpcTooltip(), view.adminRpcButton.getTitle());
        assertEquals(constants.rpcTest_enabledUserRpcTooltip(), view.enabledUserRpcButton.getTitle());
        assertEquals(constants.rpcTest_enabledAdminRpcTooltip(), view.enabledAdminRpcButton.getTitle());

        assertEquals("", view.unprotectedRpcExpectedResult.getText());
        assertEquals("", view.unprotectedRpcActualResult.getText());
        assertEquals("", view.userRpcExpectedResult.getText());
        assertEquals("", view.userRpcActualResult.getText());
        assertEquals("", view.adminRpcExpectedResult.getText());
        assertEquals("", view.adminRpcActualResult.getText());
        assertEquals("", view.enabledUserRpcExpectedResult.getText());
        assertEquals("", view.enabledUserRpcActualResult.getText());
        assertEquals("", view.enabledAdminRpcExpectedResult.getText());
        assertEquals("", view.enabledAdminRpcActualResult.getText());
    }

}
