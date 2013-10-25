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

/**
 * Plugin convention for projectGwt.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
class ProjectGwtPluginConvention {

    /** The project tied to this convention. */
    private Project project;

    /** Create a convention tied to a project. */
    public ProjectGwtPluginConvention(Project project) {
        this.project = project
    }

    /** Clean up the test directories that are created in the workspace. */
    public void cleanupCacheDirs() {
        project.file("gwt-unitCache").deleteDir()
        project.file("www-test").deleteDir()
        project.file("build/gwtUnitCache").deleteDir()
    }

    /** Get the location of the exploded App Engine SDK directory on disk. */
    public String getAppEngineSdkDir() {
        return project.file(project.gaeDownloadSdk.explodedSdkDirectory.getPath() + 
                            "/appengine-java-sdk-" + 
                            project.projectGwt.getAppEngineVersion()).canonicalPath
    }

    /** Get the location of the appengine agent jar. */
    public String getAppEngineAgentJar() {
        return project.file(getAppEngineSdkDir() + "/lib/agent/appengine-agent.jar").canonicalPath
    } 

    /** Boot the development mode server. */
    public void bootDevmode() {
        def warDir = project.gaeExplodeWar.explodedWarDirectory.getPath()
        def cacheDir = project.file(warDir + "/WEB-INF/appengine-generated").canonicalPath
        def classesDir = project.file(warDir + "/WEB-INF/classes").canonicalPath
        def libDir = project.file(warDir + "/WEB-INF/lib").canonicalPath
        def agentJar = getAppEngineAgentJar()
        def serverClass = "com.google.gwt.dev.DevMode"
        def launcher = "com.google.appengine.tools.development.gwt.AppEngineLauncher"

        cleanupCacheDirs()

        project.ant.java(classname: serverClass, dir: warDir, fork: "true", spawn: "true") {
            jvmarg(value: "-Xmx" + project.projectGwt.getDevmodeServerMemory())
            jvmarg(value: "-javaagent:" + agentJar)
            arg(line: "-startupUrl")
            arg(value: project.projectGwt.getAppStartupUrl())
            arg(line: "-war")
            arg(value: warDir)
            arg(line: "-logLevel")
            arg(value: "INFO")
            arg(line: "-codeServerPort")
            arg(value: project.projectGwt.getDevmodeCodeserverPort())
            arg(line: "-port")
            arg(value: project.projectGwt.getDevmodeServerPort())
            arg(line: "-server")
            arg(line: launcher)
            arg(value: project.projectGwt.getAppEntryPoint())
            classpath() {
                fileset(dir: libDir, includes : "*.jar")
                pathelement(location: classesDir)
                project.configurations.devmodeRuntime.each { jar -> pathelement(location: jar.canonicalPath) }
                project.configurations.providedRuntime.each { jar -> pathelement(location: jar.canonicalPath) }
                project.sourceSets.main.java.srcDirs.each { dir -> pathelement(location: dir) }
            }
        }
    }

    /** Kill the development mode server. */
    public void killDevmode() {
        // Unfortunately, this only works on Windows for now
        project.ant.exec(executable: "taskkill") {
            arg(value: "/fi")
            arg(value: '"Windowtitle eq GWT Development Mode"')
        }
    }

    /** Reboot devmode, stopping and then starting it. */
    public void rebootDevmode() {
        project.convention.plugins.projectGwt.killDevmode()
        project.convention.plugins.projectGwt.bootDevmode()
    }

    /** Wait for devmode to start, based on configuration. */
    def waitForDevmode() {
        sleep project.projectGwt.getServerWait() * 1000;  // wait for dev mode to finish booting
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

        def command = [ project.projectGwt.getCucumberPath(), "--require", "ruby", ]

        if (name != null) {
            command += "--name"
            command += name.replaceAll('"', "")  // quotes cause problems, so just remove them and the test won't be found
            command += "cucumber"
        } else if (feature != null) {
            command += "cucumber/" + feature
        } else {
            command += "cucumber"
        }

        return project.exec {
            workingDir = "acceptance"
            ignoreExitValue = true
            executable = project.projectGwt.getRubyPath()
            args = command
        }
    }

    /** Verify that all of the required components have been installed in order to run Cucumber. */
    def verifyCucumberInstall() {
        verifyRuby()
        verifyGem()
        verifyCucumber()
        verifyGemVersions()
        project.logger.lifecycle("Cucumber install is ok.")
    }

    /** Verify that the Ruby interpreter is available. */
    private void verifyRuby() {
        if (!isRubyAvailable()) {
            project.logger.error("Ruby interpreter not available: " + project.projectGwt.getRubyPath())
            project.logger.error("You must either run the installCucumber task or install Ruby and Cucumber manually.")
            throw new InvalidUserDataException("Ruby interpreter not available: " + project.projectGwt.getRubyPath())
        }
    }

    /** Verify that the Ruby 'gem' tool is available. */
    private void verifyGem() {
        if (!isGemAvailable()) {
            project.logger.error("Ruby 'gem' tool not available: " + project.projectGwt.getGemPath())
            project.logger.error("You must either run the installCucumber task or install Ruby and Cucumber manually.")
            throw new InvalidUserDataException("Ruby 'gem' tool not available: " + project.projectGwt.getGemPath())
        }
    }

    /** Verify that the Cucumber tool is available. */
    private void verifyCucumber() {
        if (!isCucumberAvailable()) {
            project.logger.error("Cucumber tool not available: " + project.projectGwt.getCucumberPath())
            project.logger.error("You must either run the installCucumber task or install Ruby and Cucumber manually.")
            throw new InvalidUserDataException("Cucumber tool not available: " + project.projectGwt.getCucumberPath())
        }
    }

    /** Verify the versions of some specific gems. */
    private void verifyGemVersions() {
        def seleniumVersion = getGemVersion("selenium-webdriver")
        if (!seleniumVersion.startsWith("2.")) {
            project.logger.warn("Cucumber tests might not work due to version mismatch; expected Selenium 2.x, but got: " + seleniumVersion)
        }

        def rspecVersion = getGemVersion("rspec")
        if (rspecVersion != "2.14.0") {
            project.logger.warn("Cucumber tests might not work due to version mismatch; expected Rspec 2.14.0, but got: " + rspecVersion)
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
        return isCommandAvailable(project.projectGwt.getRubyPath(), [ "--version", ])
    }

    /** Check whether the Ruby 'gem' tool is available. */
    private boolean isGemAvailable() {
        return isCommandAvailable(project.projectGwt.getGemPath(), [ "--version", ])
    }

    /** Check whether the Cucumber tool is available. */
    private boolean isCucumberAvailable() {
        return isCommandAvailable(project.projectGwt.getRubyPath(), [ project.projectGwt.getCucumberPath(), "--version", ])
    }

    /** Get the installed version of a Ruby gem. */
    private String getGemVersion(gem) {
        try {
            def stdout = new ByteArrayOutputStream()
            def stderr = new ByteArrayOutputStream()

            def result = project.exec {
                standardOutput = stdout
                errorOutput = stderr
                executable = project.projectGwt.getGemPath()
                args = [ "list", gem, ]
            }

            def contents = stdout.toString()
            def regex = ~/(?s)(^${gem} [(])(.*)([)].*$)/        // for a string like "selenium-webdriver (2.37.0)"
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

}
