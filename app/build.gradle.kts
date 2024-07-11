import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

fun getProperties(path: String) = Properties().apply {
    load(FileInputStream(rootProject.file(path)))
}

android {
    namespace = "com.thk.github_action_test"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.thk.github_action_test"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        getProperties("development.properties").onEach { prop ->
            buildConfigField("String", prop.key as String, prop.value as String)
        }
    }

    signingConfigs {
        getByName("debug") {
            val keystoreProp = getProperties("keystore.properties")

            storeFile = File(keystoreProp.getProperty("storeFile"))
            storePassword = keystoreProp.getProperty("storePassword")
            keyAlias = keystoreProp.getProperty("keyAlias")
            keyPassword = keystoreProp.getProperty("keyPassword")
        }

        create("release") {
            val keystoreProp = getProperties("keystore.properties")

            storeFile = File(keystoreProp.getProperty("storeFile"))
            storePassword = keystoreProp.getProperty("storePassword")
            keyAlias = keystoreProp.getProperty("keyAlias")
            keyPassword = keystoreProp.getProperty("keyPassword")
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("debug")
        }

        release {
            signingConfig = signingConfigs.getByName("release")

            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}