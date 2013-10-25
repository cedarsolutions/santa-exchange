// vim: set ft=groovy ts=4 sw=4:
// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
// *
// *              C E D A R
// *          S O L U T I O N S       "Software done right."
// *           S O F T W A R E
// *
// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
// *
// * Copyright (c) 2013 Kenneth J. Pronovici.
// * All rights reserved.
// *
// * This program is free software; you can redistribute it and/or
// * modify it under the terms of the Apache License, Version 2.0.
// * See LICENSE for more information about the licensing terms.
// *
// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
// *
// * Author   : Kenneth J. Pronovici <pronovic@ieee.org>
// * Language : Gradle (>= 1.7)
// * Project  : Secret Santa Exchange
// *
// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.cedarsolutions.gradle

import org.gradle.api.Project
import org.gradle.api.InvalidUserDataException
import org.apache.tools.ant.taskdefs.condition.Os

/**
 * Plugin convention for projectCucumber.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
class ProjectCucumberPluginConvention {

    /** The project tied to this convention. */
    private Project project;

    /** Create a convention tied to a project. */
    public ProjectCucumberPluginConvention(Project project) {
        this.project = project
    }

    /**
     * Execute the cucumber tests, returning an ExecResult so caller can handle failures.
     * If you want the database to start over fresh, you need to reboot dev mode.
     * You can optionally provide either name or feature, but not both.
     * @param name     Specific name of a test, or a substring, as for the Cucumber --name option
     * @param feature  Path to a specific feature to execute, relative to the acceptance/cucumber directory
     * See: http://jeannotsweblog.blogspot.com/2013/02/cucumber-10-command-line.html
     */
    def execCucumber(String name, String feature) {
        verifyCucumberInstall()

        def command = [ project.projectCucumber.getCucumberPath(), "--require", project.projectCucumber.getRubySubdir(), ]

        if (name != null) {
            command += "--name"
            command += name.replaceAll('"', "")  // quotes cause problems, so just remove them and the test won't be found
            command += project.projectCucumber.getFeaturesSubdir()
        } else if (feature != null) {
            command += project.projectCucumber.getFeaturesSubdir() + "/" + feature
        } else {
            command += project.projectCucumber.getFeaturesSubdir()
        }

        return project.exec {
            workingDir = project.projectCucumber.getCucumberDir()
            ignoreExitValue = true
            executable = project.projectCucumber.getRubyPath()
            args = command
        }
    }

    /** Verify that all of the required components have been installed in order to run Cucumber. */
    def verifyCucumberInstall() {
        project.logger.lifecycle("Using Cucumber from: " + project.projectCucumber.getRubyInstallDir())
        verifyRuby()
        verifyGem()
        verifyCucumber()
        verifyGemVersions()
        project.logger.lifecycle("Cucumber install is ok.")
    }

    /** Install Cucumber, including Ruby and all of the other required dependencies.  */
    def installCucumber() {
        if (!isWindows()) {
            project.logger.error("The installCucumber task is only supported on the Windows platform, sorry.");
        } else {
            if (project.file("tools/cucumber").exists()) {
                project.logger.lifecycle("Cucumber already exists in tools/cucumber; to reinstall, remove first with 'gradle uninstallCucumber'.")
            } else {
                installRubyInterpreter()
                installRubyDevkit()
                installSeleniumGem()
                installRspecGem()
                installCapybaraGem()
                installCucumberGem()
                if (project.projectCucumber.getRubyInstallDir() == "tools/cucumber") {
                    verifyCucumberInstall()
                    project.logger.lifecycle("All Cucumber tooling has been installed.")
                } else {
                    project.logger.lifecycle("All Cucumber tooling has been installed.")
                    project.logger.lifecycle("However, your project is not configured to point to the new Cucumber.");
                    project.logger.lifecycle("Change your local.properties and run 'gradle verifyCucumber'.")
                }
            }
        }
    }

    /** Uninstall Cucumber, removing the install directory. */
    def uninstallCucumber() {
        if (project.file("tools").exists()) {
            if (!project.file("tools/cucumber").exists()) {
                project.logger.lifecycle("Cucumber tooling is apparently not installed.")
            } else {
                if (project.file("tools/cucumber/unins000.exe").exists()) {
                    try {
                        def devnull = new ByteArrayOutputStream()
                        project.exec {
                            standardOutput = devnull
                            executable = "tools/cucumber/unins000.exe" 
                            args = [ "/silent", ]
                        }
                    } catch (Exception e) { 
                        throw new InvalidUserDataException("Error uninstalling Ruby: " + e.getMessage(), e);
                    }
                }

                project.file("tools/cucumber").deleteDir()
                project.logger.lifecycle("All Cucumber tooling has been uninstalled.")
            }

            project.ant.delete(includeemptydirs : "true", quiet : "true") {
                fileset(dir: "tools", excludes : "**/*")  // remove tools only if empty
            }
        }
    }

    /** Verify that the Ruby interpreter is available. */
    private void verifyRuby() {
        if (!isRubyAvailable()) {
            project.logger.error("Ruby interpreter not available: " + project.projectCucumber.getRubyPath())
            project.logger.error("You must either run the installCucumber task or install Ruby and Cucumber manually.")
            throw new InvalidUserDataException("Ruby interpreter not available: " + project.projectCucumber.getRubyPath())
        }
    }

    /** Verify that the Ruby 'gem' tool is available. */
    private void verifyGem() {
        if (!isGemAvailable()) {
            project.logger.error("Ruby 'gem' tool not available: " + project.projectCucumber.getGemPath())
            project.logger.error("You must either run the installCucumber task or install Ruby and Cucumber manually.")
            throw new InvalidUserDataException("Ruby 'gem' tool not available: " + project.projectCucumber.getGemPath())
        }
    }

    /** Verify that the Cucumber tool is available. */
    private void verifyCucumber() {
        if (!isCucumberAvailable()) {
            project.logger.error("Cucumber tool not available: " + project.projectCucumber.getCucumberPath())
            project.logger.error("You must either run the installCucumber task or install Ruby and Cucumber manually.")
            throw new InvalidUserDataException("Cucumber tool not available: " + project.projectCucumber.getCucumberPath())
        }
    }

    /** Verify the versions of some specific gems. */
    private void verifyGemVersions() {
        def seleniumVersion = getGemVersion("selenium-webdriver")
        if (!seleniumVersion.startsWith("2.")) {
            project.logger.warn("Cucumber tests might not work due to version mismatch; expected Selenium 2.x, but got: " + seleniumVersion)
        }

        def rspecVersion = getGemVersion("rspec")
        if (rspecVersion != "2.14.1") {
            project.logger.warn("Cucumber tests might not work due to version mismatch; expected Rspec 2.14.1, but got: " + rspecVersion)
        }

        def capybaraVersion = getGemVersion("capybara")
        if (capybaraVersion != "2.1.0") {
            project.logger.warn("Cucumber tests might not work due to version mismatch; expected Capybara 2.1.0, but got: " + capybaraVersion)
        }

        def cucumberVersion = getGemVersion("cucumber")
        if (cucumberVersion != "1.3.8") {
            project.logger.warn("Cucumber tests might not work due to version mismatch; expected Cucumber 1.3.8, but got: " + cucumberVersion)
        }
    }

    /** Check whether the Ruby interpreter is available. */
    private boolean isRubyAvailable() {
        return isCommandAvailable(project.projectCucumber.getRubyPath(), [ "--version", ])
    }

    /** Check whether the Ruby 'gem' tool is available. */
    private boolean isGemAvailable() {
        return isCommandAvailable(project.projectCucumber.getGemPath(), [ "--version", ])
    }

    /** Check whether the Cucumber tool is available. */
    private boolean isCucumberAvailable() {
        return isCommandAvailable(project.projectCucumber.getRubyPath(), [ project.projectCucumber.getCucumberPath(), "--version", ])
    }

    /** Get the installed version of a Ruby gem. */
    private String getGemVersion(gem) {
        try {
            def stdout = new ByteArrayOutputStream()
            def stderr = new ByteArrayOutputStream()

            def result = project.exec {
                standardOutput = stdout
                errorOutput = stderr
                executable = project.projectCucumber.getGemPath()
                args = [ "list", gem, ]
            }

            def contents = stdout.toString()
            def regex = ~/(?s)(^${gem} [(])([.0-9]*)([)].*$)/       // for a string like "selenium-webdriver (2.37.0)"
            def matcher = regex.matcher(contents)
            if (matcher.matches()) {
                return matcher.group(2)
            } 
        } catch (Exception e) { }

        project.logger.error("Error checking version for Ruby gem: " + gem)
        project.logger.error("You must either run the installCucumber task or install Ruby and Cucumber manually.")
        throw new InvalidUserDataException("Error checking version for Ruby gem: " + gem)
    }

    /** 
     * Verify whether a command is available by executing it.
     * @param command     The executable to check
     * @param arguments   Arguments to pass to the command
     * @return True if the command could be executed, false otherwise.
     */
    private boolean isCommandAvailable(command, arguments) {
        try {
            def devnull = new ByteArrayOutputStream()

            def result = project.exec {
                standardOutput = devnull
                errorOutput = devnull
                executable = command
                args = arguments
            }

            return result.getExitValue() == 0
        } catch (Exception e) {
            return false
        }
    }

    /** Install the Ruby interpreter. */
    private void installRubyInterpreter() {
        try {
            project.logger.lifecycle("Installing Ruby...")

            project.file("build/tmp/installCucumber").deleteDir()
            project.file("build/tmp/installCucumber").mkdir()

            def devnull = new ByteArrayOutputStream()

            def rubyUrl = "http://dl.bintray.com/oneclick/rubyinstaller/rubyinstaller-1.9.3-p448.exe?direct"
            def rubyTarget = "build/tmp/installCucumber/ruby.exe"
            project.ant.get(src: rubyUrl, dest: rubyTarget)

            project.exec {
                standardOutput = devnull
                executable = "build/tmp/installCucumber/ruby.exe"
                args = [ "/silent", "/dir=tools/cucumber", ]
            }
        } catch (Exception e) { 
            throw new InvalidUserDataException("Error installing Ruby: " + e.getMessage(), e);
        } finally {
            project.file("build/tmp/installCucumber").deleteDir()
        }
    }

    /** Install the Ruby DevKit. */
    private void installRubyDevkit() {
        try {
            project.logger.lifecycle("Installing Ruby DevKit...")

            project.file("build/tmp/installCucumber").deleteDir()
            project.file("build/tmp/installCucumber").mkdir()

            def devnull = new ByteArrayOutputStream()

            def devkitUrl = "http://cloud.github.com/downloads/oneclick/rubyinstaller/DevKit-tdm-32-4.5.2-20111229-1559-sfx.exe"
            def devkitTarget = "build/tmp/installCucumber/devkit.exe"
            project.ant.get(src: devkitUrl, dest: devkitTarget)

            project.exec {
                standardOutput = devnull
                executable = "build/tmp/installCucumber/devkit.exe"
                args = [ "-y", "-ai", "-gm2", "-otools\\cucumber\\DevKit", ]
            }

            project.exec {
                standardOutput = devnull
                workingDir = "tools/cucumber/DevKit"
                executable = "tools/cucumber/bin/ruby.exe"
                args = [ "dk.rb", "init", ]
            }

            project.exec {
                standardOutput = devnull
                workingDir = "tools/cucumber/DevKit"
                executable = "tools/cucumber/bin/ruby.exe"
                args = [ "dk.rb", "review", ]
            }

            project.exec {
                standardOutput = devnull
                workingDir = "tools/cucumber/DevKit"
                executable = "tools/cucumber/bin/ruby.exe"
                args = [ "dk.rb", "install", ]
            }
        } catch (Exception e) { 
            throw new InvalidUserDataException("Error installing Ruby DevKit: " + e.getMessage(), e);
        } finally {
            project.file("build/tmp/installCucumber").deleteDir()
        }
    }

    /** Install the Selenium Ruby Gem. */
    private void installSeleniumGem() {
        try {
            project.logger.lifecycle("Installing Selenium gem...")
            def devnull = new ByteArrayOutputStream()
            project.exec {
                standardOutput = devnull
                errorOutput = devnull
                executable = "tools/cucumber/bin/gem.bat"
                args = [ "install", "selenium-webdriver", ]
            }
        } catch (Exception e) { 
            throw new InvalidUserDataException("Error installing Selenium gem: " + e.getMessage(), e);
        }
    }

    /** Install the Rspec Ruby Gem. */
    private void installRspecGem() {
        try {
            project.logger.lifecycle("Installing Rspec gem...")
            def devnull = new ByteArrayOutputStream()
            project.exec {
                standardOutput = devnull
                errorOutput = devnull
                executable = "tools/cucumber/bin/gem.bat"
                args = [ "install", "rspec", "-v", "2.14.1", ]
            }
        } catch (Exception e) { 
            throw new InvalidUserDataException("Error installing Rspec gem: " + e.getMessage(), e);
        }
    }

    /** Install the Capybara Ruby Gem. */
    private void installCapybaraGem() {
        try {
            project.logger.lifecycle("Installing Capybara gem...")
            def devnull = new ByteArrayOutputStream()
            project.exec {
                standardOutput = devnull
                errorOutput = devnull
                executable = "tools/cucumber/bin/gem.bat"
                args = [ "install", "capybara", "-v", "2.1.0", ]
            }
        } catch (Exception e) { 
            throw new InvalidUserDataException("Error installing Capybara gem: " + e.getMessage(), e);
        }
    }

    /** Install the Cucumber Ruby Gem. */
    private void installCucumberGem() {
        try {
            project.logger.lifecycle("Installing Cucumber gem...")
            def devnull = new ByteArrayOutputStream()
            project.exec {
                standardOutput = devnull
                errorOutput = devnull
                executable = "tools/cucumber/bin/gem.bat"
                args = [ "install", "cucumber", "-v", "1.3.8", ]
            }
        } catch (Exception e) { 
            throw new InvalidUserDataException("Error installing Cucumber gem: " + e.getMessage(), e);
        }
    }

    private boolean isWindows() {
        return Os.isFamily(Os.FAMILY_WINDOWS);
    }

}
