import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)

    alias(libs.plugins.hiltAndroid)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ksp)
}

android {
    namespace = "ec.gob.sri.movil.app"
    compileSdk = libs.versions.compileSdk.get().toInt()

    val localProperties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists() && localPropertiesFile.isFile) {
        localPropertiesFile.inputStream().use { input ->
            localProperties.load(input)
        }
    }

    defaultConfig {
        applicationId = "ec.gob.sri.movil.app"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            val baseUrl = localProperties.getProperty("BASE_URL")
                ?: IllegalArgumentException("Property BASE_URL is not found in local.properties")
            buildConfigField("String", "BASE_URL", "\"$baseUrl\"")

            val contextApi = localProperties.getProperty("CONTEXT_API")
                ?: IllegalArgumentException("Property CONTEXT_API is not found in local.properties")
            buildConfigField("String", "CONTEXT_API", "\"$contextApi\"")
        }
        debug {
            val baseUrl = localProperties.getProperty("BASE_URL")
                ?: IllegalArgumentException("Property BASE_URL is not found in local.properties")
            buildConfigField("String", "BASE_URL", "\"$baseUrl\"")

            val contextApi = localProperties.getProperty("CONTEXT_API")
                ?: IllegalArgumentException("Property CONTEXT_API is not found in local.properties")
            buildConfigField("String", "CONTEXT_API", "\"$contextApi\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeCompiler {
        reportsDestination = file("build/outputs/compose_reports")
        metricsDestination = file("build/outputs/compose_metrics")
    }
}

dependencies {
    implementation(project(":feature:estado_tributario"))
    implementation(project(":common:framework"))
    implementation(project(":common:data"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.material3.adaptive)

    // Navigation
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.material3.adaptive.navigation3)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.androidx.compose.ui.text)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.androidx.hilt.compiler)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.kotlinx.serialization)
    implementation(libs.retrofit.converter)
    implementation(libs.interceptor)

    // Room
    implementation(libs.androidx.room.ktx)
    ksp(libs.room.compiler)
    implementation(libs.androidx.room.runtime)

    // Timber
    implementation(libs.timber)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}