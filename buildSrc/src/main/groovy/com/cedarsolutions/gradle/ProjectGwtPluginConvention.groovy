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

        project.file(cacheDir).deleteDir()  // clean up the database every time the server is rebooted

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

}
