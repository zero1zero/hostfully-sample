plugins {
    application
}

group = "me.zackmanning"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.glassfish.jersey.containers:jersey-container-grizzly2-http:3.1.2")
    implementation("org.glassfish.jersey.inject:jersey-hk2:3.1.2")
    implementation("org.glassfish.jersey.media:jersey-media-json-jackson:3.1.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.2")

    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

application {
    mainClass.set("me.zackmanning.hostfully.Main")
}