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
import org.gradle.api.Plugin

/**
 * The projectCucumber plugin.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
class ProjectCucumberPlugin implements Plugin<Project> {

    /** Apply the plugin. */
    void apply(Project project) {
        project.extensions.create("projectCucumber", ProjectCucumberPluginExtension, project)
        project.convention.plugins.projectCucumber = new ProjectCucumberPluginConvention(project)
        applyProjectCucumber(project)
    }

    /** Apply projectCucumber. */
    void applyProjectCucumber(Project project) {

        // Install all of the Cucumber-related tooling
        project.task("installCucumber") << {
            project.convention.plugins.projectCucumber.installCucumber()
        }

        // Uninstall all of the Cucumber-related tooling
        project.task("uninstallCucumber") << {
            project.convention.plugins.projectCucumber.uninstallCucumber()
        }

        // Reinstall all of the Cucumber-related tooling
        project.task("reinstallCucumber", dependsOn: [project.tasks.uninstallCucumber, project.tasks.installCucumber]) << {
        }
    
        // If they invoke both, uninstall and then reinstall
        project.tasks.installCucumber.mustRunAfter project.tasks.uninstallCucumber

        // Verify the configured cucumber install is ok.
        project.task("verifyCucumber") << {
            project.convention.plugins.projectCucumber.verifyCucumberInstall()
        }

        // Run the Cucumber tests, assuming the devmode server is already up
        // Note that you have to manually build the application and boot devmode for this to work
        project.task("runCucumber") << {
            project.convention.plugins.projectCucumber.execCucumber(null, null).assertNormalExitValue()
        }

        // Run the Cucumber tests, restricting by name containing a substring, assuming the devmode server is already up
        // Note that you have to manually build the application and boot devmode for this to work
        project.task("runCucumberByName") << {
            def name = null
            project.convention.plugins.cedarBuild.getInput("Configure Cucumber", "Test Name", false, { input -> name = input})
            project.convention.plugins.projectCucumber.execCucumber(name, null).assertNormalExitValue()
        }

        // Run the Cucumber tests for a specific feature file, assuming the devmode server is already up
        // Note that you have to manually build the application and boot devmode for this to work
        project.task("runCucumberByFeature") << {
            def feature = null
            project.convention.plugins.cedarBuild.getInput("Configure Cucumber", "Feature Path", false, { input -> feature = input})
            project.convention.plugins.projectCucumber.execCucumber(null, feature).assertNormalExitValue()
        }

        // Run the Cucumber tests, including a reboot of the server
        // Note that you have to manually build the application for this to work
        project.task("runCucumberWithReboot") << {
            project.convention.plugins.projectGwt.rebootDevmode()
            project.convention.plugins.projectGwt.waitForDevmode()
            project.convention.plugins.projectCucumber.execCucumber(null, null).assertNormalExitValue()
        }

    }

}

