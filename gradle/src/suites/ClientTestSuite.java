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
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package suites;

import junit.framework.Test;

import com.cedarsolutions.junit.gwt.GwtTestSuiteBuilder;
import com.google.gwt.junit.tools.GWTTestSuite;

/**
 * Suite containing GWT client tests only (for build process).
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ClientTestSuite extends GWTTestSuite {

    /** Create a suite of client tests. */
    public static Test suite() throws Exception {
        // The path below must match up with where build.gradle writes the test classes,
        // or nothing will happen.  As of this writing, they go to build/classes/test.
        return GwtTestSuiteBuilder.generateSuite("suites.ClientTestSuite", "build/classes/test");
    }

}
