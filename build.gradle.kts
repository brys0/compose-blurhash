import com.android.build.gradle.internal.scope.ProjectInfo.Companion.getBaseName
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.SonatypeHost
import org.jlleitschuh.gradle.ktlint.KtlintExtension


group = "io.github.brys0"
version = "0.0-3-ALPHA"

plugins {
    alias(libs.plugins.mavenPublish) apply false
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.androidKotlinMultiplatformLibrary) apply false
    alias(libs.plugins.ktlint) apply false
}

allprojects {
    apply {
        plugin("org.jlleitschuh.gradle.ktlint")
    } // Version should be inherited from parent

    configure<KtlintExtension> {
        version = rootProject.libs.versions.ktlint
    }
}


