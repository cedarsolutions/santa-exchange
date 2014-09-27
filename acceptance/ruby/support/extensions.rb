# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
#
#              C E D A R
#          S O L U T I O N S       "Software done right."
#           S O F T W A R E
#
# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
#
# Copyright (c) 2013-2014 Kenneth J. Pronovici.
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

# Name of the project-wide build properties file on disk, relative to the acceptance directory
BUILD_PROPERTIES = "../build.properties";

# Name of the project-wide local properties file on disk, relative to the acceptance directory
LOCAL_PROPERTIES = "../local.properties";

# Name of the test properties file on disk, relative to the acceptance directory
TEST_PROPERTIES = "cucumber.properties";

# Setup the headless test runner, if necessary
def setup_headless()
   xvfbRunPath = parse([BUILD_PROPERTIES, LOCAL_PROPERTIES])["config_xvfbRunPath"]
   if !isWindows and (xvfbRunPath and xvfbRunPath.length > 0)
      require 'headless'
      puts "Running tests in headless mode."
      headless = Headless.new(display: 200, destroy_at_exit: false, reuse: true)
      headless.start
      return headless
   else
      return nil
   end
end

# Tear down the headless test runner, if necessary
def teardown_headless(headless)
   if headless
      headless.destroy()
   end
end

# Whether we're running on Windows
def isWindows()
    # See: http://stackoverflow.com/questions/4871309
    require 'rbconfig'
    return (RbConfig::CONFIG['host_os'] =~ /mswin|mingw|cygwin/)
end

# Parse one or more properties files from disk
# @param filenames  Filenames of properties files to load, in order
# @return Properties hash map parsed from the file, with later definitions overriding earlier ones.
def parse(filenames)
 properties = {}
 filenames.each do |filename|
   if (File.exists?(filename))
     file = File.open(filename, "r")
     file.read.each_line do |line|
       line.strip!
       if (line[0] != ?# and line[0] != ?=)
         i = line.index("=")
         if (i)
           key = line[0..i - 1].strip
           value = line[i + 1..-1].strip
           properties[key] = value.strip
         else
           key = line
           value = ""
           properties[key] = value
         end
       end
     end
     file.close
     file = nil
   end
 end
 properties
end

# Global hooks to configure headless state
headless = setup_headless()
at_exit do
   teardown_headless(headless)
end

# Extensions that are available to all step implementations.
# @author Kenneth J. Pronovici <pronovic@ieee.org>
class WorldExtensions

  # Configuration accessible to all tests
  attr_accessor :test_config

  # Default constructor
  def initialize()
    @test_config = TestConfig.new
    configure_capybara()
    configure_browser()
  end

  # Configure Capybara
  def configure_capybara()
    Capybara.run_server = false
    Capybara.default_driver = :selenium
    Capybara.default_selector = :css
    Capybara.default_wait_time = Integer(test_config.defaultAjaxWaitTime)
    Capybara.match = :prefer_exact
    Capybara.ignore_hidden_elements = true
  end

  # Do any special browser-related configuration
  def configure_browser()
    if test_config.browser == "ie"
      Capybara.register_driver :selenium do |app|
        Capybara::Selenium::Driver.new(app, :browser=>:internet_explorer)
      end
    end
  end

  # Normalize input that is supposed to contain newlines, but just has literal "\" followed by "n"
  def normalizeInput(s)
    if s == nil
      return ""
    else
      return s.gsub(/\\n/, "\n")
    end
  end

  # Normalize a string to have standard line-endings
  # http://stackoverflow.com/questions/1836046/normalizing-line-endings-in-ruby
  def normalizeNewlines(s)
    if s == nil
      return ""
    else
      return s.encode(s.encoding, :universal_newline => true)
    end
  end

  # Get the current date in YYYY-MM-DD format.
  def current_date()
    return Time.new.strftime("%Y-%m-%d")
  end

  # Set the value for an element identified by HTML id
  # @param id      HTML id of the text box
  # @param value   Value to set
  # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
  # @raise Exception if the text box is not found
  def set_element_value(id, value, timeout=nil)
    input = normalizeInput(value)
    find_by_id(id, timeout) # so we get an error if it isn't found
    fill_in id, :with => input
  end

  # Set the value for a text box identified by HTML id
  # @param id      HTML id of the text box
  # @param value   Value to set
  # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
  # @raise Exception if the text box is not found
  def set_text_box_value(id, value, timeout=nil)
    find_by_id_and_class(id, "gwt-TextBox", timeout) # so we get an error if it doesn't have the right type
    fill_in id, :with => value
  end

  # Set the value for a text area identified by HTML id
  # @param id      HTML id of the text area
  # @param value   Value to set
  # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
  # @raise Exception if the text is not found
  def set_text_area_value(id, value, timeout=nil)
    input = normalizeInput(value)
    find_by_id_and_class(id, "gwt-TextArea", timeout) # so we get an error if it doesn't have the right type
    fill_in id, :with => input
  end

  # Set the value for a date box identified by HTML id
  # @param id      HTML id of the date box
  # @param value   Value to set
  # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
  # @raise Exception if the date box is not found
  def set_date_box_value(id, value, timeout=nil)
    find_by_id_and_class(id, "gwt-DateBox", timeout) # so we get an error if it doesn't have the right type
    fill_in id, :with => value
  end

  # Set the value for a UTC date box identified by HTML id
  # @param id      HTML id of the date box
  # @param value   Value to set
  # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
  # @raise Exception if the date box is not found
  def set_utc_date_box_value(id, value, timeout=nil)
    set_date_box_value(id, value, timeout)   # to the browser, it looks like any other date box
  end

  # Set the value for a UTC time box identified by HTML id
  # @param id      HTML id of the date box
  # @param value   Value to set
  # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
  # @raise Exception if the date box is not found
  def set_utc_time_box_value(id, value, timeout=nil)
    find_by_id_and_class(id, "gwt-TimeBox", timeout) # so we get an error if it doesn't have the right type
    fill_in id, :with => value
  end

  # Set the value for a dropdown identified by HTML id
  # @param id      HTML id of the dropdown
  # @param value   User-visible value to set
  # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
  # @raise Exception if the dropdown is not found
  def set_dropdown_value(id, value, timeout=nil)
    find_by_id_and_class(id, "gwt-ListBox", 0) # so we get an error if it doesn't have the right type
    option = find(:xpath, "//*[@id='#{id}']/option[text()='#{value}']").text
    select(option, :from => id)
  end

  # Check that an element identified by HTML id, has a specific value.
  # @param id      HTML id of the label
  # @param css     CSS class that the element must have (optional)
  # @param value   Expected value
  # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
  # @raise Exception if the element is not found or did not have the right value
  def check_element_value(id, css, value, timeout=nil)
    if css == nil
      element = find_by_id(id, timeout)
    else
      element = find_by_id_and_class(id, css, timeout)
    end
    # We have to be a little fuzzy, because we don't know what kind of element it is.
    # <div> will have text, <textarea> will have value.
    input = normalizeInput(value)
    elementText = normalizeNewlines(element.text)
    elementValue = normalizeNewlines(element.value)
    if elementValue != input and elementText != input
      raise "Element \"" + id + "\" does not have expected text or value, expected [" + input + "], got value [" + elementValue + "], text [" + elementText + "]"
    end
  end

  # Check that an element identified by HTML id, is not empty.
  # @param id      HTML id of the label
  # @param css     CSS class that the element must have (optional)
  # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
  # @raise Exception if the element is not found or did not have the right value
  def check_element_value_not_empty(id, css, timeout=nil)
    if css == nil
      element = find_by_id(id, timeout)
    else
      element = find_by_id_and_class(id, css, timeout)
    end
    # We have to be a little fuzzy, because we don't know what kind of element it is.
    # <div> will have text, <textarea> will have value.
    elementText = normalizeNewlines(element.text)
    elementValue = normalizeNewlines(element.value)
    if elementValue == "" and elementText == ""
      raise "Element \"" + id + "\" is empty when it is not supposed to be."
    end
  end

  # Check that a label, identified by HTML id, has a specific value.
  # @param id      HTML id of the label
  # @param value   Expected value
  # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
  # @raise Exception if the label is not found or did not have the right value
  def check_label_value(id, value, timeout=nil)
    element = find_by_id_and_class(id, "gwt-Label", timeout)
    if element.text != value
      raise "Label \"" + id + "\" does not have expected content, expected [" + value + "], got [" + element.text + "]"
    end
  end

  # Check that a text box, identified by HTML id, has a specific value.
  # @param id      HTML id of the text box
  # @param value   Expected value
  # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
  # @raise Exception if the text box is not found or did not have the right value
  def check_text_box_value(id, value, timeout=nil)
    element = find_by_id_and_class(id, "gwt-TextBox", timeout)
    if element.value != value
      raise "Text box \"" + id + "\" does not have expected content, expected [" + value + "], got [" + element.value + "]"
    end
  end

  # Check that a integer text box, identified by HTML id, has a specific value.
  # @param id      HTML id of the text box
  # @param value   Expected value
  # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
  # @raise Exception if the text box is not found or did not have the right value
  def check_integer_box_value(id, value, timeout=nil)
    element = find_by_id_and_tag(id, "input", timeout)
    if element.value != value
      raise "Integer box \"" + id + "\" does not have expected content, expected [" + value + "], got [" + element.value + "]"
    end
  end

  # Check that a text area, identified by HTML id, has a specific value.
  # @param id      HTML id of the text area
  # @param value   Expected value
  # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
  # @raise Exception if the text area is not found or did not have the right value
  def check_text_area_value(id, value, timeout=nil)
    element = find_by_id_and_class(id, "gwt-TextArea", timeout)
    input = normalizeInput(value)
    elementValue = normalizeNewlines(element.value)
    if elementValue != input
      raise "Text area \"" + id + "\" does not have expected content, expected [" + input + "], got [" + elementValue + "]"
    end
  end

  # Check that a date box, identified by HTML id, has a specific value.
  # @param id      HTML id of the date box
  # @param value   Expected value
  # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
  # @raise Exception if the date box is not found or did not have the right value
  def check_date_box_value(id, value, timeout=nil)
    element = find_by_id_and_class(id, "gwt-DateBox", timeout)
    if element.value != value
      raise "Date box \"" + id + "\" does not have expected content, expected [" + value + "], got: [" + element.value + "]"
    end
  end

  # Check that a UTC date box, identified by HTML id, has a specific value.
  # @param id      HTML id of the date box
  # @param value   Expected value
  # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
  # @raise Exception if the date box is not found or did not have the right value
  def check_utc_date_box_value(id, value, timeout=nil)
    check_date_box_value(id, value, timeout)  # to the browser, it looks like any other date box
  end

  # Check that a UTC time box, identified by HTML id, has a specific value.
  # @param id      HTML id of the time box
  # @param value   Expected value
  # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
  # @raise Exception if the date box is not found or did not have the right value
  def check_utc_time_box_value(id, value, timeout=nil)
    element = find_by_id_and_class(id, "gwt-TimeBox", timeout)
    if element.value != value
      raise "Time box \"" + id + "\" does not have expected content, expected [" + value + "], got: [" + element.value + "]"
    end
  end

  # Check that a dropdown, identified by HTML id, has a specific value selected.
  # @param id      HTML id of the dropdown
  # @param value   Expected user-visible value
  # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
  # @raise Exception if the dropdown is not found or did not have the right selected value
  def check_dropdown_value(id, value, timeout=nil)
    find_by_id_and_class(id, "gwt-ListBox", 0) # so we get an error if it doesn't have the right type
    begin
      page.should have_select("#{id}", :selected => value)
    rescue
      raise "Dropdown \"" + id + "\" does not have selected value \"" + value + "\""
    end
  end

  # Check that a table exists with a specific id.
  # @param id      HTML id of the table
  # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
  # @raise Exception if the table is not found
  def check_table_exists(id, timeout=nil)
    find_by_id_and_tag(id, "table", timeout)
  end

  # Check that a table row exists and that its checkbox is checked as expected.
  # The checkbox is assumed to be in the first column.
  # The row is identified by looking for values in specific columns.
  # @param id      HTML id of the table
  # @param checked Whether the row should be checked
  # @param columns Hash from one-based column index to expected column value
  # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
  # @param recurse Whether to recurse and check a second time if a failure occurs
  # @raise Exception if the expected row is not found
  def check_table_row_checked(id, checked, columns, timeout=nil, recurse=true)
    begin
       page_all_xpath("//table[@id='" + id + "']/tbody/tr").each do |row|
         found = { }
         columns.keys.sort.each do |index|
           found[index] = false
           row.all(:xpath, "./td[" + index + "]/div/span[text()='" + columns[index] + "']").each do |column|
             found[index] = true
             break
           end
         end

         missingAny = false
         found.keys.each do |index|
           if not found[index]
             missingAny = true
             break
           end
         end

         if not missingAny
           row.all(:xpath, "./td[1]/div/input[@type='checkbox']").each do |checkbox|
             if (checked and checkbox.checked?()) or ((not checked) and (not checkbox.checked?()))
               return
             end
           end
         end
       end

       # I only want to sleep if the initial check doesn't work, because it slows down the tests
       if (recurse)
         timeout = normalize_timeout(timeout)
         sleep(timeout)
         check_table_row_checked(id, checked, columns, timeout, false)
       else
         raise "Did not find expected row in table \"" + id + "\": " + columns.inspect
       end
    rescue Selenium::WebDriver::Error::StaleElementReferenceError
      retry
    end
  end

  # Check contents of a table.
  # @param id      HTML id of the table
  # @param state   Expected state of the disclosure panel, either "empty" or "not empty"
  # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
  # @param recurse Whether to recurse and check a second time if a failure occurs
  # @raise Exception if table contents do not match expectations
  def check_table_contents(id, state, timeout=nil, recurse=true)
    begin
       count = page_all_xpath("//table[@id='" + id + "']/tbody/tr").count
       if state == "empty"
         if count == 1
           # Empty tables have a single row with a single indicator label in it
           indicator = page_all_xpath("//table[@id='" + id + "']/tbody/tr/td/div/div/div/div[@class='gwt-Label']").count
           if indicator != 1
             # I only want to sleep if the initial check doesn't work, because it slows down the tests
             if (recurse)
               timeout = normalize_timeout(timeout)
               sleep(timeout)
               check_table_contents(id, state, timeout, false)
             else
               raise "Expected empty table, but found rows"
             end
           end
         elsif count > 1
           # I only want to sleep if the initial check doesn't work, because it slows down the tests
           if (recurse)
             timeout = normalize_timeout(timeout)
             sleep(timeout)
             check_table_contents(id, state, timeout, false)
           else
             raise "Expected empty table, but found rows"
           end
         end
       elsif state == "not empty"
         if count == 1
           # Empty tables have a single row with a single indicator label in it
           indicator = page_all_xpath("//table[@id='" + id + "']/tbody/tr/td/div/div/div/div[@class='gwt-Label']").count
           if indicator == 1
             # I only want to sleep if the initial check doesn't work, because it slows down the tests
             if (recurse)
               timeout = normalize_timeout(timeout)
               sleep(timeout)
               check_table_contents(id, state, timeout, false)
             else
               raise "Expected non-empty table, but did not find rows"
             end
           end
         elsif count < 1
           # I only want to sleep if the initial check doesn't work, because it slows down the tests
           if (recurse)
             timeout = normalize_timeout(timeout)
             sleep(timeout)
             check_table_contents(id, state, timeout, false)
           else
             raise "Expected non-empty table, but did not find rows"
           end
         end
       end
    rescue Selenium::WebDriver::Error::StaleElementReferenceError
      retry
    end
  end

  # Check that a table row exists.
  # The row is identified by looking for values in specific columns.
  # @param id      HTML id of the table
  # @param columns Hash from one-based column index to expected column value
  # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
  # @param recurse Whether to recurse and check a second time if a failure occurs
  # @raise Exception if the expected row is not found
  def check_table_row_exists(id, columns, timeout=nil, recurse=true)
    begin
       page_all_xpath("//table[@id='" + id + "']/tbody/tr").each do |row|
         found = { }
         columns.keys.sort.each do |index|
           found[index] = false
           row.all(:xpath, "./td[" + index + "]/div[text()='" + columns[index] + "']").each do |column|
             found[index] = true
             break
           end
           row.all(:xpath, "./td[" + index + "]/div/span[text()='" + columns[index] + "']").each do |column|
             found[index] = true
             break
           end
         end

         missingAny = false
         found.keys.each do |index|
           if not found[index]
             missingAny = true
             break
           end
         end

         if not missingAny
           return
         end
       end

       # I only want to sleep if the initial check doesn't work, because it slows down the tests
       if (recurse)
         timeout = normalize_timeout(timeout)
         sleep(timeout)
         check_table_row_exists(id, columns, timeout, false)
       else
         raise "Did not find expected row in table \"" + id + "\": " + columns.inspect
       end
    rescue Selenium::WebDriver::Error::StaleElementReferenceError
      retry
    end
  end

  # Check that a table row does not exist.
  # The row is identified by looking for values in specific columns.
  # @param id      HTML id of the table
  # @param columns Hash from one-based column index to expected column value
  # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
  # @param recurse Whether to recurse and check a second time if a failure occurs
  # @raise Exception if the expected row is found
  def check_table_row_does_not_exist(id, columns, timeout=nil, recurse=true)
    begin
       page_all_xpath("//table[@id='" + id + "']/tbody/tr").each do |row|
         found = { }
         columns.keys.sort.each do |index|
           found[index] = false
           row.all(:xpath, "./td[" + index + "]/div[text()='" + columns[index] + "']").each do |column|
             found[index] = true
             break
           end
           row.all(:xpath, "./td[" + index + "]/div/span[text()='" + columns[index] + "']").each do |column|
             found[index] = true
             break
           end
         end

         missingAny = false
         found.keys.each do |index|
           if not found[index]
             missingAny = true
             break
           end
         end

         if not missingAny
           # I only want to sleep if the initial check doesn't work, because it slows down the tests
           if (recurse)
             timeout = normalize_timeout(timeout)
             sleep(timeout)
             check_table_row_does_not_exist(id, columns, timeout, false)
           else
             raise "Found row in table \"" + id + "\" that is not supposed to exist: " + columns.inspect
           end
         end
       end
    rescue Selenium::WebDriver::Error::StaleElementReferenceError
      retry
    end
  end

  # Check that all rows in a table match some specific criteria.
  # The criteria consists of values in specific columns.
  # @param id      HTML id of the table
  # @param columns Hash from one-based column index to expected column value
  # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
  # @param recurse Whether to recurse and check a second time if a failure occurs
  # @raise Exception if the expected row is not found
  def check_all_table_rows_match(id, columns, timeout=nil, recurse=true)
    begin
       page_all_xpath("//table[@id='" + id + "']/tbody/tr").each do |row|
         found = { }
         columns.keys.sort.each do |index|
           found[index] = false
           row.all(:xpath, "./td[" + index + "]/div[text()='" + columns[index] + "']").each do |column|
             found[index] = true
             break
           end
           row.all(:xpath, "./td[" + index + "]/div/span[text()='" + columns[index] + "']").each do |column|
             found[index] = true
             break
           end
         end

         missingAny = false
         found.keys.each do |index|
           if not found[index]
             missingAny = true
             break
           end
         end

         if missingAny
           # I only want to sleep if the initial check doesn't work, because it slows down the tests
           if (recurse)
             timeout = normalize_timeout(timeout)
             sleep(timeout)
             check_all_table_rows_match(id, columns, timeout, false)
           else
             raise "At least one row in table \"" + id + "\" does not match criteria: " + columns.inspect
           end
         end
       end
    rescue Selenium::WebDriver::Error::StaleElementReferenceError
      retry
    end
  end

  # Click a table row.
  # The first matching row is identified by looking for values in specific columns.
  # @param id      HTML id of the table
  # @param columns Hash from one-based column index to expected column value
  # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
  # @param recurse Whether to recurse and check a second time if a failure occurs
  # @raise Exception if the expected row is not found
  def click_table_row(id, columns, timeout=nil, recurse=true)
    begin
       page_all_xpath("//table[@id='" + id + "']/tbody/tr").each do |row|
         found = { }
         columns.keys.sort.each do |index|
           found[index] = false
           row.all(:xpath, "./td[" + index + "]/div[text()='" + columns[index] + "']").each do |column|
             found[index] = true
             break
           end
           row.all(:xpath, "./td[" + index + "]/div/span[text()='" + columns[index] + "']").each do |column|
             found[index] = true
             break
           end
         end

         missingAny = false
         found.keys.each do |index|
           if not found[index]
             missingAny = true
             break
           end
         end

         if not missingAny
           row.click()
           return
         end
       end

       # I only want to sleep if the initial check doesn't work, because it slows down the tests
       if (recurse)
         timeout = normalize_timeout(timeout)
         sleep(timeout)
         click_table_row(id, columns, timeout, false)
       else
         raise "Did not find expected row in table \"" + id + "\": " + columns.inspect
       end
    rescue Selenium::WebDriver::Error::StaleElementReferenceError
      retry
    end
  end

  # Click a column in a table row.
  # The first matching row is identified by looking for values in specific columns.
  # @param id      HTML id of the table
  # @param columns Hash from one-based column index to expected column value
  # @param index   One-based column index of the column to click
  # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
  # @param recurse Whether to recurse and check a second time if a failure occurs
  # @raise Exception if the expected row is not found
  def click_table_row_column(id, columns, index, timeout=nil, recurse=true)
    begin
       page_all_xpath("//table[@id='" + id + "']/tbody/tr").each do |row|
         found = { }
         columns.keys.sort.each do |index|
           found[index] = false
           row.all(:xpath, "./td[" + index + "]/div[text()='" + columns[index] + "']").each do |column|
             found[index] = true
             break
           end
           row.all(:xpath, "./td[" + index + "]/div/span[text()='" + columns[index] + "']").each do |column|
             found[index] = true
             break
           end
         end

         missingAny = false
         found.keys.each do |index|
           if not found[index]
             missingAny = true
             break
           end
         end

         if not missingAny
           row.all(:xpath, "./td[" + index + "]/div[1]").each do |column|
             column.click()
             return
           end
         end
       end

       # I only want to sleep if the initial check doesn't work, because it slows down the tests
       if (recurse)
         timeout = normalize_timeout(timeout)
         sleep(timeout)
         click_table_row_column(id, columns, index, timeout, false)
       else
         raise "Did not find expected row in table \"" + id + "\": " + columns.inspect
       end
    rescue Selenium::WebDriver::Error::StaleElementReferenceError
      retry
    end
  end

  # Click the checkbox on a table row.
  # The checkbox is assumed to be in the first column.
  # The first matching row is identified by looking for values in specific columns.
  # @param id      HTML id of the table
  # @param columns Hash from one-based column index to expected column value
  # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
  # @param recurse Whether to recurse and check a second time if a failure occurs
  # @raise Exception if the expected row is not found
  def click_table_row_checkbox(id, columns, timeout=nil, recurse=true)
    begin
       page_all_xpath("//table[@id='" + id + "']/tbody/tr").each do |row|
         found = { }
         columns.keys.sort.each do |index|
           found[index] = false
           row.all(:xpath, "./td[" + index + "]/div[text()='" + columns[index] + "']").each do |column|
             found[index] = true
             break
           end
           row.all(:xpath, "./td[" + index + "]/div/span[text()='" + columns[index] + "']").each do |column|
             found[index] = true
             break
           end
         end

         missingAny = false
         found.keys.each do |index|
           if not found[index]
             missingAny = true
             break
           end
         end

         if not missingAny
           row.all(:xpath, "./td[1]/div/input[@type='checkbox']").each do |checkbox|
             checkbox.set(true)
             return
           end
         end
       end

       # I only want to sleep if the initial check doesn't work, because it slows down the tests
       if (recurse)
         timeout = normalize_timeout(timeout)
         sleep(timeout)
         click_table_row_checkbox(id, columns, timeout, false)
       else
         raise "Did not find expected row in table \"" + id + "\": " + columns.inspect
       end
    rescue Selenium::WebDriver::Error::StaleElementReferenceError
      retry
    end
  end

  # Check that a button exists with a specific id.
  # @param id      HTML id of the button
  # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
  # @raise Exception if the button is not found with the correct name
  def check_button_exists(id, timeout=nil)
    find_by_id_and_class(id, "gwt-Button", timeout)
  end

  # Click a clickable label with a particular id.
  # @param id      HTML id of the button
  # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
  # @raise Exception if the button is not found with the correct name
  def click_label_by_id(id, timeout=nil)
    label = find_by_id_and_class(id, "ss-TextAsLink", timeout)
    label.click
  end

  # Click a button with a particular id.
  # @param id      HTML id of the button
  # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
  # @raise Exception if the button is not found with the correct name
  def click_button_by_id(id, timeout=nil)
    button = find_by_id_and_class(id, "gwt-Button", timeout)
    button.click
  end

  # Check that a top-level menu exists with a specific title.
  # @param bar     HTML id of the menu bar
  # @param menu    HTML id of the top-level menu
  # @param title   Expected title of the menu
  # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
  # @raise Exception if the top-level menu is not found or does not have the right title
  def check_menu_exists(bar, menu, title, timeout=nil)
    barElement = find_by_id_and_class(bar, "ss-MainMenuBar", timeout)

    # Every so often, a menu item isn't found by Selenium.  From watching the
    # test, it sometimes appears that the menu is selected when this happens.
    # I'm making a wild guess and hoping that checking both CSS classes might
    # improve things.  However, since the problem is so intermittent, it's hard
    # to tell whether it makes any difference.
    begin
      menuElement = source_find_by_id_and_class(barElement, menu, "gwt-MenuItem", timeout)
    rescue
      menuElement = source_find_by_id_and_class(barElement, menu, "gwt-MenuItem-selected", timeout)
    end

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

  # Check that a menu item does not exist with a specific title.
  # @param bar     HTML id of the menu bar
  # @param menu    HTML id of the top-level menu
  # @param item    HTML id of the menu item
  # @param title   Expected title of the menu item
  # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
  # @raise Exception if the menu item is not found in the hierarchy, or has the wrong title
  def check_menu_item_not_exists(bar, menu, item, title, timeout=nil)
    barElement = find_by_id_and_class(bar, "ss-MainMenuBar", timeout)
    menuElement = source_find_by_id_and_class(barElement, menu, "gwt-MenuItem", timeout)
    menuElement.click
    source_find_by_id_fail(item, "gwt-MenuItem", timeout)
  end

  # Click a menu item.
  # @param bar    HTML id of the menu bar
  # @param menu   HTML id of the top-level menu
  # @param item   HTML id of the menu item
  # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
  # @raise Exception if the menu item is not found in the hierarchy
  def click_menu_item(bar, menu, item, timeout=nil)
    barElement = find_by_id_and_class(bar, "ss-MainMenuBar", timeout)
    menuElement = source_find_by_id_and_class(barElement, menu, "gwt-MenuItem", timeout)
    menuElement.click
    itemElement = find_by_id_and_class(item, "gwt-MenuItem", timeout)
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

  # Check the state of a tab identified by HTML id.
  # @param id        HTML id of the tab
  # @param timeout   Timeout in seconds (defaults to Capybara.default_wait_time)
  # @raise Exception If the tab is not found, or is not in the right state
  def click_tab(id, timeout=nil)
    tab = find_by_id_and_class(id, "gwt-TabLayoutPanelTab", timeout)
    tab.click
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

  # Check content of a pop-up identified by HTML id.
  # @param id        HTML id of the pop-up
  # @param string    Expected string
  # @param timeout   Timeout in seconds (defaults to Capybara.default_wait_time)
  # @raise Exception If the pop-up is not in the right state
  def check_popup_content(id,  string, timeout=nil)
    popup = find_by_id_and_class(id, "ss-DialogBox", timeout);
    popup.should have_content(string)
  end

  # Check text of a label identified by HTML id.
  # @param id        HTML id of the label
  # @param text      Expected text
  # @param timeout   Timeout in seconds (defaults to Capybara.default_wait_time)
  # @raise Exception If the label does not have the right text
  def check_label_content(id,  text, timeout=nil)
    if (text == "")
      # Apparently, if a label is empty, GWT just doesn't render it
      find_by_id_and_class_fail(id, "gwt-Label", timeout)
    else
      label = find_by_id_and_class(id, "gwt-Label", timeout);
      label.should have_content(text)
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

  # Execute page.all() for an xpath, retrying in case of error.
  # @param xpath  The xpath path to look for
  # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
  # @param recurse Whether to recurse and check a second time if a failure occurs
  def page_all_xpath(xpath, timeout=nil, recurse=true)
    begin
      return page.all(:xpath, xpath)
    rescue
      # I only want to sleep if the initial check doesn't work, because it slows down the tests
      if (recurse)
        timeout = normalize_timeout(timeout)
        sleep(timeout)
        page_all_xpath(xpath, timeout, false)
      else
        raise "Failed to find element with xpath: [" + xpath + "]"
      end
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

  # Find an element by HTML id, with a specific tag type.
  # @param id      HTML id of the element to find
  # @param tag     The HTML tag that is expected, like "table"
  # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
  # @return Element with the id.
  # @raise Exception if the element is not found or does not have the right type
  def find_by_id_and_tag(id, tag, timeout=nil)
    return source_find_by_id_and_tag(page, id, tag, timeout)
  end

  # Find an element by HTML id, with a specific tag type.
  # @param source  Source to check in, like page or element
  # @param id      HTML id of the element to find
  # @param tag     The HTML tag that is expected, like "table"
  # @param timeout Timeout in seconds (defaults to Capybara.default_wait_time)
  # @return Element with the id and CSS class
  # @raise Exception if the element is not found or does not have the right type
  def source_find_by_id_and_tag(source, id, tag, timeout=nil)
    timeout = normalize_timeout(timeout)

    for i in 1..timeout
      begin
        element = source.find_by_id(id)
        if element.tag_name() == tag
          return element
        end
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

  # Press a key with focus on a named element.
  # @param id  HTML id of the element
  # @param key Key to press, like :enter
  def press_element_key(id, key)
    element = find_by_id(id);
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

# Configuration accessible to all tests.
# See: http://snippets.dzone.com/posts/show/1311 for the original properties parser.
class TestConfig

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

  # Username that is disallowed
  attr_accessor :disallowed_user

  # Username that is locked
  attr_accessor :locked_user

  # Username that is locked
  attr_accessor :invalid_user

  # Browser to use for tests
  attr_accessor :browser

  # Amount of time Capybara should wait for AJAX requests to complete
  attr_accessor :defaultAjaxWaitTime

  # Default constructor, which loads properties from disk.
  def initialize()
    buildProperties = parse([BUILD_PROPERTIES, LOCAL_PROPERTIES]);
    testProperties = parse([TEST_PROPERTIES, LOCAL_PROPERTIES]);
    @app_host = testProperties["config_appHost"]
    @module_html = buildProperties["config_appStartupUrl"]
    @base_url = self.app_host + "/" + self.module_html
    @admin_user = testProperties["config_credentialsAdmin"]
    @normal_user = testProperties["config_credentialsUser"]
    @browser = testProperties["config_capybaraSeleniumBrowser"]
    @defaultAjaxWaitTime = testProperties["config_defaultAjaxWaitTime"]
  end

end
