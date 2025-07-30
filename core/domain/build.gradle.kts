import com.luxrobo.ble_simulator.setNamespace

plugins {
    alias(libs.plugins.luxrobo.android.library)
}

android {
    setNamespace("core.domain")
}

dependencies {
    implementation(projects.core.model)
    implementation(libs.inject)
}
