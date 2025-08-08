import com.luxrobo.ble_simulator.filterProject

plugins {
    alias(libs.plugins.luxrobo.android.application)
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
        }
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("debug")
        }
    }
}

dependencies {

    rootProject.subprojects.filterProject {
        if (it.name.contains("testing")) {
            testImplementation(it)
        } else {
            implementation(it)
        }
    }
}