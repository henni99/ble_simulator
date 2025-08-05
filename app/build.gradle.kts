import com.luxrobo.ble_simulator.filterProject

plugins {
    alias(libs.plugins.luxrobo.android.application)
    alias(libs.plugins.baselineprofile)
    alias(libs.plugins.roborazzi.plugin)
}

android {
    namespace = "com.luxrobo.ble_simulator"

    defaultConfig {
        applicationId = "com.luxrobo.ble_simulator"
        versionCode = 1
        versionName = "1.0"

    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/LICENSE-notice.md"
        }
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("debug")
        }

        create("benchmark") {
            matchingFallbacks.add("release")
            signingConfig = signingConfigs.getByName("debug")
            isDebuggable = false
        }
    }



}

dependencies {

    rootProject.subprojects.filterProject {
        if (it.name.contains("baselineprofile")) {
            baselineProfile(it)
        } else if (it.name.contains("testing")) {
            testImplementation(it)
        } else {
            implementation(it)
        }
    }
    implementation(libs.androidx.profileinstaller)
}