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

When /^the user clicks the Login Selector disclosure panel$/ do
    click_disclosure_panel("LoginSelector_openIdDisclosure")
end

When /^the user clicks the Already Logged In disclosure panel$/ do
    click_disclosure_panel("LoginSelector_continueDisclosure")
end

Then /^the Login Selector disclosure panel should be ([^"]*)$/ do |state|
    check_disclosure_panel_state("LoginSelector_openIdDisclosure", state)
end

Then /^the Already Logged In disclosure panel should be ([^"]*)$/ do |state|
    check_disclosure_panel_state("LoginSelector_continueDisclosure", state)
end
