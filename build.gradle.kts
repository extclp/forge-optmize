plugins {
    java
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.ow2.asm:asm:9.5")
    implementation("org.ow2.asm:asm-tree:9.5")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
