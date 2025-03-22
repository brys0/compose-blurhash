import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

//import com.vanniktech.maven.publish.SonatypeHost

plugins {
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
        moduleName = "blurhash"
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
        val androidMain by getting {
            dependencies {
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

            dependencies {
                implementation(project(":blurhash-skio"))
            }
        }

        wasmJsMain.dependencies {
                implementation(project(":blurhash-skio"))
        }

        val desktopMain by getting {
            dependencies {
                implementation(project(":blurhash-skio"))
            }
        }
    }
}

android {
    compileSdk = 33
    namespace = "com.brys.compose.blurhash"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        minSdk = 33
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
}

//mavenPublishing {
////    publishToMavenCentral(SonatypeHost.DEFAULT)
//    // or when publishing to https://s01.oss.sonatype.org
//    publishToMavenCentral(SonatypeHost.S01, automaticRelease = true)
//    signAllPublications()
//    coordinates("com.example.mylibrary", "mylibrary-runtime", "1.0.0")
//
//    pom {
//        name.set(project.name)
//        description.set("A description of what my library does.")
//        inceptionYear.set("2023")
//        url.set("https://github.com/username/mylibrary/")
//        licenses {
//            license {
//                name.set("The Apache License, Version 2.0")
//                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
//                distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
//            }
//        }
//        developers {
//            developer {
//                id.set("username")
//                name.set("User Name")
//                url.set("https://github.com/username/")
//            }
//        }
//        scm {
//            url.set("https://github.com/username/mylibrary/")
//            connection.set("scm:git:git://github.com/username/mylibrary.git")
//            developerConnection.set("scm:git:ssh://git@github.com/username/mylibrary.git")
//        }
//    }
//}
