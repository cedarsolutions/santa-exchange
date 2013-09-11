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
package suites;

import junit.framework.Test;

import com.cedarsolutions.junit.gwt.GwtTestSuiteBuilder;
import com.google.gwt.junit.tools.GWTTestSuite;

/**
 * Suite containing GWT client tests only.
 *
 * <p>
 * This suite is intended for use with Eclipse, and it uses the classes
 * that Eclipse has already compiled in the -bin directories.
 * </p>
 *
 * <p>
 * Note: oftentimes the Google Eclipse plugin gets confused and decides
 * not to offer the GWT JUnit Test option off Run As.  If that happens,
 * you can usually right-click on the GWTTestSuite reference below and that
 * will get you the correct runner.  If that doesn't work, you'll have
 * to restart Eclipse.
 * </p>
 *
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ClientTestSuite extends GWTTestSuite {

    /** Create a suite of client tests. */
    public static Test suite() throws Exception {
        return GwtTestSuiteBuilder.generateSuite("suites.ClientTestSuite", "test-bin");
    }

}
