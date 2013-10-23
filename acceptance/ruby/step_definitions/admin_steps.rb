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

When /^the user logs in with admin credentials$/ do
    click_button("LoginSelector_openIdButton")
    fill_in("email", {:with => test_config.admin_user})
    check("isAdmin")
    click_button("Log In")
end

When /^the user logs in with locked credentials$/ do
    click_button("LoginSelector_openIdButton")
    fill_in("email", {:with => test_config.locked_user})
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

When /^the user clicks on the "([^"]*)" admin tab$/ do |tab|
    if tab == "Admin Home"
        click_tab("AdminLandingPageView_tabPanel_homeTab");
    elsif tab == "Auditing"
        click_tab("AdminLandingPageView_tabPanel_auditTab");
    elsif tab == "User Maintenance"
        click_tab("AdminLandingPageView_tabPanel_userTab");
    else
        raise "Test definition problem: unknown tab \"" + tab + "\""
    end
end

When /^the "(.*?)" table should exist on the "(.*?)" admin tab$/ do |table, tab|
    if tab == "Auditing"
        if table == "Audit Events"
            check_table_exists("AuditTabView_table");
        else
            raise "Test definition problem: unknown table \"" + table + "\" on tab \"" + tab + "\""
        end
    elsif tab == "User Maintenance"
        if table == "Users"
            check_table_exists("UserTabView_table");
        else
            raise "Test definition problem: unknown table \"" + table + "\" on tab \"" + tab + "\""
        end
    else
        raise "Test definition problem: unknown tab \"" + tab + "\""
    end
end

When /^the "(.*?)" button should exist on the "(.*?)" admin tab$/ do |button, tab|
    if tab == "User Maintenance"
        if button == "Delete"
            check_button_exists("UserTabView_deleteButton");
        elsif button == "Lock"
            check_button_exists("UserTabView_lockButton");
        elsif button == "Unlock"
            check_button_exists("UserTabView_unlockButton");
        else
            raise "Test definition problem: unknown button \"" + button + "\" on tab \"" + tab + "\""
        end
    else
        raise "Test definition problem: unknown tab \"" + tab + "\""
    end
end

When(/^the user clicks the "(.*?)" button on the "(.*?)" admin tab$/) do |button, tab|
    if tab == "User Maintenance"
        if button == "Delete"
            click_button("UserTabView_deleteButton");
        elsif button == "Lock"
            click_button("UserTabView_lockButton");
        elsif button == "Unlock"
            click_button("UserTabView_unlockButton");
        else
            raise "Test definition problem: unknown button \"" + button + "\" on tab \"" + tab + "\""
        end
    else
        raise "Test definition problem: unknown tab \"" + tab + "\""
    end
end

Then(/^the "(.*?)" table on the "(.*?)" admin tab should have a row for user "(.*?)" that is (checked|unchecked)$/) do |table, tab, user, state|
    checked = state == "checked"
    if tab == "User Maintenance"
        if table == "Users"
            check_table_row_checked("UserTabView_table", checked, { "2" => user })
        else
            raise "Test definition problem: unknown table \"" + table + "\" on tab \"" + tab + "\""
        end
    else
        raise "Test definition problem: unknown tab \"" + tab + "\""
    end
end

Then(/^the "(.*?)" table on the "(.*?)" admin tab should not have a row for user "(.*?)"$/) do |table, tab, user|
    if tab == "User Maintenance"
        if table == "Users"
            check_table_row_does_not_exist("UserTabView_table", { "2" => user })
        else
            raise "Test definition problem: unknown table \"" + table + "\" on tab \"" + tab + "\""
        end
    else
        raise "Test definition problem: unknown tab \"" + tab + "\""
    end
end

Then(/^the "(.*?)" table on the "(.*?)" admin tab should show the "(.*?)" user as (locked|not locked)$/) do |table, tab, user, state|
    locked = state == "locked" ? "Yes" : "No"
    if tab == "User Maintenance"
        if table == "Users"
            check_table_row_exists("UserTabView_table", { "2" => user, "9" => locked })
        else
            raise "Test definition problem: unknown table \"" + table + "\" on tab \"" + tab + "\""
        end
    else
        raise "Test definition problem: unknown tab \"" + tab + "\""
    end
end

Then(/^the "(.*?)" table on the "(.*?)" admin tab should show the "(.*?)" user as (admin|not admin)$/) do |table, tab, user, state|
    admin = state == "admin" ? "Yes" : "No"
    if tab == "User Maintenance"
        if table == "Users"
            check_table_row_exists("UserTabView_table", { "2" => user, "8" => admin })
        else
            raise "Test definition problem: unknown table \"" + table + "\" on tab \"" + tab + "\""
        end
    else
        raise "Test definition problem: unknown tab \"" + tab + "\""
    end
end

When(/^the user clicks the checkbox for the "(.*?)" user on the "(.*?)" table on the "(.*?)" admin tab$/) do |user, table, tab|
    if tab == "User Maintenance"
        if table == "Users"
            click_table_row_checkbox("UserTabView_table", { "2" => user })
        else
            raise "Test definition problem: unknown table \"" + table + "\" on tab \"" + tab + "\""
        end
    else
        raise "Test definition problem: unknown tab \"" + tab + "\""
    end
end
