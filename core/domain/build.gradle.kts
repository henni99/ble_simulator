import com.luxrobo.ble_simulator.setNamespace

plugins {
    alias(libs.plugins.luxrobo.android.library)
}

android {
    setNamespace("core.domain")
    
    packaging {
        resources {
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/LICENSE-notice.md"
        }
    }
}

dependencies {
    implementation(libs.kotlinx.datetime)
    implementation(projects.core.model)
    implementation(libs.inject)

    api(libs.junit4)
    api(libs.junit.vintage.engine)
    api(libs.kotlin.test)
    api(libs.mockk)
    api(libs.turbine)
}
