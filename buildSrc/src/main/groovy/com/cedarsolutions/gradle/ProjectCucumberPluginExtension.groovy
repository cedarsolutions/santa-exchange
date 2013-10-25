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
import java.io.File
import java.util.concurrent.Callable
import org.apache.tools.ant.taskdefs.condition.Os

/**
 * Plugin extension for projectCucumber.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
class ProjectCucumberPluginExtension {

    /** Project tied to this extension. */
    private Project project;

    /** Create an extension for a project. */
    public ProjectCucumberPluginExtension(Project project) {
        this.project = project;
    }

    /** Path to the Ruby install directory. */
    def rubyInstallDir

    /** Directory that the Cucumber tests live in. */
    def cucumberDir

    /** Subdirectory (within cucumberDir) where features live. */
    def featuresSubdir

    /** Subdirectory (within cucumberDir) where Ruby code lives. */
    def rubySubdir

    /** Get rubyInstallDir, accounting for closures. */
    String getRubyInstallDir() {
        return rubyInstallDir != null && rubyInstallDir instanceof Callable ? rubyInstallDir.call() : rubyInstallDir
    }

    /** Get cucumberDir, accounting for closures. */
    String getCucumberDir() {
        return cucumberDir != null && cucumberDir instanceof Callable ? cucumberDir.call() : cucumberDir
    }

    /** Get featuresSubdir, accounting for closures. */
    String getFeaturesSubdir() {
        return featuresSubdir != null && featuresSubdir instanceof Callable ? featuresSubdir.call() : featuresSubdir
    }

    /** Get rubySubdir, accounting for closures. */
    String getRubySubdir() {
        return rubySubdir != null && rubySubdir instanceof Callable ? rubySubdir.call() : rubySubdir
    }

    /** Get the path to the ruby executable. */
    String getRubyPath() {
        def path = project.file(getRubyInstallDir() + "/bin/ruby").canonicalPath
        return isWindows() ? path + ".exe" : path
    }

    /** Get the path to the Ruby gem executable. */
    String getGemPath() {
        def path = project.file(getRubyInstallDir() + "/bin/gem").canonicalPath
        return isWindows() ? path + ".bat" : path
    }

    /** Get the path to the Ruby cucumber executable. */
    String getCucumberPath() {
        return project.file(getRubyInstallDir() + "/bin/cucumber").canonicalPath
    }

    private boolean isWindows() {
        return Os.isFamily(Os.FAMILY_WINDOWS);
    }

}
