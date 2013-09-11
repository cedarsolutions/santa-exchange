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

Then /^the "([^"]*)" pop\-up should be visible$/ do |popup|
    if popup == "Welcome"
        check_popup_state("WelcomePopup", true)
    elsif popup == "Error Occurred"
        check_popup_state("ErrorPopup", true)
    elsif popup == "About Santa Exchange"
        check_popup_state("AboutPopup", true)
    elsif popup == "Report a Problem"
        check_popup_state("BugReportDialogView", true)
    else
        raise "Test definition problem: unknown pop-up \"" + popup + "\""
    end
end

Then /^the "([^"]*)" pop\-up should be hidden$/ do |popup|
    if popup == "Welcome"
        check_popup_state("WelcomePopup", false)
    elsif popup == "Error Occurred"
        check_popup_state("ErrorPopup", false)
    elsif popup == "About Santa Exchange"
        check_popup_state("AboutPopup", false)
    elsif popup == "Report a Problem"
        check_popup_state("BugReportDialogView", false)
    else
        raise "Test definition problem: unknown pop-up \"" + popup + "\""
    end
end

When /^the user clicks the "([^"]*)" pop\-up "([^"]*)" button$/ do |popup, button|
    if popup == "Welcome"
        click_popup_button("WelcomePopup", button)
    elsif popup == "Error Occurred"
        click_popup_button("ErrorPopup", button)
    elsif popup == "About Santa Exchange"
        click_popup_button("AboutPopup", button)
    elsif popup == "Report a Problem"
        click_popup_button("BugReportDialogView", button)
        sleep(1) # give the dialog some time to process
    else
        raise "Test definition problem: unknown pop-up \"" + popup + "\""
    end
end

When /^the user presses the Enter key on the "([^"]*)" pop\-up$/ do |popup|
    if popup == "Welcome"
        press_popup_key("WelcomePopup", :return)
    elsif popup == "Error Occurred"
        press_popup_key("ErrorPopup", :return)
    elsif popup == "About Santa Exchange"
        press_popup_key("AboutPopup", :return)
    elsif popup == "Report a Problem"
        press_popup_key("BugReportDialogView", :return)
    else
        raise "Test definition problem: unknown pop-up \"" + popup + "\""
    end
end

When /^the user presses the Escape key on the "([^"]*)" pop\-up$/ do |popup|
    if popup == "Welcome"
        press_popup_key("WelcomePopup", :escape)
    elsif popup == "Error Occurred"
        press_popup_key("ErrorPopup", :escape)
    elsif popup == "About Santa Exchange"
        press_popup_key("AboutPopup", :escape)
    elsif popup == "Report a Problem"
        press_popup_key("BugReportDialogView", :escape)
    else
        raise "Test definition problem: unknown pop-up \"" + popup + "\""
    end
end

Then /^validation errors should be displayed on the "([^"]*)" pop\-up$/ do |popup|
    if popup == "Report a Problem"
        check_popup_validation_state("BugReportDialogView", true, true)
    else
        raise "Test definition problem: unknown pop-up \"" + popup + "\""
    end
end

When /^the user fills in valid bug report data in the "([^"]*)" pop\-up$/ do |popup|
    if popup == "Report a Problem"
        dialog = page.find_by_id("BugReportDialogView")
        dialog.fill_in("BugReportDialogView_emailAddress", { :with => "email" })
        dialog.fill_in("BugReportDialogView_problemSummary", { :with => "summary" })
        dialog.fill_in("BugReportDialogView_detailedDescription", { :with => "description" })
    else
        raise "Test definition problem: unknown pop-up \"" + popup + "\""
    end
end
