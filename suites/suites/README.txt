This folder contains unit test suites.

In a normal (non-GWT) project, I would normally forego suite definitions and
just use Run As > JUnit Test at the package or source folder level.  For GWT
projects, it seems to work better to have explicit suite definitions.

There are two suites: UnitTestSuite runs the general unit tests. ClientTestSuite
runs the GWT client-side tests.

To run UnitTestSuite, right-click on it and choose Run As > JUnit Test.  To run
ClientTestSuite, right-click on it and choose Run As > GWT JUnit Test.
Sometimes, Eclipse gets confused and doesn't offer the Run As > GWT JUnit Test
option.  To work around this, open the class in the Java editor and and right
click on the class name instead.

For more information about testing, including code coverage, etc.  see
doc/README.tests.
