plugins {
    kotlin("jvm") version "2.0.10"
    kotlin("plugin.serialization") version "2.0.10"
    application // for fat jar
}

group = "dev.surly"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("dev.surly:kotlin-minimal-server:0.1.0")
    implementation("dev.surly:kotlin-minimal-rdms:0.1.0")
    implementation("org.postgresql:postgresql:42.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("MainKt")
}

// Task to create a fat JAR
tasks.register<Jar>("fatJar") {

    // We need this for Gradle optimization to work
    dependsOn.addAll(listOf("compileJava", "compileKotlin", "processResources"))

    archiveClassifier.set("standalone")

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    // Provided we set it up in the application plugin configuration
    manifest { attributes(mapOf("Main-Class" to application.mainClass)) }

    // Specify that this JAR should include the classes and resources from this project
    val sourcesMain = sourceSets.main.get()
    val contents = configurations.runtimeClasspath.get()
        .map { if (it.isDirectory) it else zipTree(it) } + sourcesMain.output
    from(contents)
}

// Make the build task depend on the fatJar task to ensure it gets executed as part of the build
tasks.build {
    dependsOn("fatJar")
}
