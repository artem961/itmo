plugins {
    kotlin("jvm") version "2.1.21"
    application
}

group = "tech.arhr"
version = ""

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.varabyte.kotter:kotter-jvm:1.2.1")
    implementation("org.apache.commons:commons-math3:3.6.1")
    testImplementation("com.varabyte.kotterx:kotter-test-support-jvm:1.2.1")
}


tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("com.example.MainKt")
}

tasks.jar {
    manifest.attributes("Main-Class" to application.mainClass)
    val dependencies = configurations
        .runtimeClasspath
        .get()
        .map(::zipTree)
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

kotlin {
    jvmToolchain(21)
}
