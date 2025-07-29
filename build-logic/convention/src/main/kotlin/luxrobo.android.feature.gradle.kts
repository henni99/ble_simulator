import com.luxrobo.ble_simulator.configureHiltAndroid
import com.luxrobo.ble_simulator.configureRoborazzi
import com.luxrobo.ble_simulator.findLibrary

plugins {
    id("luxrobo.android.library")
    id("luxrobo.android.compose")
}

android {
    packaging {
        resources {
            excludes.add("META-INF/**")
        }
    }
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

configureHiltAndroid()
configureRoborazzi()

dependencies {
    implementation(project(":core:designsystem"))

//
//    testImplementation(project(":core:testing"))

    implementation(findLibrary("hilt.navigation.compose"))
    implementation(findLibrary("androidx.compose.navigation"))
    androidTestImplementation(findLibrary("androidx.compose.navigation.test"))

    implementation(findLibrary("androidx.lifecycle.viewModelCompose"))
    implementation(findLibrary("androidx.lifecycle.runtimeCompose"))
}
