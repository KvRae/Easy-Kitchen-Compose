import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.serilization)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.compose.compiler)
    id("androidx.room")
}

val properties = Properties()
val localPropertiesFile = rootProject.file("local.properties")

if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use { properties.load(it) }
}

android {
    namespace = "com.kvrae.easykitchen"
    compileSdk = 36

    defaultConfig {
        buildConfigField("String", "API_KEY", "\"${properties.getProperty("API_KEY")}\"")
        buildConfigField("String", "BASE_URL", "\"${properties.getProperty("BASE_URL")}\"")
        buildConfigField("String", "BASE_URL_TEST", "\"${properties.getProperty("BASE_URL_TEST")}\"")
        buildConfigField("String", "CHAT_BASE_URL", "\"${properties.getProperty("CHAT_BASE_URL")}\"")

        applicationId = "com.kvrae.easykitchen"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {

    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    sourceSets {
        getByName("main") {
            assets {
                srcDirs("src\\main\\assets", "src\\main\\assets")
            }
        }
    }
    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {
    // Default App dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.androidx.compose.material.icons.extended)
    // Lottie
    implementation(libs.android.lottie.compose)
    // Navigation Compose
    implementation(libs.androidx.navigation.compose)
    // Coil
    implementation(libs.coil.kt)
    // Ktor
    implementation(libs.bundles.ktor)

    // Koin
    implementation(libs.koin.android.compose)
    // Room
    implementation(libs.bundles.room)
    annotationProcessor(libs.androidx.room.compiler)
    // google auth
    implementation(libs.google.auth.service)
    // google location
    implementation(libs.google.location.service)
    // datastore
    implementation(libs.androidx.datastore.preferences)

    // Shimmer
    implementation(libs.valentinilk.shimmer)
    // Accompanist Swipe Refresh
    implementation(libs.google.accompanist.swiperefresher)
    // Accompanist Permissions
    implementation(libs.google.accompanist.permissions)

    // Gemini
    implementation(libs.google.generativeai)

    // Compose Test dependencies
    testImplementation(libs.junit)

    //  AndroidX Test dependencies
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    // Debug dependencies
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
