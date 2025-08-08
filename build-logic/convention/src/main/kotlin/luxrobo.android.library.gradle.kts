import com.luxrobo.ble_simulator.configureCoroutineAndroid
import com.luxrobo.ble_simulator.configureHiltAndroid
import com.luxrobo.ble_simulator.configureKotest
import com.luxrobo.ble_simulator.configureKotlinAndroid
import com.luxrobo.ble_simulator.configureMock

plugins {
    id("com.android.library")
}

configureKotlinAndroid()
configureKotest()
configureMock()
configureCoroutineAndroid()
configureHiltAndroid()
