plugins {
    kotlin("jvm") version "1.5.10"
    java
    id("com.github.johnrengelman.shadow") version "7.0.0"
    idea
}

group = "liray.itemsaver"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.codemc.org/repository/maven-public/")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("de.tr7zw:item-nbt-api:2.8.0")
    compileOnly("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}

tasks {
    shadowJar {
        archiveBaseName.set("SimpleSaveItem")
        archiveVersion.set("")
        archiveClassifier.set("")
        destinationDirectory.set(file("C:\\Users\\david\\Desktop\\servers\\serverMegan\\plugins"))
        relocate("de.tr7zw", "liray.itemsaver.tr7zw")
    }
}