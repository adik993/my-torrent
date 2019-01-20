import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    groovy
}

apply {
    plugin("org.springframework.boot")
}



tasks {
    "processResources"(ProcessResources::class) {
        dependsOn(project(":gui").tasks["assemble"])
        from(project(":gui").file("build/dist")) {
            into("static")
        }
        finalizedBy(copyResourcesForIdea)
    }
    "bootJar"(BootJar::class) {
        launchScript()
    }
}

val copyResourcesForIdea by tasks.creating(Copy::class) {
    from("$buildDir/resources/main")
    into("$buildDir/classes/java/main")
}

val bootJar: BootJar by tasks
val jar: Jar by tasks
jar.finalizedBy(bootJar)


dependencies {
    compileOnly("org.springframework.boot:spring-boot-configuration-processor")

    compile("org.springframework.boot:spring-boot-starter-webflux")
    compile("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
    compile("de.flapdoodle.embed:de.flapdoodle.embed.mongo")
    compile("io.projectreactor.addons:reactor-extra")
    compile("io.projectreactor.addons:reactor-adapter")
    compile("com.adik993:tpb-client")
    compile("org.apache.commons:commons-lang3")

    testCompile("org.springframework.boot:spring-boot-starter-test")
    testCompile("io.projectreactor:reactor-test")
    testCompile("junit:junit")
    testCompile("org.spockframework:spock-core")
    testCompile("org.spockframework:spock-spring")
    testCompile("com.pchudzik.springmock:springmock-spock")
    testCompile("org.jsoup:jsoup:1.11.3")
}