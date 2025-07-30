import com.luxrobo.ble_simulator.setNamespace

plugins {
    alias(libs.plugins.luxrobo.android.library)
    alias(libs.plugins.luxrobo.kotlin.library.serialization)
    alias(libs.plugins.luxrobo.android.hilt)
}

setNamespace("core.virtual.api")


dependencies {
    implementation(projects.core.model)
    implementation(libs.kotlinx.datetime)

}