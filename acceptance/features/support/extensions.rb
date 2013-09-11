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

# Extensions that are available to all step implementations.
# @author Kenneth J. Pronovici <pronovic@ieee.org>
class WorldExtensions

    # Configuration accessible to all tests
    attr_accessor :test_config
    # Default constructor
    def initialize()
        @test_config = TestConfig.new
        configure_browser()
    end

    # Do any special browser-related configuration
    def configure_browser()
        if test_config.browser == "ie"
            Capybara.register_driver :selenium do |app|
                Capybara::Selenium::Driver.new(app, :browser=>:internet_explorer)
            end
        end
    end

    # Check that a label, identified by HTML id, has a specific value.
    # @param id      HTML id of the disclosure panel
    # @param value   Expected value
    # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
    # @raise Exception if the disclosure label is not found or did not have the right value
    def check_label_value(id, value, timeout=nil)
        element = find_by_id_and_class(id, "gwt-Label", timeout)
        if element.text != value
            raise "Label \"" + id + "\" does not have expected content"
        end
    end

    # Check that a top-level menu exists with a specific title.
    # @param bar     HTML id of the menu bar
    # @param menu    HTML id of the top-level menu
    # @param title   Expected title of the menu
    # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
    # @raise Exception if the top-level menu is not found or does not have the right title
    def check_menu_exists(bar, menu, title, timeout=nil)
        barElement = find_by_id_and_class(bar, "ss-MainMenuBar", timeout)
        menuElement = source_find_by_id_and_class(barElement, menu, "gwt-MenuItem", timeout)
        if menuElement.text != title
            raise "Menu item \"" + menu + "\" does not have expected title \"" + title + "\""
        end
    end

    # Check that a menu item exists with a specific title.
    # @param bar     HTML id of the menu bar
    # @param menu    HTML id of the top-level menu
    # @param item    HTML id of the menu item
    # @param title   Expected title of the menu item
    # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
    # @raise Exception if the menu item is not found in the hierarchy, or has the wrong title
    def check_menu_item_exists(bar, menu, item, title, timeout=nil)
        barElement = find_by_id_and_class(bar, "ss-MainMenuBar", timeout)
        menuElement = source_find_by_id_and_class(barElement, menu, "gwt-MenuItem", timeout)
        menuElement.click
        itemElement = find_by_id_and_class(item, "gwt-MenuItem", timeout)
        if itemElement.text != title
            raise "Menu item \"" + item + "\" does not have expected title \"" + title + "\""
        end
    end

    # Click a menu item.
    # @param bar    HTML id of the menu bar
    # @param menu   HTML id of the top-level menu
    # @param item   HTML id of the menu item
    # @raise Exception if the menu item is not found in the hierarchy
    def click_menu_item(bar, menu, item)
        barElement = find_by_id_and_class(bar, "ss-MainMenuBar")
        menuElement = source_find_by_id_and_class(barElement, menu, "gwt-MenuItem")
        menuElement.click
        itemElement = find_by_id_and_class(item, "gwt-MenuItem")
        itemElement.click
    end

    # Click on a named button in a pop-up.
    # @param id      HTML id of the popup
    # @param button  Title of the button to click
    def click_popup_button(id, button)
        popupElement = find_by_id_and_class(id, "ss-DialogBox");
        popupElement.find_button(button).click
    end

    # Click on a disclosure panel identified by HTML id.
    #
    # This encapsulates knowledge about how GWT implements a disclosure
    # panel.  As of 2.4.0, it's done as a set of nested tables plus a link
    # to accept the click action.  You have to click on some element within
    # the <a>.
    #
    # @param id  HTML id of the disclosure panel
    # @raise Exception if the disclosure panel is not found or could not be clicked.
    def click_disclosure_panel(id)
        find_by_id_and_class(id, "ss-DisclosurePanel").find(:xpath, "//a/table").click
    end

    # Check the state of a disclosure panel identified by HTML id.
    # @param id      HTML id of the disclosure panel
    # @param state   Expected state of the disclosure panel, either "open" or "closed"
    # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
    # @raise Exception if the disclosure panel is not found, or is not in the right state.
    def check_disclosure_panel_state(id, state, timeout=nil)
        find_by_id_and_class(id, "ss-DisclosurePanel-" + state, timeout)
    end

    # Check the state of a tab identified by HTML id.
    # @param id        HTML id of the tab
    # @param selected  True if the tab is expected to be selected, false otherwise
    # @param timeout   Timeout in seconds (defaults to Capybara.default_wait_time)
    # @raise Exception If the tab is not found, or is not in the right state
    def check_tab_state(id, selected, timeout=nil)
        if selected
            find_by_id_and_class(id, "gwt-TabLayoutPanelTab-selected", timeout)
        else
            find_by_id_and_class(id, "gwt-TabLayoutPanelTab", timeout)
            find_by_id_and_class_fail(id, "gwt-TabLayoutPanelTab-selected", timeout)
        end
    end

    # Check the state of a pop-up identified by HTML id.
    # @param id        HTML id of the pop-up
    # @param visible   True if the pop-up is expected to be visible, false otherwise
    # @param timeout   Timeout in seconds (defaults to Capybara.default_wait_time)
    # @raise Exception If the pop-up is not in the right state
    def check_popup_state(id, visible, timeout=nil)
        if visible
            find_by_id_and_class(id, "ss-DialogBox", timeout);
        else
            find_by_id_and_class_fail(id, "ss-DialogBox", timeout);
        end
    end

    # Does high-level checks that the pop-up validation state is correct.
    # @param id       Id of the pop-up to look in
    # @param summary  True if a validation summary is expected
    # @param details  True if validation details are expected
    # @param timeout  Timeout in seconds (defaults to Capybara.default_wait_time)
    # @raise Exception if expected validation data is not found.
    def check_popup_validation_state(id, summary, details, timeout=nil)
        timeout = normalize_timeout(timeout)

        popupElement = find_by_id_and_class(id, "ss-DialogBox", timeout);

        if summary
            source_find_by_id(popupElement, "ValidationErrorWidget_validationErrorSummary", timeout)
        else
            source_find_by_id_fail(popupElement, "ValidationErrorWidget_validationErrorSummary", timeout)
        end

        if details
            source_find_by_id(popupElement, "ValidationErrorWidget_validationErrorList", timeout)
        else
            source_find_by_id_fail(popupElement, "ValidationErrorWidget_validationErrorList", timeout)
        end
    end

    # Find an element by HTML id.
    # @param id      HTML id of the element to find
    # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
    # @return Element with the id.
    # @raise Exception if the element is not found
    def find_by_id(id, timeout=nil)
        return source_find_by_id(page, id, timeout)
    end

    # Find an element by HTML id.
    # @param source  Source to check in, like page or element
    # @param id      HTML id of the element to find
    # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
    # @return Element with the id and CSS class
    # @raise Exception if the element is not found
    def source_find_by_id(source, id, timeout=nil)
        timeout = normalize_timeout(timeout)

        for i in 1..timeout
            begin
                return source.find_by_id(id)
            rescue
            end

            sleep(1)
        end

        raise "Did not find element \"" + id + "\""
    end

    # Find an element by HTML id, with a specific css class.
    # The css class must be one of the css classes in the list of classes.
    # @param id      HTML id of the element to find
    # @param css     CSS class that the element must have
    # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
    # @return Element with the id and CSS class
    # @raise Exception if the element is not found
    def find_by_id_and_class(id, css, timeout=nil)
        return source_find_by_id_and_class(page, id, css, timeout)
    end

    # Find an element by HTML id, with a specific css class.
    # The css class must be one of the css classes in the list of classes.
    # @param source  Source to check in, like page or element
    # @param id      HTML id of the element to find
    # @param css     CSS class that the element must have
    # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
    # @return Element with the id and CSS class
    # @raise Exception if the element is not found
    def source_find_by_id_and_class(source, id, css, timeout=nil)
        timeout = normalize_timeout(timeout)

        for i in 1..timeout
            begin
                element = source.find_by_id(id)
                if element[:class].split(" ").include? css
                    return element
                end
            rescue
            end

            sleep(1)
        end

        raise "Did not find element \"" + id + "\" with CSS class \"" + css + "\""
    end

    # Try to find an element by HTML id, with a specific class, and fail if found.
    # @param id      HTML id of the element to find
    # @param css     CSS class that the element must have
    # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
    # @raise Exception if the element is found
    def find_by_id_and_class_fail(id, css, timeout=nil)
        source_find_by_id_and_class_fail(page, id, css, timeout)
    end

    # Try to find an element by HTML id, with a specific class, and fail if found.
    # @param source  Source to check in, like page or element
    # @param id      HTML id of the element to find
    # @param css     CSS class that the element must have
    # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
    # @raise Exception if the element is found
    def source_find_by_id_and_class_fail(source, id, css, timeout=nil)
        timeout = normalize_timeout(timeout)

        for i in 1..timeout
            begin
                element = source.find_by_id(id)
                if !element[:class].split(" ").include? css
                    return
                end
            rescue
                return
            end

            sleep(1)
        end

        raise "Found element \"" + id + "\" with CSS class \"" + css + "\" which was not supposed to exist"
    end

    # Try to find an element by HTML id, and fail if found.
    # @param source  Source to check in, like page or element
    # @param id      HTML id of the element to find
    # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
    # @raise Exception if the element is found
    def source_find_by_id_fail(source, id, timeout=nil)
        timeout = normalize_timeout(timeout)

        for i in 1..timeout
            begin
                source.find_by_id(id)
            rescue
                return
            end

            sleep(1)
        end

        raise "Found element \"" + id + "\" which was not supposed to exist"
    end

    # Press a key with focus on a pop-up.
    # @param id  HTML id of the pop-up
    # @param key Key to press, like :enter
    def press_popup_key(id, key)
        element = find_by_id_and_class(id, "ss-DialogBox");
        keypress_on(element, key)
    end

    # Send a keypress to a specific element.
    # @param element  Element to interact with
    # @param key      Key to press, like :return or :escape
    # See: http://stackoverflow.com/questions/2797752/cucumber-capybara-and-selenium-submiting-a-form-without-a-button
    def keypress_on(element, key)
        element.native.send_key(key)
    end

    # Assert that the current URL matches a pattern.
    #
    # Normally, we'd use something like: wait_until { current_url.should =~ /whatever/ }
    # However, that doesn't seem to notice that GWT is in the middle of forwarding between
    # internal URLs and it doesn't wait long enough to see the correct result.
    #
    # @param pattern    Pattern to check
    # @param timeout    Timeout in seconds (defaults to Capybara.default_wait_time)
    #
    # @raise  Exception if the URL does not match the pattern before timing out.
    def current_url_matches(pattern, timeout=nil)
        timeout = normalize_timeout(timeout)

        url = nil
        for i in 1..timeout
            url = current_url
            if url =~ /#{pattern}/
                return
            end
            sleep(1)
        end

        raise "Timeout waiting for current URL to match pattern: " + pattern + "\n\tgot: " + url
    end

    # Assert that the current URL equals a string.
    #
    # Normally, we'd use something like: wait_until { current_url.should == "whatever" }
    # However, that doesn't seem to notice that GWT is in the middle of forwarding between
    # internal URLs and it doesn't wait long enough to see the correct result.
    #
    # @param pattern    Pattern to check
    # @param timeout    Timeout in seconds (defaults to Capybara.default_wait_time)
    #
    # @raise  Exception if the URL does not match the pattern before timing out.
    def current_url_equals(value, timeout=nil)
        timeout = normalize_timeout(timeout)

        url = nil
        for i in 1..timeout
            url = current_url
            if url == value
                return
            end
            sleep(1)
        end

        raise "Timeout waiting for current URL to equal value: " + value + "\n\tgot: " + url
    end

    # Normalize a timeout value so it's at least 1 second
    def normalize_timeout(timeout)
        if timeout == nil
            timeout = Capybara.default_wait_time
        end

        if timeout < 1
            timeout = 1
        end

        return timeout
    end

end

# Configuration accessible to all tests, taken from cucumber.properties.
# See: http://snippets.dzone.com/posts/show/1311 for the original properties parser.
class TestConfig

    # Name of the properties file on disk
    PROPERTIES = "cucumber.properties";

    # Host that we're testing against, like "http://127.0.0.1:8888"
    attr_accessor :app_host

    # Module HTML file, like "SantaExchange.html
    attr_accessor :module_html

    # Base URL for testing, combination of app_host and module_html
    attr_accessor :base_url

    # Username that should be used for logging in adminstrators
    attr_accessor :admin_user

    # Username that should be used for logging in normal users
    attr_accessor :normal_user

    # Browser to use for tests
    attr_accessor :browser
    # Default constructor, which loads properties from disk.
    def initialize()
        properties = parse(PROPERTIES);
        @app_host = properties["application.host"]
        @module_html = properties["application.moduleHtml"]
        @base_url = self.app_host + "/" + self.module_html
        @admin_user = properties["credentials.admin"]
        @normal_user = properties["credentials.user"]
        @browser = properties["capybara.selenium.browser"]
    end

    # Parse a properties file from disk
    # @param Path to the file
    # @return Properties hash map parsed from the file
    def parse(filename)
        properties = {}
        File.open(filename, "r") do |file|
            file.read.each_line do |line|
                line.strip!
                if (line[0] != ?# and line[0] != ?=)
                    i = line.index("=")
                    if (i)
                        properties[line[0..i - 1].strip] = line[i + 1..-1].strip
                    else
                        properties[line] = ""
                    end
                end
            end
        end
        properties
    end

end
