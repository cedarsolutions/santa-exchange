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
package com.cedarsolutions.santa.client.junit;

import static org.mockito.Mockito.reset;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.cedarsolutions.client.gwt.validation.ValidationUtils;
import com.cedarsolutions.junit.gwt.GwtStubbedTestRunner;
import com.cedarsolutions.junit.util.TestUtils;
import com.cedarsolutions.santa.client.SantaExchangeConfig;
import com.cedarsolutions.santa.client.SantaExchangeConstants;
import com.cedarsolutions.santa.client.SantaExchangeMessages;
import com.cedarsolutions.santa.client.common.view.ViewUtils;
import com.cedarsolutions.santa.client.common.widget.WidgetUtils;
import com.cedarsolutions.santa.client.rpc.util.RpcUtils;
import com.google.gwt.core.client.GWT;

/**
 * Test case for stubbed client testing, rather than relying on GWTTestCase.
 *
 * <p>
 * This test case is intended to be used when testing presenters, event handlers
 * and the like.  It doesn't really work for testing views or widgets, because
 * many of the required classes can't be mocked successfully.
 * </p>
 *
 * <p>
 * Speaking of mocking... we've actually been kind of lucky so far, because
 * it is possible to mock the Widget class using Mockito, and that's the main
 * client class that presenters need to interact with.  If Google ever screws
 * with the definition for Widget (i.e. makes it final, or makes some of its
 * methods final), we're going to have problems. We can partially work around
 * this by trying to build our own interfaces using IsWidget rather than Widget,
 * but that doesn't help when extending classes like Panel (which uses the two
 * rather interchangeably).
 * </p>
 *
 * <p>
 * For more information about test suites, code coverage, etc. see
 * <code>doc/README.tests.</code>
 * </p>
 *
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@RunWith(GwtStubbedTestRunner.class)
public abstract class StubbedClientTestCase {

    protected static SantaExchangeMessages SANTA_EXCHANGE_MESSAGES;
    protected static SantaExchangeConfig SANTA_EXCHANGE_CONFIG;
    protected static SantaExchangeConstants SANTA_EXCHANGE_CONSTANTS;

    /** Set up logging for GAE, so log messages go to Log4J. */
    @BeforeClass
    public static void setUpGaeLogging() {
        // The datastore writes lots of crap to the log, which we can't control otherwise
        TestUtils.setUpGaeLogging();
    }

    /** Apply stubbed resources to this test. */
    @BeforeClass
    public static void applyStubbedResources() {
        SANTA_EXCHANGE_MESSAGES = GWT.create(SantaExchangeMessages.class);
        SANTA_EXCHANGE_CONFIG = GWT.create(SantaExchangeConfig.class);
        SANTA_EXCHANGE_CONSTANTS = GWT.create(SantaExchangeConstants.class);
    }

    /** Reset any known singleton mocks before each test case runs. */
    @Before
    public void resetSingletonMocks() {
        reset(WidgetUtils.getInstance());
        reset(ViewUtils.getInstance());
        reset(ValidationUtils.getInstance());
        reset(RpcUtils.getInstance());
    }

}
