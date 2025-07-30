import com.luxrobo.ble_simulator.setNamespace

plugins {
    alias(libs.plugins.luxrobo.android.library)
    alias(libs.plugins.luxrobo.android.hilt)
    alias(libs.plugins.luxrobo.kotlin.library.serialization)
}

setNamespace("core.data")

dependencies {
    implementation(projects.core.model)

    implementation(projects.core.virtualApi)
}