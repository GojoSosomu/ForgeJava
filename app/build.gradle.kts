import org.gradle.internal.os.OperatingSystem

plugins {
    id("java")
    id("application")
    // Official JavaFX plugin - handles the 'javafx' block and dependencies
    id("org.openjfx.javafxplugin") version "0.1.0"
}

version = "1.0.1-Beta"

repositories {
    mavenCentral()
}

// JavaFX block (enabled by the openjfx plugin)
javafx {
    version = "21.0.5"
    modules("javafx.controls", "javafx.fxml", "javafx.graphics", "javafx.base", "javafx.media")
}

dependencies {
    // Standard Dependencies (JavaFX jars are handled automatically by the plugin)
    implementation("org.controlsfx:controlsfx:11.2.1")
    implementation("org.fxmisc.richtext:richtextfx:0.11.1")
    implementation("org.reactfx:reactfx:2.0-M5")
    implementation("org.kordamp.ikonli:ikonli-javafx:12.3.1")
    implementation("org.kordamp.ikonli:ikonli-fontawesome5-pack:12.3.1")
    implementation("com.dlsc.formsfx:formsfx-core:11.6.0")
    implementation("org.fxmisc.flowless:flowless:0.6.10")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("org.codehaus.janino:janino:3.1.10")
    implementation("ch.qos.logback:logback-classic:1.4.11")
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

// PRESERVED: Your fixed 'run' task for Gradle 9 compatibility
tasks.named<JavaExec>("run") {
    val runtimeClasspath = configurations.runtimeClasspath.get()
    doFirst {
        jvmArgs = listOf(
            "--module-path", runtimeClasspath.asPath,
            "--add-modules", "javafx.controls,javafx.fxml"
        )
    }
}

// CUSTOM JPACKAGE TASK (Bypasses Beryx plugin errors)
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
        
        copy {
            from(layout.buildDirectory.dir("libs"))
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
        "--main-jar", "${project.name}-${project.version}.jar",
        "--main-class", "App",
        "--app-content", "${file("src/main/resources/data").absolutePath},${file("user_datas").absolutePath},${file("src/main/resources/images").absolutePath}", 
        "--icon", file("src/main/resources/images/icon/Icon.ico").absolutePath
    )

}


tasks.test {
    useJUnitPlatform()
}
