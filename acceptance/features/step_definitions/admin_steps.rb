# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
#
#              C E D A R
#          S O L U T I O N S       "Software done right."
#           S O F T W A R E
#
# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
#
# Copyright (c) 2012 Kenneth J. Pronovici.
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

When /^the user logs in with admin credentials$/ do
    click_button("LoginSelector_openIdButton")
    fill_in("email", {:with => test_config.admin_user})
    check("isAdmin")
    click_button("Log In")
end

Then /^the user should be taken to the "([^"]*)" admin tab$/ do |tab|
    if tab == "Admin Home"
        current_url_matches(test_config.base_url + "#(admin\/adminLandingPage|admin\/adminHome)")
        check_tab_state("AdminLandingPageView_tabPanel_homeTab", true)
        check_tab_state("AdminLandingPageView_tabPanel_auditTab", false)
        check_tab_state("AdminLandingPageView_tabPanel_userTab", false)
    elsif tab == "Auditing"
        current_url_matches(test_config.base_url + "#admin\/adminAudit")
        check_tab_state("AdminLandingPageView_tabPanel_homeTab", false)
        check_tab_state("AdminLandingPageView_tabPanel_auditTab", true)
        check_tab_state("AdminLandingPageView_tabPanel_userTab", false)
    elsif tab == "User Maintenance"
        current_url_matches(test_config.base_url + "#admin\/adminUser")
        check_tab_state("AdminLandingPageView_tabPanel_homeTab", false)
        check_tab_state("AdminLandingPageView_tabPanel_auditTab", false)
        check_tab_state("AdminLandingPageView_tabPanel_userTab", true)
    else
        raise "Test definition problem: unknown tab \"" + tab + "\""
    end
end

Then /^the admin title bar should say "([^"]*)"$/ do |title|
    check_label_value("AdminView_title", title)
end

Then /^the admin menu should display the admin email address$/ do
    check_menu_exists("AdminView_menuBar", "AdminView_mainMenu", test_config.admin_user)
end

Then /^the "([^"]*)" admin menu option should be visible$/ do |option|
    if option == "Internal Landing Page"
        check_menu_item_exists("AdminView_menuBar", "AdminView_mainMenu", "AdminView_internalLandingPageItem", option)
    elsif option == "Application Dashboard"
        check_menu_item_exists("AdminView_menuBar", "AdminView_mainMenu", "AdminView_dashboardItem", option)
    elsif option == "About Santa Exchange"
        check_menu_item_exists("AdminView_menuBar", "AdminView_mainMenu", "AdminView_aboutItem", option)
    elsif option == "Report a Problem"
        check_menu_item_exists("AdminView_menuBar", "AdminView_mainMenu", "AdminView_bugReportItem", option)
    elsif option == "Source Code"
        check_menu_item_exists("AdminView_menuBar", "AdminView_mainMenu", "AdminView_sourceCodeItem", option)
    elsif option == "Sign Out"
        check_menu_item_exists("AdminView_menuBar", "AdminView_mainMenu", "AdminView_logoutItem", option)
    else
        raise "Test definition problem: unknown menu option \"" + option + "\""
    end
end

When /^the user chooses the "([^"]*)" admin menu option$/ do |option|
    if option == "Internal Landing Page"
        click_menu_item("AdminView_menuBar", "AdminView_mainMenu", "AdminView_internalLandingPageItem")
    elsif option == "Application Dashboard"
        click_menu_item("AdminView_menuBar", "AdminView_mainMenu", "AdminView_dashboardItem")
    elsif option == "About Santa Exchange"
        click_menu_item("AdminView_menuBar", "AdminView_mainMenu", "AdminView_aboutItem")
    elsif option == "Report a Problem"
        click_menu_item("AdminView_menuBar", "AdminView_mainMenu", "AdminView_bugReportItem")
    elsif option == "Source Code"
        click_menu_item("AdminView_menuBar", "AdminView_mainMenu", "AdminView_sourceCodeItem")
    elsif option == "Sign Out"
        click_menu_item("AdminView_menuBar", "AdminView_mainMenu", "AdminView_logoutItem")
    else
        raise "Test definition problem: unknown menu option \"" + option + "\""
    end
end

When /^the user views (a|an) "([^"]*)" admin tab bookmark$/ do |adjective, bookmark|
    if bookmark == "Admin Home"
        visit test_config.base_url + "#admin/adminHome"
    elsif bookmark == "Auditing"
        visit test_config.base_url + "#admin/adminAudit"
    elsif bookmark == "User Maintenance"
        visit test_config.base_url + "#admin/adminUser"
    else
        raise "Test definition problem: unknown tab bookmark \"" + bookmark + "\""
    end
end
