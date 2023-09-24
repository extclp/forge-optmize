plugins {
    java
}

repositories {
    maven {
        name  = "NeoForged"
        url = uri("https://maven.neoforged.net/releases")
    }
}

dependencies {
    implementation("net.minecraftforge:forgespi:7.0.1") {
        isTransitive = false
    }
    implementation("cpw.mods:modlauncher:10.0.10") {
        isTransitive = false
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
