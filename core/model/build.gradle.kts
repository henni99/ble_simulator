plugins {
    alias(libs.plugins.luxrobo.kotlin.library)
    alias(libs.plugins.luxrobo.kotlin.library.serialization)
}

dependencies {
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)
}
