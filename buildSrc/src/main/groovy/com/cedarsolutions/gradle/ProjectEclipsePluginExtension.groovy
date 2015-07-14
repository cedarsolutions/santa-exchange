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
// * Language : Gradle (>= 2.5)
// * Project  : Secret Santa Exchange
// *
// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.cedarsolutions.gradle

import org.gradle.api.Project
import org.gradle.api.InvalidUserDataException
import java.io.File
import java.util.concurrent.Callable

/**
 * Plugin extension for projectEclipse.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
class ProjectEclipsePluginExtension {

    /** Project tied to this extension. */
    private Project project;

    /** Create an extension for a project. */
    public ProjectEclipsePluginExtension(Project project) {
        this.project = project;
    }

    /** List of the source folders, in the desired order within Eclipse. */
    def sourceFolders;

    /** Source for the suites source files. */
    def suitesSourceDir;

    /** Source for the settings files. */
    def settingsSourceDir;

    /** Source for the external tool builders files. */
    def externalToolBuildersSourceDir;

    /** Get sourceFolders, accounting for closures. */
    def getSourceFolders() {
        return sourceFolders != null && sourceFolders instanceof Callable ? sourceFolders.call() : sourceFolders
    }

    /** Get suitesSourceDir, accounting for closures. */
    String getSuitesSourceDir() {
        return suitesSourceDir != null && suitesSourceDir instanceof Callable ? suitesSourceDir.call() : suitesSourceDir
    }

    /** Get settingsSourceDir, accounting for closures. */
    String getSettingsSourceDir() {
        return settingsSourceDir != null && settingsSourceDir instanceof Callable ? settingsSourceDir.call() : settingsSourceDir
    }

    /** Get externalToolBuildersSourceDir, accounting for closures. */
    String getExternalToolBuildersSourceDir() {
        return externalToolBuildersSourceDir != null && externalToolBuildersSourceDir instanceof Callable ? externalToolBuildersSourceDir.call() : externalToolBuildersSourceDir
    }

}

