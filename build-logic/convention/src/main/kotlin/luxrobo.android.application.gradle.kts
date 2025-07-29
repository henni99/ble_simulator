import com.luxrobo.ble_simulator.configureHiltAndroid
import com.luxrobo.ble_simulator.configureKotestAndroid
import com.luxrobo.ble_simulator.configureKotlinAndroid
import com.luxrobo.ble_simulator.configureRoborazzi

plugins {
    id("com.android.application")
}

configureKotlinAndroid()
configureHiltAndroid()
configureKotestAndroid()
configureRoborazzi()
