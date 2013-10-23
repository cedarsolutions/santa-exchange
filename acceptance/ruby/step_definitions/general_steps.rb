# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
#
#              C E D A R
#          S O L U T I O N S       "Software done right."
#           S O F T W A R E
#
# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
#
# Copyright (c) 2013 Kenneth J. Pronovici.
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

When /^the user clicks the "([^"]*)" link$/ do |link|
    click_link link
end

When /^the user clicks the "([^"]*)" button$/ do |button|
    click_button button
end

Then /^the page should have a link "([^"]*)"$/ do |link|
    page.should have_link(link)
end

Then /^the page should have a button "([^"]*)"$/ do |button|
    page.should have_button(button)
end

Then /^the page should have content "([^"]*)"$/ do |content|
    page.should have_content(content)
end
