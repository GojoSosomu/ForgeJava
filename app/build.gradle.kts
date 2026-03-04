import org.gradle.internal.os.OperatingSystem

plugins {
    id("java")
    id("application")
}

version = "1.0.2-Beta"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("org.codehaus.janino:janino:3.1.10")
    implementation("commons-io:commons-io:2.16.1")
    
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

application {
    mainClass.set("App")
}

// CUSTOM JPACKAGE TASK
// Simplified for Swing (no module-path or add-modules required)
tasks.register<Exec>("jpackage") {
    group = "distribution"
    dependsOn("jar")

    val javaHome = System.getProperty("java.home")
    val jpackageBin = if (OperatingSystem.current().isWindows) "$javaHome\\bin\\jpackage.exe" else "$javaHome/bin/jpackage"
    
    val outputDir = layout.buildDirectory.dir("dist").get().asFile
    val tempLibsDir = layout.buildDirectory.dir("jpackage-libs").get().asFile

    doFirst {
        delete(outputDir)
        delete(tempLibsDir)
        tempLibsDir.mkdirs()
        
        // Consolidate all JARs for the jpackage input
        copy {
            from(tasks.jar.get().archiveFile)
            from(configurations.runtimeClasspath)
            into(tempLibsDir)
        }
    }

    commandLine(
        jpackageBin,
        "--type", "app-image",
        "--dest", outputDir.absolutePath,
        "--name", "JLearning-${project.version}",
        "--input", tempLibsDir.absolutePath,
        "--main-jar", tasks.jar.get().archiveFileName.get(),
        "--main-class", "App",
        "--app-content", "${file("src/main/resources/data").absolutePath},${file("user_datas").absolutePath},${file("src/main/resources/images").absolutePath}", 
        "--icon", file("src/main/resources/images/icon/Icon.ico").absolutePath
    )
}

tasks.test {
    useJUnitPlatform()
}