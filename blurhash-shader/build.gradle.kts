import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.mavenPublish)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
//    id("com.android.library")
//    id("org.jetbrains.compose")
//    id("org.jetbrains.dokka")
//    id("com.vanniktech.maven.publish")
}

kotlin {
    androidTarget {
        publishLibraryVariants("release")
    }

    jvm("desktop")


    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "blurhash-shader"
        browser()
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "lib"
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)

            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
    }
}

android {
    compileSdk = 35
    namespace = "com.brys.compose.blurhash.shader"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        minSdk = 21
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    coordinates(rootProject.group.toString(), "blurhash-shader", rootProject.version.toString())

    pom {
        name = "Compose Blurhash"
        description = "A high performance implementation of blurhash for Compose Multiplatform. Utilizes SKSL shaders for performant rendering."
        url = "https://github.com/brys0/compose-blurhash"

        licenses {
            license {
                name = "MIT License"
                url = "https://opensource.org/license/mit"
            }
        }

        developers {
            developer {
                id = "brys0"
                name = "Bryson T."
            }
        }

        scm {
            connection = "scm:git:git://github.com/brys0/compose-blurhash.git"
            developerConnection = "scm:git:ssh://github.com/brys0/compose-blurhash.git"
            url = "https://github.com/brys0/compose-blurhash"
        }
    }

    signAllPublications()
}