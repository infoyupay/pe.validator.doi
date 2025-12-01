plugins {
    `java-library`
    `maven-publish`
    signing
}

group = "com.infoyupay.validator"
version = "1.0.0"

// === Java 11 Compatibility ===
// Even if you're using JDK 25 locally, this library targets Java 11.
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
    modularity.inferModulePath.set(true)

    // Required by Maven Central: JARs must include sources and javadoc.
    withSourcesJar()
    withJavadocJar()
}

// === Dependencies ===
repositories {
    mavenCentral()
}

dependencies {
    // JUnit Jupiter 5.x – last major version supporting Java 11
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.5")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.5")

    // JUnit Platform launcher (required when running tests from IDEs)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.10.2")

    // AssertJ modern fluent assertion library
    testImplementation("org.assertj:assertj-core:3.27.6")
}

tasks.test {
    // Ensure JUnit Platform runs (Jupiter 5)
    useJUnitPlatform()
}

// === Maven Central Publication (New Publisher API) ===
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "validator-doi"
            // Publish the default Java component (JAR + sources + javadoc)
            from(components["java"])

            // Maven POM metadata
            pom {
                name.set("validator-doi")
                description.set("Lightweight Peruvian DOI validator library (RUC/DNI/CE).")
                url.set("https://github.com/infoyupay/pe.validator.doi")

                licenses {
                    license {
                        name.set("GPL-3.0-or-later")
                        url.set("https://www.gnu.org/licenses/gpl-3.0.html")
                    }
                }

                developers {
                    developer {
                        id.set("dvidal")
                        name.set("David Vidal")
                        email.set("info@infoyupay.com")
                    }
                }

                scm {
                    url.set("https://github.com/infoyupay/pe.validator.doi")
                    connection.set("scm:git:https://github.com/infoyupay/pe.validator.doi.git")
                    developerConnection.set("scm:git:ssh://git@github.com:infoyupay/pe.validator.doi.git")
                }

            }
        }
    }

    repositories {
        maven {
            name = "Central"

            // Modern Maven Central Publisher API endpoint
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")

            // Credentials are provided via ~/.gradle/gradle.properties
            credentials {
                username = findProperty("ossrhUsername") as String?
                password = findProperty("ossrhPassword") as String?
            }
        }
    }
}

// === GPG Signing ===
// Signs all generated artifacts (JARs, POM, sources, javadoc)
// Required for Maven Central.
signing {
    useInMemoryPgpKeys(
        findProperty("signing.keyId") as String?,
        file("/home/dvidal/.gradle/private-key.asc").readText(),
        findProperty("signing.password") as String?
    )

    sign(publishing.publications["mavenJava"])
}
