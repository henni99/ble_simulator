/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

group = "com.luxrobo.ble_simulator.buildlogic"

// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    compileOnly(libs.plugin.kotlin.serializationPlugin)
    compileOnly(libs.compose.compiler.gradle.plugin)
    compileOnly(libs.plugin.android.junit5)
}

gradlePlugin {
    plugins {
        register("androidHilt") {
            id = "luxrobo.android.hilt"
            implementationClass = "com.luxrobo.ble_simulator.HiltAndroidPlugin"
        }
        register("kotlinHilt") {
            id = "luxrobo.kotlin.hilt"
            implementationClass = "com.luxrobo.ble_simulator.HiltKotlinPlugin"
        }

        // Kotlin
        register("kotlinLibrary") {
            id = "luxrobo.kotlin.library"
            implementationClass = "KotlinLibraryConventionPlugin"
        }
        register("kotlinLibrarySerialization") {
            id = "luxrobo.kotlin.library.serialization"
            implementationClass = "KotlinLibrarySerializationConventionPlugin"
        }
    }
}
