# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
#
#              C E D A R
#          S O L U T I O N S       "Software done right."
#           S O F T W A R E
#
# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
#
# Copyright (c) 2012-2013 Kenneth J. Pronovici.
# All rights reserved.
#
# This program is free software; you can redistribute it and/or
# modify it under the terms of the Apache License, Version 2.0.
# See LICENSE for more information about the licensing terms.
#
# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
#
# Author   : Kenneth J. Pronovici <pronovic@ieee.org>
# Language : Ruby (>= 1.9), for use with Cucumber and Capybara
# Project  : Acceptance test suite
#
# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #

# This is not really the "right" way to disable specific checks in Cucumber.
# The recommended way is to tag tests and then disable those tests via the
# command line using --tag.  However, I want to disable tests based on the
# browser, and I don't want to have to change configuration in two different
# places to get a single result.  So, what this does is to effectively disable
# the tests by marking them as pending.  See acceptance/README.txt.
Given /^that external link checking is enabled$/ do
    if test_config.browser == "ie"
        pending "External link checking is disabled because browser is Internet Explorer"
    end
end

When /^the user views the canonical site url$/ do
    visit test_config.base_url
end

When /^the user views (a|an) "([^"]*)" bookmark$/ do |adjective, bookmark|
    if bookmark == "Admin Landing Page"
        visit test_config.base_url + "#admin/adminLandingPage"
    elsif bookmark == "Internal Landing Page"
        visit test_config.base_url + "#internal/internalLandingPage"
    elsif bookmark == "External Landing Page"
        visit test_config.base_url + "#external/externalLandingPage"
    elsif bookmark == "Base Landing Page"
        visit test_config.base_url + "#landingPage"
    else
        raise "Test definition problem: unknown bookmark \"" + bookmark + "\""
    end
end

When /^the user views an invalid ([^"]*) bookmark$/ do |bookmark|
    if bookmark == "top-level"
        visit test_config.base_url + "#blech"
    elsif bookmark == "internal"
        visit test_config.base_url + "#internal/blech"
    elsif bookmark == "admin"
        visit test_config.base_url + "#admin/blech"
    else
        raise "Test definition problem: unknown invalid bookmark \"" + bookmark + "\""
    end
end

Then /^the user should be taken to the external landing page$/ do
    current_url_matches(test_config.base_url + "#external\/externalLandingPage")
end

Then /^the user should be taken to the internal landing page$/ do
    current_url_matches(test_config.base_url + "#(internal\/internalLandingPage|internal\/exchangeList)")
end

Then /^the user should be taken to the login required page$/ do
    page.should have_content("Login Required")
    page.should have_content("The page you requested requires you to be logged in.")
end

Then /^the user should be taken to the external Google Code page$/ do
    current_url_equals("http://code.google.com/p/santa-exchange/")
end

Then /^the user should be taken to the external Apache license page$/ do
    current_url_equals("http://www.apache.org/licenses/LICENSE-2.0")
end

Then /^the user should be taken to the external Google Code site for Santa Exchange$/ do
    current_url_equals("http://code.google.com/p/santa-exchange/")
end

Then /^the user should be taken to the external application dashboard page$/ do
    # Technically, we go to "https://appengine.google.com/dashboard?&app_id=santa-exchange-hrd"
    # However, Google will make us authenticate, so the URL below is the actual place we end up
    current_url_equals("https://accounts.google.com/ServiceLogin?service=ah&passive=true&continue=https%3A%2F%2Fappengine.google.com%2F_ah%2Fconflogin%3Fcontinue%3Dhttps%3A%2F%2Fappengine.google.com%2Fdashboard%253F%2526app_id%253Dsanta-exchange-hrd&ltmpl=ae")
end
