import com.luxrobo.ble_simulator.setNamespace

plugins {
    alias(libs.plugins.luxrobo.android.feature)
    id("kotlin-parcelize")
}

android {
    setNamespace("feature.data_transfer")
    
    packaging {
        resources {
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/LICENSE-notice.md"
        }
    }
}

dependencies {
    implementation(projects.core.model)
    implementation(projects.core.domain)

    implementation(libs.kotlinx.datetime)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.compose)
    implementation(libs.kotlinx.immutable)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    kspAndroidTest(libs.hilt.android.compiler)
    implementation(libs.kotlinx.immutable)
    androidTestImplementation(libs.hilt.android.testing)

}