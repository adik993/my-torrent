plugins {
    distribution
}

repositories {
    mavenCentral()
    maven("http://repo.jenkins-ci.org/releases")
}

val windows = configurations.create("windows")
val common = configurations.create("common")

distributions {
    create("linux") {
        contents {
            eachFile {
                if (name.endsWith(".sh")) mode = 500
            }

            from(common) {
                rename { "my-torrent.jar" }
            }
        }
    }
    create("windows") {
        contents {
            from(windows) {
                rename { "my-torrent.exe" }
            }

            from(common) {
                rename { "my-torrent.jar" }
            }
        }
    }
}

tasks.all {
    if (name.endsWith("Tar")) enabled = false
}

dependencies {
    windows("com.sun.winsw:winsw:2.0.3:bin@exe")
    common(project(":server")) {
        exclude(group = "*", module = "*")
    }
}
