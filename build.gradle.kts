plugins {
    id("java")
}

group = "fish.crafting"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(files("libs/HytaleServer.jar"))
}

tasks.test {
    useJUnitPlatform()
}