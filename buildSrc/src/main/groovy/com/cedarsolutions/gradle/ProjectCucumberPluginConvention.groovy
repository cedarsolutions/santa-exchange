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
        if (project.projectCucumber.getRubyInstallDir() != "tools/cucumber") {
            project.logger.error("Project is configured to use Ruby from: " + project.projectCucumber.getRubyInstallDir())
            throw new InvalidUserDataException("Before running installing Cucumber, re-configure project to use tools/cucumber")
        } else {
            if (project.file(project.projectCucumber.getRubyInstallDir()).exists()) {
                throw new InvalidUserDataException("Cucumber is already installed.  To reinstall, use 'gradle reinstallCucumber'.")
            } else {
                installJRuby(project.projectCucumber.getJRubyDownloadUrl())
                installGem("selenium-webdriver", project.projectCucumber.getSeleniumVersion())
                installGem("rspec", project.projectCucumber.getRspecVersion())
                installGem("capybara", project.projectCucumber.getCapybaraVersion())
                installGem("cucumber", project.projectCucumber.getCucumberVersion())
                verifyCucumberInstall()
                project.logger.lifecycle("All Cucumber tooling has been installed.")
            }
        }
    }

    /** Uninstall Cucumber, removing the install directory. */
    def uninstallCucumber() {
        if (project.file("tools").exists()) {
            if (project.file("tools/cucumber").exists()) {
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
        if (project.projectCucumber.getSeleniumVersion() != null) {
            def seleniumVersion = getGemVersion("selenium-webdriver")
            if (!seleniumVersion != project.projectCucumber.getSeleniumVersion()) {
                def version = project.projectCucumber.getSeleniumVersion()
                project.logger.warn("Cucumber tests might not work due to version mismatch; expected Selenium ${version}, but got: " + seleniumVersion)
            }
        }

        if (project.projectCucumber.getRspecVersion() != null) {
            def rspecVersion = getGemVersion("rspec")
            if (rspecVersion != project.projectCucumber.getRspecVersion()) {
                def version = project.projectCucumber.getRspecVersion()
                project.logger.warn("Cucumber tests might not work due to version mismatch; expected Rspec ${version}, but got: " + rspecVersion)
            }
        }

        if (project.projectCucumber.getCapybaraVersion() != null) {
            def capybaraVersion = getGemVersion("capybara")
            if (capybaraVersion != project.projectCucumber.getCapybaraVersion()) {
                def version = project.projectCucumber.getCapybaraVersion()
                project.logger.warn("Cucumber tests might not work due to version mismatch; expected Capybara ${version}, but got: " + capybaraVersion)
            }
        }

        if (project.projectCucumber.getCucumberVersion() != null) {
            def cucumberVersion = getGemVersion("cucumber")
            if (cucumberVersion != project.projectCucumber.getCucumberVersion()) {
                def version = project.projectCucumber.getCucumberVersion()
                project.logger.warn("Cucumber tests might not work due to version mismatch; expected Cucumber ${version}, but got: " + cucumberVersion)
            } 
        }
    }

    /** Check whether the Ruby interpreter is available. */
    private boolean isRubyAvailable() {
        return isCommandAvailable(project.projectCucumber.getRubyPath(), [ "--version", ])
    }

    /** Check whether the Ruby 'gem' tool is available. */
    private boolean isGemAvailable() {
        return isCommandAvailable(project.projectCucumber.getRubyPath(), [ project.projectCucumber.getGemPath(), "--version", ])
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
                executable = project.projectCucumber.getRubyPath()
                args = [ project.projectCucumber.getGemPath(), "list", gem, ]
            }

            def contents = stdout.toString()
            def regex = ~/(?s)(^${gem} [(])([.0-9]*)([,)].*$)/       // for a string like "selenium-webdriver (2.37.0)"
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

    /** Install JRuby from a particular URL, expected to be a .tar.gz file. */
    private void installJRuby(url) {
        try {
            project.logger.lifecycle("Installing JRuby...")

            project.file("build/tmp/jruby").deleteDir()
            project.file("build/tmp/jruby").mkdir()
            project.ant.get(src: url, dest: "build/tmp/jruby/jruby.tar.gz")
            if (!project.file("build/tmp/jruby/jruby.tar.gz").exists()) {
                throw new InvalidUserDataException("Error downloading JRuby, file not retrieved.")
            }

            if (isWindows()) {
                project.ant.untar(src: "build/tmp/jruby/jruby.tar.gz", dest: "build/tmp/jruby", compression: "gzip")
            } else {
                // The Ant task doesn't preserve permissions, so use tar on other platforms
                def stdout = new ByteArrayOutputStream()
                def stderr = new ByteArrayOutputStream()
                def result = project.exec {
                    ignoreExitValue = true
                    workingDir = "build/tmp/jruby"
                    standardOutput = stdout
                    errorOutput = stderr
                    executable = "tar"
                    args = [ "zxvf", "jruby.tar.gz", ]
                }
            }

            project.file("tools").mkdir()
            project.file("build/tmp/jruby").eachDir() { dir ->
                dir.renameTo("tools/cucumber") // there should be only one directory, like jruby-1.7.6
            }
        } catch (Exception e) { 
            throw new InvalidUserDataException("Error installing JRuby: " + e.getMessage(), e);
        } finally {
            project.file("build/tmp/jruby").deleteDir()
        }
    }

    /** Install a named gem, optionally specifying a version. */
    private void installGem(gem, version) {
        try {
            def arguments = [ project.projectCucumber.getGemPath(), "install", gem, ]
            if (version != null) {
                arguments += [ "-v", version ]
            }

            if (version != null) {
                project.logger.lifecycle("Installing gem: " + gem + ", version " + version)
            } else {
                project.logger.lifecycle("Installing gem: " + gem + ", latest version")
            }
            
            def devnull = new ByteArrayOutputStream()
            project.exec {
                standardOutput = devnull
                errorOutput = devnull
                executable = project.projectCucumber.getRubyPath()
                args = arguments
            }
        } catch (Exception e) { 
            throw new InvalidUserDataException("Error installing gem: " + e.getMessage(), e);
        }
    }

    private boolean isWindows() {
        return Os.isFamily(Os.FAMILY_WINDOWS);
    }

}
