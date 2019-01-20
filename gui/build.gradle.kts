import com.moowork.gradle.node.NodeExtension
import com.moowork.gradle.node.npm.NpmInstallTask
import com.moowork.gradle.node.npm.NpmTask

plugins {
    id("com.moowork.node") version "1.2.0"
}

configure<NodeExtension> {
    version = "10.15.0"
    npmVersion = "6.4.1"
    download = true
}

val npmInstall: NpmInstallTask by tasks

val npm_build_dist by tasks.creating(NpmTask::class) {
    inputs.files(fileTree("src"), fileTree("config"))
    outputs.dir("$buildDir/dist")
    dependsOn(npmInstall)
    setArgs(listOf("run", "build:dist"))
}

val npm_start by tasks.creating(NpmTask::class) {
    dependsOn(npmInstall)
    setArgs(listOf("start"))
    inputs.files(fileTree("src"), fileTree("config"))
    outputs.dir("bin/main")
}

tasks["assemble"].dependsOn(npm_build_dist)

sourceSets {
    getByName("main") {
        resources {
            srcDir("src")
        }
    }
}