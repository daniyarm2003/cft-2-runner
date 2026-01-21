plugins {
    id("java")
    id("application")
}

group = "com.cft"
version = "1.1.2"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.20.1")
    implementation("commons-cli:commons-cli:1.11.0")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "com.cft.Main",
            "Manifest-Version" to "1.0"
        )
    }

    from({
        configurations.runtimeClasspath.get()
            .filter({ it.name.endsWith(".jar") })
            .map { zipTree(it) }
    })

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.test {
    useJUnitPlatform()
}