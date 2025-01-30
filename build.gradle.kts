plugins {
    id("java")
    application
}

application {
    mainClass.set("simplex.trading.Main")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaExec> {
    standardInput = System.`in`
}

tasks.withType<Jar> {
    archiveBaseName.set("trading-app")
    manifest {
        attributes["Main-Class"] = "simplex.trading.Main"
    }
}