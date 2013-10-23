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

When(/^the user invokes the "(.*?)" on the RPC test page$/) do |rpc|
    click_button_by_id("RpcTestPageView_" + rpc + "Button")
end

Then(/^the "(.*?)" result should display "(.*?)"$/) do |rpc, string|
    check_label_content("RpcTestPageView_" + rpc + "ActualResult", string)
end

Then(/^the "(.*?)" result should be empty$/) do |rpc|
    check_label_content("RpcTestPageView_" + rpc + "ActualResult", "")
end
