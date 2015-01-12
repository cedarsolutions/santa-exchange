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

When /^the user logs in with normal user credentials$/ do
    click_button("Sign In With Google")
    fill_in("email", {:with => test_config.normal_user})
    click_button("Log In")
end

Then /^the internal title bar should say "([^"]*)"$/ do |title|
    check_label_value("InternalView_title", title)
end

Then /^the internal menu should display the normal user email address$/ do
    check_menu_exists("InternalView_menuBar", "InternalView_mainMenu", test_config.normal_user)
end

Then /^the internal menu should display the admin email address$/ do
    check_menu_exists("InternalView_menuBar", "InternalView_mainMenu", test_config.admin_user)
end

Then /^the "([^"]*)" internal menu option should be visible$/ do |option|
    if option == "Admin Landing Page"
        check_menu_item_exists("InternalView_menuBar", "InternalView_mainMenu", "InternalView_adminLandingPageItem", option)
    elsif option == "About Santa Exchange"
        check_menu_item_exists("InternalView_menuBar", "InternalView_mainMenu", "InternalView_aboutItem", option)
    elsif option == "Report a Problem"
        check_menu_item_exists("InternalView_menuBar", "InternalView_mainMenu", "InternalView_bugReportItem", option)
    elsif option == "Source Code"
        check_menu_item_exists("InternalView_menuBar", "InternalView_mainMenu", "InternalView_sourceCodeItem", option)
    elsif option == "Sign Out"
        check_menu_item_exists("InternalView_menuBar", "InternalView_mainMenu", "InternalView_logoutItem", option)
    else
        raise "Test definition problem: unknown menu option \"" + option + "\""
    end
end

When /^the user chooses the "([^"]*)" internal menu option$/ do |option|
    if option == "Admin Landing Page"
        click_menu_item("InternalView_menuBar", "InternalView_mainMenu", "InternalView_adminLandingPageItem")
    elsif option == "About Santa Exchange"
        click_menu_item("InternalView_menuBar", "InternalView_mainMenu", "InternalView_aboutItem")
    elsif option == "Report a Problem"
        click_menu_item("InternalView_menuBar", "InternalView_mainMenu", "InternalView_bugReportItem")
    elsif option == "Source Code"
        click_menu_item("InternalView_menuBar", "InternalView_mainMenu", "InternalView_sourceCodeItem")
    elsif option == "Sign Out"
        click_menu_item("InternalView_menuBar", "InternalView_mainMenu", "InternalView_logoutItem")
    else
        raise "Test definition problem: unknown menu option \"" + option + "\""
    end
end
