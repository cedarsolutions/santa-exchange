# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
#
#              C E D A R
#          S O L U T I O N S       "Software done right."
#           S O F T W A R E
#
# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
#
# Copyright (c) 2014 Kenneth J. Pronovici.
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
# Project  : Custom Cucumber output formatter
#
# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #

# This is a customized Cucumber output formatter that writes output to the
# screen in a legible manner (summarized by feature file) and also writes a
# re-run file to disk, equivalent to the one written by the standard Cucumber
# re-run formatter.  It also adds some additional debug information about
# undefined tests, something I find sorely lacking in the standard output.
#
# The problem with the standard Cucumber re-run formatter is that you don't get
# any useful output from the entire first test run.  I want to see output while
# the tests are executing, so I have some idea what is going on.  So, this
# formatter takes the name of an output file from the environment and writes it
# manually, while still writing output to the terminal.
#
# This implementation is based in part on several examples:
#
# https://github.com/cucumber/cucumber/blob/master/lib/cucumber/formatter/progress.rb
# https://github.com/cucumber/cucumber/blob/master/lib/cucumber/formatter/rerun.rb
# https://github.com/tpope/fivemat/blob/master/lib/fivemat/cucumber.rb

require 'rubygems'
require 'cucumber/formatter/console'
require 'cucumber/formatter/io'

module Formatter
  class Cedar

    include ::Cucumber::Formatter::Console
    include ::Cucumber::Formatter::Io
    attr_reader :runtime
    def initialize(runtime, path_or_io, options)
      @runtime = runtime
      @io = ensure_io(path_or_io, "delta")
      @options = options

      @file_names = []
      @file_colon_lines = Hash.new{|h,k| h[k] = []}

      @rerunio = nil
      if ENV['RERUN_FILE']
        @rerunio = File.open(ENV['RERUN_FILE'], 'w')
      end
    end

    def before_features(features)
      print_profile_information
    end

    def after_features(features)
      if (@rerunio)
        @rerunio.close()
      end

      print_summary(features)
    end

    def before_feature(feature_element)
      @lines = []
      @file = feature_element.file
      @attempted = 0
      @failed = 0
      @pending = 0
      @undefined = 0
      @exceptions = []
      @start = Time.now
    end

    def after_feature(feature)
      if (@rerunio)
        unless @lines.empty?
          after_first_time do
            @rerunio.print ' '
          end
          @rerunio.print "#{@file}:#{@lines.join(':')}"
          @rerunio.flush
        end
      end

      elapsed = (((Time.now - @start) * 100).to_i / 100.0)
      @io.print "Feature \"#{feature.short_name}\": attempted=#{@attempted}, failed=#{@failed}, pending=#{@pending}, undefined=#{@undefined} (#{elapsed}s)\r\n";
      if @failed > 0
        @io.print "\r\n";
        @exceptions.each do |(exception, status)|
          print_exception(exception, status, 2)
          @io.print "\r\n";
        end
      end
      @io.flush
    end

    def before_feature_element(feature_element)
      @rerun = false
      @attempted += 1
    end

    def after_feature_element(feature_element)
      if (@rerun || feature_element.failed?) && !(Cucumber::Ast::ScenarioOutline === feature_element)
        @lines << feature_element.line
      end
    end

    def after_table_row(table_row)
      return unless @in_examples and Cucumber::Ast::OutlineTable::ExampleRow === table_row
      unless @header_row
        if table_row.failed?
          @rerun = true
          @lines << table_row.line
        end
      end

      @header_row = false if @header_row
    end

    def before_examples(*args)
      @header_row = true
      @in_examples = true
    end

    def after_examples(*args)
      @in_examples = false
    end

    def before_table_row(table_row)
      return unless @in_examples
    end

    def step_name(keyword, step_match, status, source_indent, background, file_colon_line)
      @rerun = true if [:failed, :pending, :undefined].index(status)
      @pending += 1 if [:pending].index(status)
      @undefined += 1 if [:undefined].index(status)
    end

    def exception(exception, status)
      @failed += 1
      @exceptions << [exception, status]
    end

    def after_first_time
      yield if @not_first_time
      @not_first_time = true
    end

    def print_summary(features)
      @io.flush
      @io.print "\r\n";

      print_undefined()
      @io.print "\r\n";

      print_snippets(@options)
      @io.print "\r\n";

      print_stats(features, @options)
      @io.print "\r\n";

      @io.flush
    end

    def print_undefined()
      undefined = runtime.steps(:undefined)
      return if undefined.empty?

      @io.print "Undefined steps:\r\n\r\n"
      undefined.map do |step|
        @io.print step.file_colon_line + ": " + step.name
        @io.print "\r\n"
      end
    end

  end
end
