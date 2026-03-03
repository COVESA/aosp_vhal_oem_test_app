plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
	kotlin("kapt")
	alias(libs.plugins.hilt.android)
}

android {
    namespace = "global.covesa.aosp.vhal.test.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "global.covesa.aosp.vhal.test.app"
        minSdk = 33
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
	kapt {
		correctErrorTypes = true
	}
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
	// Core
	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.lifecycle.runtime.ktx)
	implementation(libs.androidx.appcompat)

	// Hilt
	implementation(libs.hilt)
	kapt(libs.hilt.compiler)
	implementation(libs.hilt.navigation.compose)

	// Compose
	implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
	implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

	// Debug tooling
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

	// Automotive
	implementation("androidx.car.app:app:1.4.0")
	implementation("androidx.car.app:app-automotive:1.4.0")
	implementation("androidx.car:car:1.0.0-alpha5")
	compileOnly(files("libs/android.car.jar"))

	// Test
	testImplementation(libs.junit)

	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espresso.core)
	androidTestImplementation(platform(libs.androidx.compose.bom))
	androidTestImplementation(libs.androidx.ui.test.junit4)
}
