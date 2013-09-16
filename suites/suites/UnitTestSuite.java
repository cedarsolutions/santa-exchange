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
package suites;

import org.junit.extensions.cpsuite.ClasspathSuite;
import org.junit.extensions.cpsuite.ClasspathSuite.ClassnameFilters;
import org.junit.runner.RunWith;

// Note: originally, I used a classname filter like this to exclude the client tests:
//
//            @ClassnameFilters({ "!.*ClientTest" })
//
// However, I ended up with intermittent, unexplainable failures (i.e. NoClassDefFoundError
// when trying to mock Widget.class).  These errors happened only when I was running from
// a suite, not when I was running the test by itself.
//
// I eventually realized that I didn't really need the filter, because the client tests
// don't use the JUnit 4 test runner, and don't get picked up by this suite anyway.  So,
// I removed the filter.  However, I continued to see the problems.
//
// Oddly, when I changed the filter to the one shown below -- which as far as I can tell
// picks up exactly the same classes -- the errors went away.  I don't get it.

/**
 * Suite containing only unit tests (no GWT client tests).
 * See <a href="http://stackoverflow.com/questions/3257080/how-do-i-dynamically-create-a-test-suite-in-junit-4">StackOverflow</a>
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@RunWith(ClasspathSuite.class)
@ClassnameFilters({ "com\\..*Test" })
public class UnitTestSuite {
}
