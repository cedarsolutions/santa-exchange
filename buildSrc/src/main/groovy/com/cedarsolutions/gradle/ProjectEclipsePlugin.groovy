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

import org.gradle.api.tasks.Delete
import org.gradle.plugins.ide.eclipse.EclipsePlugin

/**
 * The projectEclipse plugin.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
class ProjectEclipsePlugin implements Plugin<Project> {

    /** Apply the plugin. */
    void apply(Project project) {
        project.plugins.apply(EclipsePlugin)
        project.extensions.create("projectEclipse", ProjectEclipsePluginExtension, project)
        applyProjectEclipse(project)
    }

    /** Apply projectEclipse. */
    void applyProjectEclipse(Project project) {

        // Customize the Eclipse .project file
        project.eclipse.project {
            natures "org.eclipse.jdt.core.javanature"
            natures "com.google.appengine.eclipse.core.gaeNature"
            natures "com.google.gwt.eclipse.core.gwtNature"
            natures "net.sf.eclipsecs.core.CheckstyleNature"
            natures "net.sourceforge.metrics.nature"

            buildCommand "org.eclipse.jdt.core.javabuilder"
            buildCommand "net.sf.eclipsecs.core.CheckstyleBuilder"
            // no "com.google.gdt.eclipse.core.webAppProjectValidator"
            // no "com.google.gwt.eclipse.core.gwtProjectValidator"
            // no "com.google.appengine.eclipse.core.projectValidator"

            // See: http://stackoverflow.com/questions/6442619/launching-gradle-builds-from-eclipse
            def builder = "<project>/.externalToolBuilders/com.google.appengine.eclipse.core.enhancerbuilder.launch"
            buildCommand "org.eclipse.ui.externaltools.ExternalToolBuilder", LaunchConfigHandle: builder
            file {
                withXml {
                    def projectNode = it.asNode()
                    projectNode.iterator().each { subNode ->
                        String subNodeText = "" + subNode
                        if (subNodeText.startsWith("buildSpec")) {
                            subNode.iterator().each { buildCmd ->
                                String nameNode = buildCmd?.name
                                if (nameNode.contains("ExternalToolBuilder")) {
                                    buildCmd.appendNode("triggers", "full,incremental,")
                                }
                            }
                        }
                    }
                }
            }
        }

        // Hide the various derived directories from Eclipse
        project.eclipse.project.file.withXml { provider ->
            project.convention.plugins.cedarBuild.ignoreResourcesFromDirectories(provider, 
                [ ".gradle",
                  "build",
                  "tools",
                  "buildSrc/build",
                  "gwt-unitCache",
                  "www-test",
                  "war/" + project.cedarGwtOnGae.getAppEntryPoint() + ".JUnit",
                  "war/" + project.cedarGwtOnGae.getAppModuleName(), 
                  "war/WEB-INF/deploy",
                  "war/WEB-INF/lib",
                  "war/WEB-INF/appengine-generated",
                  "WEB-INF",
                  "extras", ])
        }

        // Copy settings into the workspace
        project.eclipseJdt << {
            project.mkdir ".settings"
            project.copy {
              from project.projectEclipse.getSettingsSourceDir()
              into ".settings"
              include "*.prefs"
            }

            project.mkdir ".externalToolBuilders"
            project.copy {
              from project.projectEclipse.getExternalToolBuildersSourceDir()
              into ".externalToolBuilders"
              include "*.launch"
            }
        }

        // Clean up extended settings
        project.task("cleanEclipseExtendedSettings", type: Delete) << {

            // Delete the settings we copied in
            project.file(project.projectEclipse.getSettingsSourceDir()).listFiles(
                [accept:{file-> file ==~ /^.*\.prefs$/ }] as FileFilter).each { name ->
                    project.file(".settings/" + project.file(name).name).delete()
            }

            // Delete the external tool builders we copied in
            project.file(project.projectEclipse.getExternalToolBuildersSourceDir()).listFiles(
                [accept:{file-> file ==~ /^.*\.launch/ }] as FileFilter).each { name ->
                    project.file(".externalToolBuilders/" + project.file(name).name).delete()
            }

            // Remove .settings and .externalToolBuilders only if they're empty
            ant.delete(includeemptydirs : "true", quiet : "true") {
                fileset(dir: ".settings", excludes : "**/*")
                fileset(dir: ".externalToolBuilders", excludes : "**/*")
            }
        }

        // Cleanup the runtime libraries
        project.task("cleanupRuntimeLibraries") << { 
            project.ant.delete(includeemptydirs: "true", quiet: "true") {
                fileset(dir: "war/WEB-INF/lib", includes: "**/*")
            }
        }

        // Copy the runtime libraries to war/WEB-INF/lib to make the Eclipse web application launcher happy
        project.task("copyRuntimeLibraries", dependsOn: project.tasks.cleanupRuntimeLibraries) << { 
            def files = project.configurations.runtime.files - project.configurations.providedRuntime.files
            project.copy {
                into "war/WEB-INF/lib"
                from files
            }
        }

        // Just an alias for copyRuntimeLibaries
        project.task("refreshLibraries", dependsOn: project.tasks.copyRuntimeLibraries) << {
        }

        // Always re-generate everything, otherwise we get duplicate sections in files
        project.tasks.eclipse.dependsOn(project.tasks.cleanEclipse)

        // Clean up runtime libraries whenever the rest of Eclipse gets cleaned up
        project.tasks.cleanEclipse.dependsOn(project.tasks.cleanupRuntimeLibraries)

        // We need to copy the jars into the WEB-INF/lib directory once we know which ones are required
        project.tasks.eclipseClasspath.dependsOn(project.tasks.copyRuntimeLibraries)

        // Clean up custom settings whenever the rest of Eclipse gets cleaned up
        project.tasks.cleanEclipseJdt.dependsOn(project.tasks.cleanEclipseExtendedSettings)

        // We need to download the SDK before the classpath can be generated properly.
        // We also want to copy the jars into the WEB-INF/lib directory at the same time.
        project.tasks.eclipseClasspath.dependsOn(project.tasks.gaeDownloadSdk, project.tasks.copyRuntimeLibraries)

        // Customize the Eclipse .classpath file
        project.eclipse.classpath.file {
            whenMerged { classpath ->
                // Change the default output directory to war/WEB-INF/classes instead of bin
                classpath.entries.findAll { entry -> entry.kind == "output" && entry.path == "bin" }.each { entry ->
                    entry.path = "war/WEB-INF/classes"
                }

                // Test output should go to test-bin, so it can be separated for coverage purposes
                classpath.entries.findAll { entry -> entry.kind == "src" && entry.path == "test" }.each { entry ->
                    entry.output = "test-bin"
                }

                // Replace the suites source folder with just "suites", and send classes to test-bin
                classpath.entries.findAll { entry -> entry.kind == "src" && entry.path == project.projectEclipse.getSuitesSourceDir() }.each { entry ->
                    entry.path = "suites"
                    entry.output = "test-bin"
                }

                // Add the devmode runtime dependencies to the Eclipse classpath
                // Note: the ".nondependency" attributes means that this shouldn't go into the war
                withXml { xml ->
                    def node = xml.asNode()
                    project.configurations.devmodeRuntime.each { devmode ->
                        node.appendNode("classpathentry", [ kind : "lib", path : devmode.canonicalPath, exported : "true", ])
                            .appendNode("attributes").appendNode("attribute",  [ name : "org.eclipse.jst.component.nondependency", value : "", ])
                    }
                }

                // Exclude the provided dependencies (like GWT) from the war file so Eclipse is consistent with gradle build
                // Note: the ".nondependency" attributes means that this shouldn't go into the war
                withXml { xml ->
                    def node = xml.asNode()
                    project.configurations.providedRuntime.each { provided ->
                        def path = provided.canonicalPath.replace("\\", "/")
                        node."classpathentry".findAll { it.@kind == "lib" && it.@path == path }.each { entry ->
                            entry.appendNode("attributes").appendNode("attribute",  [ name : "org.eclipse.jst.component.nondependency", value : "", ])
                        }
                    }
                }

                // Reorder the source packages the way I want them to appear
                def order = project.projectEclipse.getSourceFolders()
                def all = classpath.entries.findAll { entry -> entry.kind == "src" }
                classpath.entries.removeAll(all)
                order.eachWithIndex() { path, index ->
                    all.findAll { entry -> entry.kind == "src" && entry.path == path }.each { entry ->
                        classpath.entries.add(index, entry)
                    }
                }

                // Put all of the "con" classpath entries (like the JRE container) at the bottom
                all = classpath.entries.findAll { entry -> entry.kind == "con" }
                classpath.entries.removeAll(all)
                withXml { xml ->
                    def node = xml.asNode()
                    all.findAll { entry ->
                        node.appendNode("classpathentry", [ kind : "con", path : entry.path, exported : entry.exported, ])
                    }
                }
            }
        }
    }
}

