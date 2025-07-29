package com.luxrobo.ble_simulator

import org.gradle.api.Project

fun Project.setNamespace(name: String) {
    androidExtension.apply {
        namespace = "com.luxrobo.ble_simulator.$name"
    }
}
