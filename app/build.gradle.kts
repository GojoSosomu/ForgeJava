import org.gradle.internal.os.OperatingSystem
import java.io.File

plugins {
    id("application")
    id("eclipse")
}

repositories {
    mavenCentral()
}

val javaFxVersion = "21.0.5"
val platform = OperatingSystem.current().run {
    when {
        isWindows -> "win"
        isMacOsX -> "mac"
        else -> "linux"
    }
}

dependencies {
    // ✅ JavaFX dependencies
    implementation("org.openjfx:javafx-base:$javaFxVersion:$platform")
    implementation("org.openjfx:javafx-controls:$javaFxVersion:$platform")
    implementation("org.openjfx:javafx-fxml:$javaFxVersion:$platform")
    implementation("org.openjfx:javafx-graphics:$javaFxVersion:$platform")
    
    // ✅ JavaFX UI Libraries
    implementation("org.controlsfx:controlsfx:11.2.1")
    implementation("org.fxmisc.richtext:richtextfx:0.11.1")
    implementation("org.reactfx:reactfx:2.0-M5")
    implementation("org.kordamp.ikonli:ikonli-javafx:12.3.1")
    implementation("org.kordamp.ikonli:ikonli-fontawesome5-pack:12.3.1")
    implementation("com.dlsc.formsfx:formsfx-core:11.6.0")
    implementation("org.fxmisc.flowless:flowless:0.6.10")
    
    // ✅ SAVE/LOAD
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")

    // ✅ RichTextFX for editor
    implementation("org.fxmisc.richtext:richtextfx:0.11.0")

    // ✅ Code Execution
    implementation("org.codehaus.janino:janino:3.1.10")

    // ✅ Logging
    implementation("ch.qos.logback:logback-classic:1.4.11")

    // ✅ IO Utilities
    implementation("commons-io:commons-io:2.16.1")

    // ✅ JUnit dependencies
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.10.2")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

eclipse {
    jdt {
        sourceCompatibility = JavaVersion.toVersion(25)
        targetCompatibility = JavaVersion.toVersion(25)
    }
}

application {
    // ✅ Your main JavaFX entry point
    mainClass.set("App")

    // ✅ Tell the JVM to load JavaFX modules at runtime
    applicationDefaultJvmArgs = listOf(
        "--add-modules", "javafx.controls,javafx.fxml"
    )
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaExec> {
    val javaFxVersion = "21.0.5"
    val platform = org.gradle.internal.os.OperatingSystem.current().run {
        when {
            isWindows -> "win"
            isMacOsX -> "mac"
            else -> "linux"
        }
    }
    jvmArgs = listOf(
        "--module-path", configurations.runtimeClasspath.get()
            .filter { it.name.contains("javafx") }
            .joinToString(File.pathSeparator),
        "--add-modules", "javafx.controls,javafx.fxml"
    )
}
