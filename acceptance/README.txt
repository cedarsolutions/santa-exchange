WHAT IS THIS?

This folder contains behavior-driven acceptance tests implemented in Ruby using
Cucumber and Capybara.


HOW TO RUN THE TESTS

In order to run these tests, you need to install the Cucumber and Capybara
tools, as well as a Ruby interpreter.  To develop tests, it's also useful to
install the Ruby language plugin for Eclipse.  See install.txt in the notes
folder for information about how to install these tools.

All of the acceptance tests are invoked via the standard build script.
This script does a fresh build of the entire source tree (separate from the
directories that are used by Eclipse), then boots the App Engine development
mode server and executes the Cucumber tests.

If you are developing tests, you don't always need to go through the complete
build/start/execute/stop process.  You can also run the steps by hand: build
the code, start the server, and then use execute the tests.  The simplest way
to do this is probably using the build.xml Ant script.   


WHAT IS COVERED, AND OTHER NOTES

What you see here is a partial suite which covers only a subset of the user
interface: mainly the landing pages, menu options, bookmark functionality, etc.
There are no tests that stress the real "guts" of the system, like the edit
exchange functionality or the user maintenance features in the admin module.

I started down the path of writing automated acceptance tests well after Santa
Exchange was feature-complete.  So, unlike the JUnit tests, these tests were
written after the fact and not as part of the development process.  A client of
mine was considering using Cucumber for their own GWT project.  I decided to
use Santa Exchange as a test bed, to convince myself whether Cucumber and
Capybara represent a viable mechanism for writing client-side acceptance tests.

I do think that the experiment was a success, especially after I integrated the
ArcBees IdHandler functionality to generate legible HTML ids on my GWT widgets.
Capybara coupled with Selenium seems to be a reliable way to control a browser
and interact with a GWT application.  On the other hand, I suspect that these
tests are likely to be brittle, because they depend on the GWT implementation
of each widget.  For the same reason, they sometimes feel kind of ugly, because
I have to work around GWT behaviors that are outside of my control.

Besides all that, it takes a lot of manual effort to write these tests.  I
found that I needed to walk through each scenario by hand first, so I could
write the feature in a legible way.  Then, I needed to capture implementation-
specific details (i.e. how to find the right button, div, table row or
whatever) using Firebug before I could write the test steps in Ruby using
Capybara.

In the end, I can't really justify the time it would take to finish writing
tests to cover all of the user interface functionality, especially since I have
well over 90% code coverage from the existing JUnit test suite.  So, I am going
to settle for partial coverage from Cucumber, which gives me a solid set of
smoke tests that I can run prior to each release.
