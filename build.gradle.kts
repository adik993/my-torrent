import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension

val springBootVersion by extra { "2.1.2.RELEASE" }
val tpbClientVersion by extra { "1.0.0" }
val lombokVersion by extra { "1.18.4" }
val rxjavaVersion by extra { "2.2.5" }
val spockVersion by extra { "1.2-groovy-2.5" }
val springMockSpock by extra { "1.2.0" }
val apacheCommonsLang by extra { "3.8.1" }

buildscript {
    val springBootVersion by extra { "2.1.2.RELEASE" }
    repositories {
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:$springBootVersion")
        classpath("gradle.plugin.com.dorongold.plugins:task-tree:1.3.1")
    }
}

plugins {
    idea
    java
    maven
    `project-report`
}

apply {
    plugin("com.dorongold.task-tree")
    plugin("io.spring.dependency-management")
}

allprojects {
    group = "com.adik993"
    version = "1.1-SNAPSHOT"
}

subprojects {
    apply {
        plugin("java")
        plugin("idea")
        plugin("io.spring.dependency-management")
    }
    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
    }

    val clean by tasks
    clean.doLast {
        delete("$projectDir/out")
    }

    idea {
        module {
            inheritOutputDirs = false
            outputDir = file("$buildDir/classes/java/main/")
            testOutputDir = file("$buildDir/classes/java/test/")
        }
    }

    configure<DependencyManagementExtension> {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:$springBootVersion")
        }

        dependencies {
            dependency("com.adik993:tpb-client:$tpbClientVersion")
            dependency("io.reactivex.rxjava2:rxjava:$rxjavaVersion")
            dependency("org.spockframework:spock-core:$spockVersion")
            dependency("org.spockframework:spock-spring:$spockVersion")
            dependency("com.pchudzik.springmock:springmock-spock:$springMockSpock")
            dependency("org.apache.commons:commons-lang3:$apacheCommonsLang")
        }
    }

    dependencies {
        annotationProcessor("org.projectlombok:lombok")
        compileOnly("org.projectlombok:lombok")
    }
}