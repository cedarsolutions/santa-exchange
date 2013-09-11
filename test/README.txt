This folder contains two kinds of tests: unit tests, and GWT client-side tests.

The same class might have both unit tests and client-side tests.  You can tell
apart client-side tests by their "ClientTest" suffix.  Given class "Whatever",
you'll find test classes named "WhateverTest" (unit tests) and "WhateverClientTest"
(client-side tests).

Client-side unit tests all inherit from ClientTestCase (which itself inherits
from Google's GWTTestCase).  When invoked, client-side tests use a customized
test runner provided by Google.  The test runner starts up a servlet
container and invokes the client side code in that container with a stubbed
browser.

To run unit tests for an individual class, right-click on the class and choose
Run As > JUnit Test.

To run client-side tests for an individual class, right-click on the class and
choose Run As > GWT JUnit Test.  Sometimes, Eclipse gets confused and doesn't
offer the Run As > GWT JUnit Test option.  To work around this, open the class
in the Java editor and and right click on the class name instead.

For more information about test suites, code coverage, etc. see
doc/README.tests.

