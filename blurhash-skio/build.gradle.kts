import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

//import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.mavenPublish)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
//    androidTarget {
//        publishLibraryVariants("release")
//    }

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
                implementation(project(":blurhash-shader"))
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

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    coordinates(rootProject.group.toString(), "blurhash-skio", rootProject.version.toString())

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