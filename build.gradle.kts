plugins {
    `java-library`
    `maven-publish`
    signing
}

group = "com.infoyupay.validator"
version = "0.1.0"

// === JAVA 25 local ===
java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
    modularity.inferModulePath.set(true)
}

// === DEPENDENCIAS ===
repositories {
    mavenCentral()
}

dependencies {
    // JUnit Jupiter 6.x
    // https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter
    testImplementation("org.junit.jupiter:junit-jupiter:6.0.1")


    // AssertJ moderno
    testImplementation("org.assertj:assertj-core:3.27.6")
}

tasks.test {
    useJUnitPlatform()
}

// === PUBLICACIÓN A MAVEN CENTRAL (OSSRH) ===
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            pom {
                name.set("validator-doi")
                description.set("Lightweight Peruvian DOI validator library (RUC/DNI/CE).")
                url.set("https://github.com/infoyupay/validator-doi")

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
                    url.set("https://github.com/infoyupay/validator-doi")
                    connection.set("scm:git:https://github.com/infoyupay/validator-doi.git")
                    developerConnection.set("scm:git:ssh://git@github.com:infoyupay/validator-doi.git")
                }
            }
        }
    }

    repositories {
        mavenCentral()
        maven {
            name = "OSSRH"

            url = uri(
                if (version.toString().endsWith("SNAPSHOT"))
                    "https://s01.oss.sonatype.org/content/repositories/snapshots/"
                else
                    "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
            )

            credentials {
                username = System.getenv("OSSRH_USERNAME")
                password = System.getenv("OSSRH_PASSWORD")
            }
        }
    }
}

// === SIGNING ===
signing {
    sign(publishing.publications["mavenJava"])
}
