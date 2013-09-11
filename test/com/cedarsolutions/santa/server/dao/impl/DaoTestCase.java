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
package com.cedarsolutions.santa.server.dao.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import com.cedarsolutions.dao.gae.IDaoObjectifyService;
import com.cedarsolutions.dao.gae.impl.DaoObjectifyService;
import com.cedarsolutions.dao.gae.impl.ObjectifyProxy;
import com.cedarsolutions.junit.gae.DaoTestUtils;
import com.cedarsolutions.junit.util.TestUtils;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

/**
 * Test case that all DAO tests inherit from.
 *
 * <p>
 * This test case knows how to stub out the GAE datastore, via some utilities
 * that Google provides.
 * </p>
 *
 * <p>
 * Note that you must have appengine-api-stubs.jar and appengine-testing.jar on
 * your unit test classpath for this to work.  I can't find an easy way of making
 * that happen, except by copying the jars into the lib directory... so, updating
 * these jars is just one more thing that you have to do when a new AppEngine version
 * is released.
 * </p>
 *
 * @see <a href="http://code.google.com/appengine/docs/java/tools/localunittesting.html">Local Unit Testing for Java</a>
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public abstract class DaoTestCase {

    /** Path to the managed entities file on disk. */
    private static final String ENTITIES_PATH = "resources/managed-entities.txt";

    /** Singleton reference to DaoObjectifyService. */
    private static DaoObjectifyService DAO_OBJECTIFY_SERVICE;

    /** GAE helper class that stubs out datastore access. */
    protected final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    /** Set up logging for GAE, so log messages go to Log4J. */
    @BeforeClass
    public static void setUpGaeLogging() {
        // The datastore writes lots of crap to the log, which we can't control otherwise
        TestUtils.setUpGaeLogging();
    }

    /** Set up the stubbed AppEngine datastore. */
    @Before
    public void setupAppEngineDatastore() {
        helper.setUp();
    }

    /** Tear down the stubbed AppEngine datastore. */
    @After
    public void tearDownAppEngineDatastore() {
        helper.tearDown();
    }

    /** Get a singleton reference to the DaoObjectifyService that should be used for testing. */
    protected static IDaoObjectifyService getDaoObjectifyService() {
        // This has to be a singleton because we can only register classes
        // with Objectify once... otherwise, we get an error.

        if (DAO_OBJECTIFY_SERVICE == null) {
            DAO_OBJECTIFY_SERVICE = DaoTestUtils.createDaoObjectifyService(ENTITIES_PATH);
        }

        return DAO_OBJECTIFY_SERVICE;
    }

    /** Get a DaoObjectifyService that returns a mock ObjectifyProxy. */
    protected static IDaoObjectifyService getMockedDaoObjectifyService() {
        IDaoObjectifyService service = mock(IDaoObjectifyService.class);

        ObjectifyProxy proxy = mock(ObjectifyProxy.class);
        when(proxy.isTransactional()).thenReturn(false);
        when(service.getObjectify()).thenReturn(proxy);

        ObjectifyProxy transactional = mock(ObjectifyProxy.class);
        when(transactional.isTransactional()).thenReturn(true);
        when(service.getObjectifyWithTransaction()).thenReturn(transactional);

        return service;
    }

}
