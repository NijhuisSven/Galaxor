plugins {
    `java-library`
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.14"
    id("com.gradleup.shadow") version "8.3.0"
    id("io.freefair.lombok") version "8.6"
}

group = "nl.nijhuissven.orbit"
version = "1.0"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io")
    maven("https://repo.aikar.co/content/groups/aikar/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://hub.spigotmc.org/nexus/content/groups/public/")
    maven("https://repo.spongepowered.org/maven")
}

dependencies {
    paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")
    compileOnly("org.spongepowered:configurate-yaml:4.2.0-SNAPSHOT")
    compileOnly("org.spongepowered:configurate-core:4.2.0-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")
    implementation("co.aikar:acf-paper:0.5.1-SNAPSHOT")
    implementation("org.reflections:reflections:0.10.2")
    implementation("com.github.Mindgamesnl:storm:e1f961b480")
    compileOnly("org.xerial:sqlite-jdbc:3.42.0.0")
    compileOnly("com.zaxxer:HikariCP:5.0.1")
    compileOnly("org.mariadb.jdbc:mariadb-java-client:3.3.3")
    compileOnly("net.luckperms:api:5.4")
    implementation("com.influxdb:influxdb-client-java:6.11.0")
}

tasks {
    compileJava {
        options.release.set(21)
        options.compilerArgs.add("-parameters")
        options.isFork = true
    }
    shadowJar {
        minimize()
        archiveFileName.set("${rootProject.name}-${rootProject.version}.jar")
        relocate("co.aikar.commands", "nl.nijhuissven.shaded.acf")
        relocate("co.aikar.locales", "nl.nijhuissven.shaded.locales")
        // Voeg hier extra relocates toe indien nodig
        exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
    }
}
