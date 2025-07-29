import com.luxrobo.ble_simulator.setNamespace

plugins {
    alias(libs.plugins.luxrobo.android.library)
    alias(libs.plugins.luxrobo.android.compose)
}

android {
    setNamespace("core.designsystem")
}

dependencies {

    implementation(libs.androidx.appcompat)

    implementation(libs.landscapist.bom)
    implementation(libs.landscapist.coil)
    implementation(libs.landscapist.placeholder)

    implementation(libs.androidx.glance)
}
